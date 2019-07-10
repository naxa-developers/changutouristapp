package com.naxa.np.changunarayantouristapp.touristinformationguide;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.selectlanguage.LanguageDetailsResponse;
import com.naxa.np.changunarayantouristapp.selectlanguage.SelectlanguageActivity;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.NetworkUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TourishInformationGuideActivity extends BaseActivity {

    RecyclerView recyclerView;
    private BaseRecyclerViewAdapter<TouristInformationGuideDetails, TouristGuideListViewHolder> adapter;

    Dialog dialog;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourish_information_guide);

        gson = new Gson();

        setupToolbar("Tourist Information Guide", false);

        initUI();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.rv_tourist_guide_info);

        fetchData();
    }

    private void fetchData() {
        if (NetworkUtils.isNetworkAvailable()) {
            dialog = DialogFactory.createProgressDialog(this, "Please wait!!!\nfetching data.");
            dialog.show();
            fetchDatFromServer();
        } else {
            fetchDataFromSharedPrefs();
        }
    }

    private void fetchDataFromSharedPrefs() {

        TouristInformationGuideListResponse touristInformationGuideListResponse = gson.fromJson(SharedPreferenceUtils.getInstance(TourishInformationGuideActivity.this).getStringValue(Constant.SharedPrefKey.KEY_TOURIST_INFORMATION_GUIDE_DETAILS, null), TouristInformationGuideListResponse.class);
        if (touristInformationGuideListResponse.getData() != null) {
            setupRecyclerView(touristInformationGuideListResponse.getData());
        }

    }

    private void fetchDatFromServer() {
        apiInterface.getTouristInformationGuideListResponse(Constant.Network.API_KEY)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(observable -> Observable.timer(5, TimeUnit.SECONDS))
                .subscribe(new DisposableObserver<TouristInformationGuideListResponse>() {
                    @Override
                    public void onNext(TouristInformationGuideListResponse touristInformationGuideListResponse) {

                        if (touristInformationGuideListResponse == null) {
                            dialog = DialogFactory.createSimpleOkErrorDialog(TourishInformationGuideActivity.this, "Data Fetch Error", "unable to download data ");
                            dialog.show();
                            return;
                        }

                        if (touristInformationGuideListResponse.getError() == 0) {
                            dialog.dismiss();
                            if (touristInformationGuideListResponse.getData() != null) {
                                SharedPreferenceUtils.getInstance(TourishInformationGuideActivity.this).setValue(Constant.SharedPrefKey.KEY_TOURIST_INFORMATION_GUIDE_DETAILS, gson.toJson(touristInformationGuideListResponse));
                                setupRecyclerView(touristInformationGuideListResponse.getData());
                            }
                        } else {
                            dialog = DialogFactory.createSimpleOkErrorDialog(TourishInformationGuideActivity.this, "Data Fetch Error", touristInformationGuideListResponse.getMessage());
                            dialog.show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog = DialogFactory.createSimpleOkErrorDialog(TourishInformationGuideActivity.this, "Data Fetch Error", e.getMessage());
                        dialog.show();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });

    }

    private void setupRecyclerView(List<TouristInformationGuideDetails> touristInformationGuideDetailsList) {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<TouristInformationGuideDetails, TouristGuideListViewHolder>(touristInformationGuideDetailsList, R.layout.tourist_info_guide_row_item_layout) {

            @Override
            public void viewBinded(TouristGuideListViewHolder touristGuideListViewHolder, TouristInformationGuideDetails touristInformationGuideDetails, int position) {
                Log.d(TAG, "viewBinded: " + position);
                touristGuideListViewHolder.bindView(touristInformationGuideDetails);
                touristGuideListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }

            @Override
            public TouristGuideListViewHolder attachViewHolder(View view) {
                return new TouristGuideListViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }


}
