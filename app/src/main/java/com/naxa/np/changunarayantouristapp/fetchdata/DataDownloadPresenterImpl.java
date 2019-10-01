package com.naxa.np.changunarayantouristapp.fetchdata;

import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonListViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.map.featurecollection.Feature;
import com.naxa.np.changunarayantouristapp.map.featurecollection.FeatureCollectionModel;
import com.naxa.np.changunarayantouristapp.mayormessage.MayorMessagesListResponse;
import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlaceListDetailsResponse;
import com.naxa.np.changunarayantouristapp.selectlanguage.LanguageDetailsResponse;
import com.naxa.np.changunarayantouristapp.touristinformationguide.TouristInformationGuideListResponse;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.naxa.np.changunarayantouristapp.utils.Constant.Network.API_KEY;


public class DataDownloadPresenterImpl implements DataDownloadPresenter {

    private static final String TAG = "DataDownloadPresntr";

    private DataDonwloadView dataDonwloadView;
    private GeoJsonCategoryViewModel geoJsonCategoryViewModel;
    private GeoJsonListViewModel geoJsonListViewModel;
    private PlaceDetailsEntityViewModel placeDetailsEntityViewModel;
    AppCompatActivity appCompatActivity;
    Gson gson;


    public DataDownloadPresenterImpl(DataDonwloadView dataDonwloadView, AppCompatActivity appCompatActivity, GeoJsonListViewModel geoJsonListViewModel, GeoJsonCategoryViewModel geoJsonCategoryViewModel, PlaceDetailsEntityViewModel placeDetailsEntityViewModel) {
        this.dataDonwloadView = dataDonwloadView;
        this.appCompatActivity = appCompatActivity;
        this.geoJsonCategoryViewModel = geoJsonCategoryViewModel;
        this.geoJsonListViewModel = geoJsonListViewModel;
        this.placeDetailsEntityViewModel = placeDetailsEntityViewModel;
        gson = new Gson();
    }

    int totalCount;
    int progress;
    String geoJsonDisplayName;

    @Override
    public void handleDataDownload(NetworkApiInterface apiInterface, String apiKey, String language) {

        totalCount = 0;
        progress = 0;

        List<String> distinctCategoryType = new ArrayList<>();
        List<GeoJsonCategoryListEntity> distinctCategoryEntityListType = new ArrayList<>();

        apiInterface.getGeoJsonCategoriesListResponse(API_KEY)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(listResponse -> {
                    if (listResponse.getData() != null) {
                        geoJsonCategoryViewModel.insertAll(listResponse.getData());
                        downloadMainPlaceListDetails(apiInterface, apiKey, language);
//                        totalCount = listResponse.getData().size();
                        for(GeoJsonCategoryListEntity geoJsonCategoryListEntity : listResponse.getData()){

                            if(!distinctCategoryType.contains(geoJsonCategoryListEntity.getCategoryTable())){
                                distinctCategoryType.add(geoJsonCategoryListEntity.getCategoryTable());
                                distinctCategoryEntityListType.add(geoJsonCategoryListEntity);
                            }
                        }
                        totalCount = distinctCategoryEntityListType.size();
                        return distinctCategoryEntityListType;
                    }else {
                        List<GeoJsonCategoryListEntity> geoJsonCategoryListEntityList = new ArrayList<>();
                        return geoJsonCategoryListEntityList;
                    }
                })
                .flatMapIterable(new Function<List<GeoJsonCategoryListEntity>, Iterable<GeoJsonCategoryListEntity>>() {
                    @Override
                    public Iterable<GeoJsonCategoryListEntity> apply(List<GeoJsonCategoryListEntity> entities) throws Exception {

                        return entities;
                    }
                })
                .flatMap(new Function<GeoJsonCategoryListEntity, ObservableSource<Feature>>() {
                    @Override
                    public ObservableSource<Feature> apply(GeoJsonCategoryListEntity categoryListEntity) throws Exception {
                        geoJsonDisplayName = categoryListEntity.getCategoryName();
                        String geoJsonName = categoryListEntity.getCategoryTable();

                        return apiInterface.getGeoJsonDetails(API_KEY, categoryListEntity.getSlug(), categoryListEntity.getCategoryTable())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .retryWhen(observable -> Observable.timer(5, TimeUnit.SECONDS))
                                .doOnNext(responseBody -> {
                                    progress++;
                                    dataDonwloadView.downloadProgress(progress, totalCount, categoryListEntity.getSubCategories(), categoryListEntity.getCategoryTable(), categoryListEntity.getCategoryMarker());
                                })
                                .map(responseBody -> {
                                    try {
                                        FeatureCollectionModel featureCollection = gson.fromJson(DataDownloadPresenterImpl.this.getJsonStringFromResponseBody(responseBody), FeatureCollectionModel.class);
                                        return featureCollection.getFeatures();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.d(TAG, "handleDataDownload: catch " + " type : " + categoryListEntity.getCategoryTable() + " , " + responseBody.string());
                                        return new ArrayList<Feature>();
                                    }
                                })
                                .toList()
                                .map(lists -> {
                                    Log.d(TAG, "handleDataDownload: mapLists : " + categoryListEntity.getCategoryTable() + " : " + lists.size());
                                    List<Feature> features = new ArrayList<>();
                                    for (List<Feature> list : lists) {
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
                                }).doOnNext(new Consumer<Feature>() {
                                    @Override
                                    public void accept(Feature feature) throws Exception {
                                        PlacesDetailsEntity placesDetailsEntity = feature.getProperties();
                                        placesDetailsEntity.setCategoryType(geoJsonName);

                                        LatLng latLng = getLocationFromGeometry(feature);
                                        placesDetailsEntity.setLatitude(latLng.getLatitude() + "");
                                        placesDetailsEntity.setLongitude(latLng.getLongitude() + "");
                                        if (TextUtils.isEmpty(placesDetailsEntity.getQRCode())) {
                                            placesDetailsEntity.setQrLanguage(placesDetailsEntity.getLanguage() + "_" + placesDetailsEntity.getName());
                                        } else {
                                            placesDetailsEntity.setQrLanguage(placesDetailsEntity.getLanguage() + "_" + placesDetailsEntity.getQRCode());
                                        }

                                        placeDetailsEntityViewModel.insert(placesDetailsEntity);
                                    }
                                });

                    }
                })
                .subscribe(new DisposableObserver<Feature>() {
                    @Override
                    public void onNext(Feature feature) {


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
                        fetchMayorMessage(apiInterface, apiKey, language);
                        fetchTouristInfoDatFromServer(apiInterface, apiKey, language);
                        fetchLanguageListFromServer(apiInterface);
                        dataDonwloadView.downloadSuccess("All Data Downloaded Successfully");
                        SharedPreferenceUtils.getInstance(ChangunarayanTouristApp.getInstance()).setValue(Constant.SharedPrefKey.IS_PLACES_DATA_ALREADY_EXISTS, true);
                        Log.d(TAG, "onComplete: ");
                    }
                });


    }


    private String getJsonStringFromResponseBody (@NotNull ResponseBody responseBody) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        String geoJsonToString = sb.toString();

        Log.d(TAG, "getJsonStringFromResponseBody: "+geoJsonToString);
        return geoJsonToString;
    }

    private LatLng getLocationFromGeometry (@NotNull Feature feature){
                                LatLng location = new LatLng(0.0, 0.0);
                        try {
                            String Lon = feature.getGeometry().getCoordinates().get(0).toString();
                            String Lat = feature.getGeometry().getCoordinates().get(1).toString();
                            location = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lon));
                            Log.d(TAG, "onNext: JSON Object Co-ordinates " + feature.getGeometry().getCoordinates().toString());

                            return  location;
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return location;
                        }
    }


    private void downloadMainPlaceListDetails(@NotNull NetworkApiInterface apiInterface, String apiKey, String language) {

        apiInterface.getMainPlacesListDetails(apiKey, language)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .retryWhen(observable -> Observable.timer(5, TimeUnit.SECONDS))
                .subscribe(new DisposableObserver<MainPlaceListDetailsResponse>() {
                    @Override
                    public void onNext(MainPlaceListDetailsResponse mainPlaceListDetailsResponse) {

                        SharedPreferenceUtils.getInstance(appCompatActivity).setValue(Constant.SharedPrefKey.KEY_MAIN_PLACES_list_DETAILS, gson.toJson(mainPlaceListDetailsResponse));

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void fetchMayorMessage(@NotNull NetworkApiInterface apiInterface, String apiKey, String language) {
        apiInterface.getMaoyorMessagesListDetails(apiKey)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .retryWhen(observable -> Observable.timer(5, TimeUnit.SECONDS))
                .subscribe(new DisposableObserver<MayorMessagesListResponse>() {
                    @Override
                    public void onNext(MayorMessagesListResponse mayorMessagesListResponse) {

                        SharedPreferenceUtils.getInstance(appCompatActivity).setValue(Constant.SharedPrefKey.KEY_MAYOR_MESSAGE_DETAILS, gson.toJson(mayorMessagesListResponse));

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    private void fetchLanguageListFromServer(@NotNull NetworkApiInterface apiInterface) {
        apiInterface.getLanguages(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<LanguageDetailsResponse>() {
                    @Override
                    public void onNext(LanguageDetailsResponse languageDetailsResponse) {

                        if (languageDetailsResponse.getError() == 0) {
                            if (languageDetailsResponse.getData() != null) {
                                SharedPreferenceUtils.getInstance(appCompatActivity).setValue(Constant.SharedPrefKey.KEY_LANGUAGE_LIST_DETAILS, gson.toJson(languageDetailsResponse));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void fetchTouristInfoDatFromServer(@NotNull NetworkApiInterface apiInterface, String apiKey, String language) {
        apiInterface.getTouristInformationGuideListResponse(apiKey, language)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(observable -> Observable.timer(5, TimeUnit.SECONDS))
                .subscribe(new DisposableObserver<TouristInformationGuideListResponse>() {
                    @Override
                    public void onNext(TouristInformationGuideListResponse touristInformationGuideListResponse) {

                        if (touristInformationGuideListResponse.getError() == 0) {
                            if (touristInformationGuideListResponse.getData() != null) {
                                SharedPreferenceUtils.getInstance(appCompatActivity).setValue(Constant.SharedPrefKey.KEY_TOURIST_INFORMATION_GUIDE_DETAILS, gson.toJson(touristInformationGuideListResponse));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }



}
