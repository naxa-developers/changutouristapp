package com.naxa.np.changunarayantouristapp.database.databaserepository;

import android.app.Application;
import android.util.Log;

import com.naxa.np.changunarayantouristapp.database.ISETRoomDatabase;
import com.naxa.np.changunarayantouristapp.database.dao.GeoJsonCategoryDao;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GeoJsonCategoryRepository {

    private GeoJsonCategoryDao mGeoJsonCategoryDao;
    private Flowable<List<GeoJsonCategoryListEntity>> mAllGeoJsonCategoryEntity;
    private Flowable<List<GeoJsonCategoryListEntity>> mSpecificTypeGeoJsonCategoryEntity;
    private List<GeoJsonCategoryListEntity> mDistinctTypeGeoJsonCategoryEntity;
    private Flowable<List<GeoJsonCategoryListEntity>> mDistinctSlugList;

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
    public Flowable<List<GeoJsonCategoryListEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonCategoryEntity;
    }


    public Flowable<List<GeoJsonCategoryListEntity>> getAllGeoJsonCategoryEntityByLanguage(String language,  String slug) {
        mSpecificTypeGeoJsonCategoryEntity = mGeoJsonCategoryDao.getGeoJsonCategoryListByLanguage(language, slug);
        return mSpecificTypeGeoJsonCategoryEntity;
    }


    public Flowable<List<GeoJsonCategoryListEntity>> getGeoJsonSubCategorySlugByLanguage(String language) {
        mDistinctSlugList = mGeoJsonCategoryDao.getGeoJsonSubCategorySlugByLanguage(language);
        return mDistinctSlugList;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert (GeoJsonCategoryListEntity geoJsonCategoryListEntity) {
        Observable.just(geoJsonCategoryListEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableObserver<GeoJsonCategoryListEntity>() {
            @Override
            public void onNext(GeoJsonCategoryListEntity geoJsonCategoryListEntity) {
                Log.d("GeoJsonCategoryEntity", "insert: "+ geoJsonCategoryListEntity.getCategoryName());
                mGeoJsonCategoryDao.insert(geoJsonCategoryListEntity);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    public void insertAll (List<GeoJsonCategoryListEntity> geoJsonCategoryListEntity) {
        Observable.just(geoJsonCategoryListEntity)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(new Function<List<GeoJsonCategoryListEntity>, Iterable<GeoJsonCategoryListEntity>>() {
                    @Override
                    public Iterable<GeoJsonCategoryListEntity> apply(List<GeoJsonCategoryListEntity> geoJsonCategoryListEntities) throws Exception {
                        return geoJsonCategoryListEntities;
                    }
                })
                .map(new Function<GeoJsonCategoryListEntity, GeoJsonCategoryListEntity>() {
                    @Override
                    public GeoJsonCategoryListEntity apply(GeoJsonCategoryListEntity geoJsonCategoryListEntity) throws Exception {
                        return geoJsonCategoryListEntity;
                    }
                })
                .subscribe(new DisposableObserver<GeoJsonCategoryListEntity>() {
                    @Override
                    public void onNext(GeoJsonCategoryListEntity geoJsonCategoryListEntity) {
                        Log.d("GeoJsonCategoryEntity", "insert: "+ geoJsonCategoryListEntity.getCategoryName());
                        GeoJsonCategoryListEntity geoJsonCategoryListEntity1 = geoJsonCategoryListEntity;
                        geoJsonCategoryListEntity1.setCategoryTableLanguage(geoJsonCategoryListEntity.getCategoryTable()+geoJsonCategoryListEntity.getLanguage());
                        mGeoJsonCategoryDao.insert(geoJsonCategoryListEntity1);
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
