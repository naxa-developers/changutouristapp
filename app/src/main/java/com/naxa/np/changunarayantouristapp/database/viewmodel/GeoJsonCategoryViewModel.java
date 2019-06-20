package com.naxa.np.changunarayantouristapp.database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.naxa.np.changunarayantouristapp.database.databaserepository.GeoJsonCategoryRepository;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;

import java.util.List;

import io.reactivex.Maybe;

public class GeoJsonCategoryViewModel extends AndroidViewModel {
    private GeoJsonCategoryRepository mRepository;

    private LiveData<List<GeoJsonCategoryListEntity>> mAllGeoJsonCategoryEntity;
    private Maybe<List<GeoJsonCategoryListEntity>> mSpecificTypeGeoJsonCategoryEntity;

    public GeoJsonCategoryViewModel(Application application) {
        super(application);
        mRepository = new GeoJsonCategoryRepository(application);

        mAllGeoJsonCategoryEntity = mRepository.getAllGeoJsonCategoryEntity();
    }
    
    public LiveData<List<GeoJsonCategoryListEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonCategoryEntity; }

    public Maybe<List<GeoJsonCategoryListEntity>> getAllGeoJsonCategoryEntityByType(String category_type) {
        mSpecificTypeGeoJsonCategoryEntity = mRepository.getSpecificTypeListGeoJsonCategoryEntity(category_type);
        return mSpecificTypeGeoJsonCategoryEntity;
    }

    public void insert(GeoJsonCategoryListEntity geoJsonCategoryListEntity) {
        mRepository.insert(geoJsonCategoryListEntity);

    }
}
