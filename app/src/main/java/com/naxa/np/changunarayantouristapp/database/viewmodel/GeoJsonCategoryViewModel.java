package com.naxa.np.changunarayantouristapp.database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.naxa.np.changunarayantouristapp.database.databaserepository.GeoJsonCategoryRepository;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonListDetailsEntity;

import java.util.List;

import io.reactivex.Maybe;

public class GeoJsonCategoryViewModel extends AndroidViewModel {
    private GeoJsonCategoryRepository mRepository;

    private LiveData<List<GeoJsonListDetailsEntity>> mAllGeoJsonCategoryEntity;
    private Maybe<List<GeoJsonListDetailsEntity>> mSpecificTypeGeoJsonCategoryEntity;

    public GeoJsonCategoryViewModel(Application application) {
        super(application);
        mRepository = new GeoJsonCategoryRepository(application);

        mAllGeoJsonCategoryEntity = mRepository.getAllGeoJsonCategoryEntity();
    }
    
    public LiveData<List<GeoJsonListDetailsEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonCategoryEntity; }

    public Maybe<List<GeoJsonListDetailsEntity>> getAllGeoJsonCategoryEntityByType(String category_type) {
        mSpecificTypeGeoJsonCategoryEntity = mRepository.getSpecificTypeListGeoJsonCategoryEntity(category_type);
        return mSpecificTypeGeoJsonCategoryEntity;
    }

    public void insert(GeoJsonListDetailsEntity geoJsonListDetailsEntity) {
        mRepository.insert(geoJsonListDetailsEntity);

    }
}
