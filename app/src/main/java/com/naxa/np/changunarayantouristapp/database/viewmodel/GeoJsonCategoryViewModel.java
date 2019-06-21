package com.naxa.np.changunarayantouristapp.database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.naxa.np.changunarayantouristapp.database.databaserepository.GeoJsonCategoryRepository;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class GeoJsonCategoryViewModel extends AndroidViewModel {
    private GeoJsonCategoryRepository mRepository;

    private Flowable<List<GeoJsonCategoryListEntity>> mAllGeoJsonCategoryEntity;
    private Maybe<List<GeoJsonCategoryListEntity>> mSpecificTypeGeoJsonCategoryEntity;

    public GeoJsonCategoryViewModel(Application application) {
        super(application);
        mRepository = new GeoJsonCategoryRepository(application);

        mAllGeoJsonCategoryEntity = mRepository.getAllGeoJsonCategoryEntity();
    }
    
    public Flowable<List<GeoJsonCategoryListEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonCategoryEntity; }


    public void insert(GeoJsonCategoryListEntity geoJsonCategoryListEntity) {
        mRepository.insert(geoJsonCategoryListEntity);

    }
}
