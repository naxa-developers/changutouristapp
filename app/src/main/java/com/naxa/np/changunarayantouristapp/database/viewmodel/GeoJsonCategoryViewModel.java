package com.naxa.np.changunarayantouristapp.database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;


import com.naxa.np.changunarayantouristapp.database.databaserepository.GeoJsonCategoryRepository;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class GeoJsonCategoryViewModel extends AndroidViewModel {
    private GeoJsonCategoryRepository mRepository;

    private Flowable<List<GeoJsonCategoryListEntity>> mAllGeoJsonCategoryEntity;
    private Flowable<List<GeoJsonCategoryListEntity>> mSpecificTypeGeoJsonCategoryEntity;
    private List<GeoJsonCategoryListEntity> mDistinctTypeGeoJsonCategoryEntity;
    private Flowable<List<GeoJsonCategoryListEntity>> mDistinctSlugList;

    public GeoJsonCategoryViewModel(Application application) {
        super(application);
        mRepository = new GeoJsonCategoryRepository(application);

        mAllGeoJsonCategoryEntity = mRepository.getAllGeoJsonCategoryEntity();
    }
    
    public Flowable<List<GeoJsonCategoryListEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonCategoryEntity; }

    public Flowable<List<GeoJsonCategoryListEntity>> getAllGeoJsonCategoryEntityByLanguage(String language, String slug) {
        mSpecificTypeGeoJsonCategoryEntity = mRepository.getAllGeoJsonCategoryEntityByLanguage(language, slug);
        return mSpecificTypeGeoJsonCategoryEntity;
    }


    public Flowable<List<GeoJsonCategoryListEntity>> getGeoJsonSubCategorySlugByLanguage(String language) {
        mDistinctSlugList = mRepository.getGeoJsonSubCategorySlugByLanguage(language);
        return mDistinctSlugList;
    }


    public void insert(GeoJsonCategoryListEntity geoJsonCategoryListEntity) {
        mRepository.insert(geoJsonCategoryListEntity);

    }
    public void insertAll(List<GeoJsonCategoryListEntity> geoJsonCategoryListEntity) {
        mRepository.insertAll(geoJsonCategoryListEntity);

    }
}
