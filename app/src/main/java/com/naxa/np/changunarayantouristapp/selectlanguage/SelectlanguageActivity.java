package com.naxa.np.changunarayantouristapp.selectlanguage;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDonwloadView;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDownloadPresenter;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.login.LoginActivity;
import com.naxa.np.changunarayantouristapp.splashscreen.WalkThroughSliderActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission_group.STORAGE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Network.API_KEY;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Permission.STORAGE_READ;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Permission.STORAGE_WRITE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.PermissionID.RC_STORAGE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.IS_APP_FIRST_TIME_LAUNCH;

public class SelectlanguageActivity extends BaseActivity implements DataDonwloadView {

    private static final String TAG = "SelectlanguageActivity";

    Toolbar toolbar;
    RecyclerView recyclerView;
    DataDownloadPresenter dataDownloadPresenter;
    private BaseRecyclerViewAdapter<LanguageDetails, SelectLanguageViewHolder> adapter;

    Gson gson ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectlanguage);
        gson = new Gson();
        dataDownloadPresenter = new DataDownloadPresenterImpl(this);

        setupToolbar("Select Language", false);
        initUI();

    }

    private void initUI(){

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rv_language_selector);

        fetchLanguageListFromServer();
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

                        if(languageDetailsResponse == null){
                            dialog = DialogFactory.createSimpleOkErrorDialog(SelectlanguageActivity.this, "Data Fetch Error", "unable to download data ");
                            dialog.show();
                            return;
                        }

                        if(languageDetailsResponse.getError() == 0){
                            dialog.dismiss();
                            if(languageDetailsResponse.getData()!= null){
                                SharedPreferenceUtils.getInstance(SelectlanguageActivity.this).setValue(Constant.SharedPrefKey.KEY_LANGUAGE_LIST_DETAILS, gson.toJson(languageDetailsResponse) );
                                setuprecyclerView(languageDetailsResponse.getData());
                            }
                        }else {
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

        if(languageDetailsList == null){
            return;
        }

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<LanguageDetails, SelectLanguageViewHolder>(languageDetailsList, R.layout.language_selector_row_item_layout){

            @Override
            public void viewBinded(SelectLanguageViewHolder selectLanguageViewHolder, final LanguageDetails languageDetails, int position) {
                selectLanguageViewHolder.bindView(languageDetails);
                selectLanguageViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: "+ languageDetails.getName());

                        dataDownloadPresenter.handleDataDownload(apiInterface, API_KEY, languageDetails.getLanguage());
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

        checkPermission(RC_STORAGE, new String[] {STORAGE_READ, STORAGE_WRITE}, getString(R.string.storage_rationale),
        new PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                SharedPreferenceUtils.getInstance(SelectlanguageActivity.this).setValue(IS_APP_FIRST_TIME_LAUNCH, false);

                ActivityUtil.openActivity(LoginActivity.class, SelectlanguageActivity.this);
                finish();
            }

            @Override
            public void onPermissionDenied() {
            }
        });


    }

    @Override
    public void downloadProgress(int progress, int total, String successMsg) {

    }

    @Override
    public void downloadSuccess(String successMsg) {
        launchLoginScreen();
    }

    @Override
    public void downloadFailed(String failedMsg) {

    }
}
