package com.naxa.np.changunarayantouristapp.fetchdata;

import android.text.TextUtils;
import android.util.Log;

import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonListEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonListViewModel;
import com.naxa.np.changunarayantouristapp.map.mapcategory.GeojsonCategoriesListResponse;
import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

public class DataDownloadPresenterImpl implements DataDownloadPresenter {

    private DataDonwloadView dataDonwloadView;
   private GeoJsonCategoryViewModel geoJsonCategoryViewModel;
    private GeoJsonListViewModel geoJsonListViewModel;



    public DataDownloadPresenterImpl(DataDonwloadView dataDonwloadView, GeoJsonListViewModel geoJsonListViewModel, GeoJsonCategoryViewModel geoJsonCategoryViewModel) {
        this.dataDonwloadView = dataDonwloadView;
        this.geoJsonCategoryViewModel = geoJsonCategoryViewModel;
        this.geoJsonListViewModel = geoJsonListViewModel;
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

                        return apiInterface.getGeoJsonDetails(Constant.Network.API_KEY,geoJsonCategoryEntity.getCategoryTable());
                    }

                })
                .retry(Constant.Network.KEY_MAX_RETRY_COUNT)
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody s) {
                                progress[0]++;
                                dataDonwloadView.downloadProgress(progress[0], totalCount[0], geoJsonDisplayName[0]);


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

//                            JSONObject jsonObject = null;
//                            try {
//                                int latlongDiffCounter = 0;
//
//                                jsonObject = new JSONObject(geoJsonToString);
//
//                                JSONArray jsonarray = new JSONArray(jsonObject.getString("features"));
//                                Log.d(TAG, "onNext: " + "save data to database --> " + geoJsonName[0] + " , " + geoJsonBaseType[0]);
//
//                                for (int i = 0; i < jsonarray.length(); i++) {
//                                    JSONObject properties = new JSONObject(jsonarray.getJSONObject(i).getString("properties"));
//                                    JSONObject geometry = new JSONObject(jsonarray.getJSONObject(i).getString("geometry"));
//                                    JSONArray coordinates = geometry.getJSONArray("coordinates");
//
//                                    String name = properties.has(summaryName[0]) ? properties.getString(summaryName[0])
//                                            : properties.has("name") ? properties.getString("name")
//                                            : properties.has("Name") ? properties.getString("Name")
//                                            : properties.has("Name of Bank Providing ATM Service") ? properties.getString("Name of Bank Providing ATM Service")
//                                            : "null";
//
//                                    String address = properties.has("address") ? properties.getString("address") : properties.has("Address") ? properties.getString("Address") : " ";
//
//                                    String type = geometry.getString("type");
//
//
//                                    double longitude;
//                                    double latitude;
//                                    double latlongMakeDiff = Double.parseDouble("0.000000000000"+latlongDiffCounter+i);
//                                    if (type.equals("Point")) {
//                                        longitude = Double.parseDouble(coordinates.get(0).toString()) + latlongMakeDiff;
//                                        latitude = Double.parseDouble(coordinates.get(1).toString()) + latlongMakeDiff;
//                                    } else if (type.equals("MultiPolygon")) {
//                                        JSONArray coordinates1 = coordinates.getJSONArray(0);
//                                        JSONArray coordinates2 = coordinates1.getJSONArray(0);
//                                        JSONArray coordinates3 = coordinates2.getJSONArray(0);
//
//                                        longitude = Double.parseDouble(coordinates3.get(0).toString()) + latlongMakeDiff;
//                                        latitude = Double.parseDouble(coordinates3.get(1).toString()) + latlongMakeDiff;
//                                    } else {
//// for multiLineString
//                                        JSONArray coordinates1 = coordinates.getJSONArray(0);
//                                        JSONArray coordinates2 = coordinates1.getJSONArray(0);
//
//                                        longitude = Double.parseDouble(coordinates2.get(0).toString()) + latlongMakeDiff;
//                                        latitude = Double.parseDouble(coordinates2.get(1).toString()) + latlongMakeDiff;
//                                    }
//
//
//                                    latlongDiffCounter++;
//                                    if(latlongDiffCounter > 9){
//                                        latlongDiffCounter = 0;
//                                    }
//
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
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
