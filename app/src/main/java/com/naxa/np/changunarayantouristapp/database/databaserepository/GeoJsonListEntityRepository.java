package com.naxa.np.changunarayantouristapp.database.databaserepository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.naxa.np.changunarayantouristapp.database.ISETRoomDatabase;
import com.naxa.np.changunarayantouristapp.database.dao.GeoJsonListDao;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonListEntity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class GeoJsonListEntityRepository {

    private GeoJsonListDao mGeoJsonListDao;
    private LiveData<List<GeoJsonListEntity>> mAllGeoJsonListEntity;
    private LiveData<GeoJsonListEntity> mSpecificGeoJsonEntity;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public GeoJsonListEntityRepository(Application application) {
        ISETRoomDatabase db = ISETRoomDatabase.getDatabase(application);
        mGeoJsonListDao = db.geoJsonListDao();
        mAllGeoJsonListEntity = mGeoJsonListDao.getGeoJsonListEntity();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<GeoJsonListEntity>> getAllGeoJsonListEntity() {
        return mAllGeoJsonListEntity;
    }

    public LiveData<GeoJsonListEntity> getSpecificGeoJsonEntity(String category_table) {
        mSpecificGeoJsonEntity = mGeoJsonListDao.getGeoJsonListByTableName(category_table);
        return mSpecificGeoJsonEntity;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (GeoJsonListEntity geoJsonListEntity) {
        Observable.just(geoJsonListEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<GeoJsonListEntity>() {
                    @Override
                    public void onNext(GeoJsonListEntity geoJsonListEntity) {
                        Log.d("GeoJsonListEntity", "insert: "+ geoJsonListEntity.getCategoryName());
                        mGeoJsonListDao.insert(geoJsonListEntity);
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
