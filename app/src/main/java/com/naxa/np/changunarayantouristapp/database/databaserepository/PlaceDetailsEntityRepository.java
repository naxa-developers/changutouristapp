package com.naxa.np.changunarayantouristapp.database.databaserepository;

import android.app.Application;
import android.util.Log;

import com.naxa.np.changunarayantouristapp.database.ISETRoomDatabase;
import com.naxa.np.changunarayantouristapp.database.dao.PlaceDetailsDao;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PlaceDetailsEntityRepository {
    private PlaceDetailsDao placeDetailsDao;
    private Flowable<List<PlacesDetailsEntity>> mAllPlacesDetailsEntity;
    private Flowable<List<PlacesDetailsEntity>> mSpecificPlacesDetailsEntityList;
    private Single<PlacesDetailsEntity> mSpecificPlacesDetailsEntity;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public PlaceDetailsEntityRepository(Application application) {
        ISETRoomDatabase db = ISETRoomDatabase.getDatabase(application);
        placeDetailsDao = db.placeDetailsDao();
        mAllPlacesDetailsEntity = placeDetailsDao.getAllPlacesDetailsEntity();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public Flowable<List<PlacesDetailsEntity>> getAllPlacesDetailsEntity() {
        return mAllPlacesDetailsEntity;
    }

    public Flowable<List<PlacesDetailsEntity>> getPlacesDetailsEntityBYPlaceAndCategoryType(String placeType, String categoryType,  String language) {
        mSpecificPlacesDetailsEntityList = placeDetailsDao.getPlacesDetailsEntityBYPlaceAndCategoryType(placeType, categoryType, language);
        return mSpecificPlacesDetailsEntityList;
    }

    public Single<PlacesDetailsEntity> getPlacesDetailsEntityBYQRCode(String qrCode, String language) {
        mSpecificPlacesDetailsEntity = placeDetailsDao.getPlacesDetailsEntityBYQRCode(qrCode, language);
        return mSpecificPlacesDetailsEntity;
    }

    public Flowable<List<PlacesDetailsEntity>> getNearByPlacesListByPlaceTypeAndNearByTypeList(String placeType, List<String> nearByPlacesTypeList, String language) {
        mSpecificPlacesDetailsEntityList = placeDetailsDao.getNearByPlacesListByPlaceTypeAndNearByTypeList(placeType, nearByPlacesTypeList, language);
        return mSpecificPlacesDetailsEntityList;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (PlacesDetailsEntity placesDetailsEntity) {
        Observable.just(placesDetailsEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<PlacesDetailsEntity>() {
                    @Override
                    public void onNext(PlacesDetailsEntity placesDetailsEntity1) {
//                        Gson gson = new Gson();
//                        Log.d("PlaceDetailsEntity", "insert: "+ gson.toJson(placesDetailsEntity1));
                        Log.d("PlaceDetailsEntity", "insert: "+ placesDetailsEntity1.getCategoryType());
                        placeDetailsDao.insert(placesDetailsEntity1);
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
