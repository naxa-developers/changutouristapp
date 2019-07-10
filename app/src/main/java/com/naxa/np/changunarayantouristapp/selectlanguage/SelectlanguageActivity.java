package com.naxa.np.changunarayantouristapp.selectlanguage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.naxa.np.changunarayantouristapp.MainActivity;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.mayormessage.MayorMessageActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.NetworkUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Network.API_KEY;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Permission.STORAGE_READ;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Permission.STORAGE_WRITE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.PermissionID.RC_STORAGE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.IS_APP_FIRST_TIME_LAUNCH;

public class SelectlanguageActivity extends BaseActivity {

    private static final String TAG = "SelectlanguageActivity";

    Toolbar toolbar;
    RecyclerView recyclerView;
    private BaseRecyclerViewAdapter<LanguageDetails, SelectLanguageViewHolder> adapter;

    Gson gson;
    boolean isFromMainActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectlanguage);
        gson = new Gson();

        setupToolbar("Select Language", false);
        initUI();

        newGetIntent(getIntent());

    }

    private void newGetIntent(Intent intent) {
        if (intent != null) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            if(hashMap != null) {
                isFromMainActivity = (boolean) hashMap.get(KEY_VALUE);
            }

        }
    }

    private void initlanguagesList() {
        if (NetworkUtils.isNetworkAvailable()) {
            fetchLanguageListFromServer();
        } else {
            LanguageDetailsResponse languageDetailsResponse = gson.fromJson(SharedPreferenceUtils.getInstance(SelectlanguageActivity.this).getStringValue(Constant.SharedPrefKey.KEY_LANGUAGE_LIST_DETAILS, null), LanguageDetailsResponse.class);
            if (languageDetailsResponse.getData() != null) {
                setuprecyclerView(languageDetailsResponse.getData());
            }
        }
    }


    private void initUI() {

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_language_selector);

        initlanguagesList();
    }

    Dialog dialog;

    private void fetchLanguageListFromServer() {
        dialog = DialogFactory.createProgressDialog(this, "Please wait!!!\nfetching data.");
        dialog.show();
        apiInterface.getLanguages(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<LanguageDetailsResponse>() {
                    @Override
                    public void onNext(LanguageDetailsResponse languageDetailsResponse) {

                        if (languageDetailsResponse == null) {
                            dialog = DialogFactory.createSimpleOkErrorDialog(SelectlanguageActivity.this, "Data Fetch Error", "unable to download data ");
                            dialog.show();
                            return;
                        }

                        if (languageDetailsResponse.getError() == 0) {
                            dialog.dismiss();
                            if (languageDetailsResponse.getData() != null) {
                                SharedPreferenceUtils.getInstance(SelectlanguageActivity.this).setValue(Constant.SharedPrefKey.KEY_LANGUAGE_LIST_DETAILS, gson.toJson(languageDetailsResponse));
                                setuprecyclerView(languageDetailsResponse.getData());
                            }
                        } else {
                            dialog.dismiss();
                            dialog = DialogFactory.createSimpleOkErrorDialog(SelectlanguageActivity.this, "Data Fetch Error", languageDetailsResponse.getMessage());
                            dialog.show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog = DialogFactory.createSimpleOkErrorDialog(SelectlanguageActivity.this, "Data Fetch Error", e.getMessage());
                        dialog.show();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }


    private void setuprecyclerView(List<LanguageDetails> languageDetailsList) {

        if (languageDetailsList == null) {
            return;
        }
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<LanguageDetails, SelectLanguageViewHolder>(languageDetailsList, R.layout.language_selector_row_item_layout) {

            @Override
            public void viewBinded(SelectLanguageViewHolder selectLanguageViewHolder, final LanguageDetails languageDetails, int position) {
                selectLanguageViewHolder.bindView(languageDetails);
                selectLanguageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: " + languageDetails.getName());
                        SharedPreferenceUtils.getInstance(SelectlanguageActivity.this).setValue(Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE, languageDetails.getAlias());

                        if (isFromMainActivity) {
                            launchMainActivity();
                        } else {
                            launchLoginScreen();
                        }
                    }
                });

            }

            @Override
            public SelectLanguageViewHolder attachViewHolder(View view) {
                return new SelectLanguageViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }


    private void launchLoginScreen() {

        checkPermission(RC_STORAGE, new String[]{STORAGE_READ, STORAGE_WRITE}, getString(R.string.storage_rationale),
                new PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        SharedPreferenceUtils.getInstance(SelectlanguageActivity.this).setValue(IS_APP_FIRST_TIME_LAUNCH, false);

                        ActivityUtil.openActivity(MayorMessageActivity.class, SelectlanguageActivity.this);
                        finish();
                    }

                    @Override
                    public void onPermissionDenied() {
                    }
                });


    }


    private void launchMainActivity() {
        checkPermission(RC_STORAGE, new String[]{STORAGE_READ, STORAGE_WRITE}, getString(R.string.storage_rationale),
                new PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        SharedPreferenceUtils.getInstance(SelectlanguageActivity.this).setValue(Constant.SharedPrefKey.IS_PLACES_DATA_ALREADY_EXISTS, false);

                       if(NetworkUtils.isNetworkAvailable()) {
                           ActivityUtil.openActivity(MainActivity.class, SelectlanguageActivity.this);
                           finish();
                       }else {
                           dialog = DialogFactory.createSimpleOkErrorDialog(SelectlanguageActivity.this, getResources().getString(R.string.no_internet_connection), getResources().getString(R.string.check_internet_retry_again));
                           dialog.show();
                       }
                    }

                    @Override
                    public void onPermissionDenied() {
                    }
                });
    }

}
