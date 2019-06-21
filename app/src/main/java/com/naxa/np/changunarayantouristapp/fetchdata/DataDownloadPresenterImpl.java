package com.naxa.np.changunarayantouristapp.fetchdata;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonListEntity;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonListViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.map.mapcategory.GeojsonCategoriesListResponse;
import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.Observable;
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

        apiInterface
                .getGeoJsonCategoriesListResponse(Constant.Network.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<GeojsonCategoriesListResponse, ObservableSource<List<GeoJsonCategoryListEntity>>>() {
                    @Override
                    public ObservableSource<List<GeoJsonCategoryListEntity>> apply(GeojsonCategoriesListResponse geoJsonCategoryDetails) throws Exception {
                        totalCount[0] = geoJsonCategoryDetails.getData().size();
                        Log.d(TAG, "apply: total count : "+totalCount[0]);
                        return Observable.just(geoJsonCategoryDetails.getData());
                    }
                })
                .flatMapIterable(new Function<List<GeoJsonCategoryListEntity>, Iterable<GeoJsonCategoryListEntity>>() {
                    @Override
                    public Iterable<GeoJsonCategoryListEntity> apply(List<GeoJsonCategoryListEntity> geoJsonCategoryEntities) throws Exception {
                        return geoJsonCategoryEntities;
                    }
                })
                .flatMap(new Function<GeoJsonCategoryListEntity, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(GeoJsonCategoryListEntity geoJsonCategoryEntity) throws Exception {

                        geoJsonCategoryViewModel.insert(geoJsonCategoryEntity);
                        geoJsonDisplayName[0] = geoJsonCategoryEntity.getCategoryName();
                        geoJsonName[0] = geoJsonCategoryEntity.getCategoryTable();
                        Log.d(TAG, "apply: category_name : "+ geoJsonName[0]);
                        return apiInterface.getGeoJsonDetails(Constant.Network.API_KEY,geoJsonCategoryEntity.getCategoryTable());
                    }

                })
                .retry(Constant.Network.KEY_MAX_RETRY_COUNT)
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody s) {
//                                progress[0]++;
//                                dataDonwloadView.downloadProgress(progress[0], totalCount[0], geoJsonDisplayName[0]);


                        BufferedReader reader = new BufferedReader(new InputStreamReader(s.byteStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        try {

                            while ((line = reader.readLine()) != null) {
                                sb.append(line).append("\n");
                            }
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String geoJsonToString = sb.toString();
                        Log.d(TAG, "onNext: GeoJson " + sb.toString());

                        if (!TextUtils.isEmpty(geoJsonToString)) {
                            geoJsonListViewModel.insert(new GeoJsonListEntity(geoJsonName[0], geoJsonToString));

//                            json parse and store to database

                            FeatureCollection featureCollection = FeatureCollection.fromJson(geoJsonToString);
                            List<Feature> featureList  = featureCollection.features();

                            Observable.just(featureList)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .flatMapIterable(new Function<List<Feature>, Iterable<Feature>>() {
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
                                            Log.d(TAG, "onNext: JSON Object "+snippest);
                                            Log.d(TAG, "onNext: JSON Object Geometry "+feature.geometry().toJson());


                                            LatLng location = new LatLng(0.0, 0.0);
                                            try {
                                                JSONObject jsonObject = new JSONObject(feature.geometry().toJson());
                                                Log.d(TAG, "onNext: JSON Object Co-ordinates "+jsonObject.getJSONArray("coordinates").getString(0));
                                                String Lon = jsonObject.getJSONArray("coordinates").getString(0) ;
                                                String Lat = jsonObject.getJSONArray("coordinates").getString(1) ;
                                                location = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lon));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ToastUtils.showShortToast("Error reading Json file");
                                        }

                                        @Override
                                        public void onComplete() {
                                            Log.d(TAG, "onComplete: ");

//                        runThread(items);
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof SocketTimeoutException) {
                            dataDonwloadView.downloadFailed("Slow Internet Connection, Please try again later.");
                        }else {
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
