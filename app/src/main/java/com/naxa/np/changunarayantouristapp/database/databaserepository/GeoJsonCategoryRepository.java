package com.naxa.np.changunarayantouristapp.database.databaserepository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.naxa.np.changunarayantouristapp.database.ISETRoomDatabase;
import com.naxa.np.changunarayantouristapp.database.dao.GeoJsonCategoryDao;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonListDetailsEntity;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GeoJsonCategoryRepository {

    private GeoJsonCategoryDao mGeoJsonCategoryDao;
    private LiveData<List<GeoJsonListDetailsEntity>> mAllGeoJsonCategoryEntity;
    private Maybe<List<GeoJsonListDetailsEntity>> mSpecificTypeGeoJsonCategoryEntity;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public GeoJsonCategoryRepository(Application application) {
        ISETRoomDatabase db = ISETRoomDatabase.getDatabase(application);
        mGeoJsonCategoryDao = db.geoJsonCategoryDao();
        mAllGeoJsonCategoryEntity = mGeoJsonCategoryDao.getGeoJsonCategoryList();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<GeoJsonListDetailsEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonCategoryEntity;
    }

    public Maybe<List<GeoJsonListDetailsEntity>> getSpecificTypeListGeoJsonCategoryEntity(String category_type) {
        mSpecificTypeGeoJsonCategoryEntity = mGeoJsonCategoryDao.getGeoJsonListByCategoryType(category_type);
        return mSpecificTypeGeoJsonCategoryEntity;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (GeoJsonListDetailsEntity geoJsonListDetailsEntity) {
        Observable.just(geoJsonListDetailsEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<GeoJsonListDetailsEntity>() {
            @Override
            public void onNext(GeoJsonListDetailsEntity geoJsonListDetailsEntity) {
                Log.d("GeoJsonListDetailsEntity", "insert: "+ geoJsonListDetailsEntity.getCategoryName());
                mGeoJsonCategoryDao.insert(geoJsonListDetailsEntity);
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
