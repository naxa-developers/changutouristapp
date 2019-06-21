package com.naxa.np.changunarayantouristapp.fetchdata;

import android.util.Log;

import com.google.gson.Gson;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonListViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class DataDownloadPresenterImpl implements DataDownloadPresenter {

    private static final String TAG = "DataDownloadPresntr";

    private DataDonwloadView dataDonwloadView;
    private GeoJsonCategoryViewModel geoJsonCategoryViewModel;
    private GeoJsonListViewModel geoJsonListViewModel;
    private PlaceDetailsEntityViewModel placeDetailsEntityViewModel;
    Gson gson;


    public DataDownloadPresenterImpl(DataDonwloadView dataDonwloadView, GeoJsonListViewModel geoJsonListViewModel, GeoJsonCategoryViewModel geoJsonCategoryViewModel, PlaceDetailsEntityViewModel placeDetailsEntityViewModel) {
        this.dataDonwloadView = dataDonwloadView;
        this.geoJsonCategoryViewModel = geoJsonCategoryViewModel;
        this.geoJsonListViewModel = geoJsonListViewModel;
        this.placeDetailsEntityViewModel = placeDetailsEntityViewModel;
        gson = new Gson();
    }

    @Override
    public void handleDataDownload(NetworkApiInterface apiInterface, String apiKey, String language) {

        int[] totalCount = new int[1];
        int[] progress = new int[1];
        String[] geoJsonDisplayName = new String[1];
        final String[] geoJsonName = new String[1];

        apiInterface.getGeoJsonCategoriesListResponse(Constant.Network.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(listResponse -> {
                    totalCount[0] = listResponse.getData().size();
                    return listResponse.getData();
                })
                .flatMapIterable((Function<List<GeoJsonCategoryListEntity>, Iterable<GeoJsonCategoryListEntity>>) entities -> entities)
                .flatMap((Function<GeoJsonCategoryListEntity, ObservableSource<ResponseBody>>) categoryListEntity -> {
                    geoJsonCategoryViewModel.insert(categoryListEntity);
                    geoJsonDisplayName[0] = categoryListEntity.getCategoryName();
                    geoJsonName[0] = categoryListEntity.getCategoryTable();

                    return apiInterface.getGeoJsonDetails(Constant.Network.API_KEY, categoryListEntity.getCategoryTable()).subscribeOn(Schedulers.io());
                })
                .doOnNext(responseBody -> {
                    progress[0]++;
                    dataDonwloadView.downloadProgress(progress[0], totalCount[0], geoJsonDisplayName[0]);
                })
                .map(responseBody -> {
                    FeatureCollection featureCollection = FeatureCollection.fromJson(responseBody.string());
                    return featureCollection.features();
                })
                .toList()
                .map(lists -> {
                    List<Feature> features = new ArrayList<>();
                    for (List<Feature> list: lists){
                        features.addAll(list);
                    }
                    return features;
                })
                .flattenAsObservable(new Function<List<Feature>, Iterable<Feature>>() {
                    @Override
                    public Iterable<Feature> apply(List<Feature> features) throws Exception {
                        return features;
                    }
                })
                .map(new Function<Feature, Feature>() {
                    @Override
                    public Feature apply(Feature feature) throws Exception {
                        return feature;
                    }
                })
                .subscribe(new DisposableObserver<Feature>() {
                    @Override
                    public void onNext(Feature feature) {
                        String snippest = feature.properties().toString();
                        PlacesDetailsEntity placesDetailsEntity = gson.fromJson(snippest, PlacesDetailsEntity.class);
                        placesDetailsEntity.setCategoryType(geoJsonName[0]);
                        placeDetailsEntityViewModel.insert(placesDetailsEntity);
                        Log.d(TAG, "onNext: JSON Object " + snippest);


//                        LatLng location = new LatLng(0.0, 0.0);
//                        try {
//                            JSONObject jsonObject = new JSONObject(feature.geometry().toJson());
//                            Log.d(TAG, "onNext: JSON Object Co-ordinates " + jsonObject.getJSONArray("coordinates").getString(0));
//                            String Lon = jsonObject.getJSONArray("coordinates").getString(0);
//                            String Lat = jsonObject.getJSONArray("coordinates").getString(1);
//                            location = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lon));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof SocketTimeoutException) {
                            dataDonwloadView.downloadFailed("Slow Internet Connection, Please try again later.");
                        } else {
                            dataDonwloadView.downloadFailed(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        dataDonwloadView.downloadSuccess("All Data Downloaded Successfully");
                        SharedPreferenceUtils.getInstance(ChangunarayanTouristApp.getInstance()).setValue(Constant.SharedPrefKey.IS_PLACES_DATA_ALREADY_EXISTS, true);
                    }
                });


    }
}
