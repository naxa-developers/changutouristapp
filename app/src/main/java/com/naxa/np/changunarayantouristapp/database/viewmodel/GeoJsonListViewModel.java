package com.naxa.np.changunarayantouristapp.database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.naxa.np.changunarayantouristapp.database.databaserepository.GeoJsonListEntityRepository;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonListEntity;

import java.util.List;


public class GeoJsonListViewModel extends AndroidViewModel {
    private GeoJsonListEntityRepository mRepository;

    private LiveData<List<GeoJsonListEntity>> mAllGeoJsonListEntity;
    private LiveData<GeoJsonListEntity> mSpecifiedGeoJsonEntity;

    public GeoJsonListViewModel(Application application) {
        super(application);
        mRepository = new GeoJsonListEntityRepository(application);

        mAllGeoJsonListEntity = mRepository.getAllGeoJsonListEntity();
    }

    public LiveData<List<GeoJsonListEntity>> getAllGeoJsonCategoryEntity() {
        return mAllGeoJsonListEntity;
    }

    public LiveData<GeoJsonListEntity> getmSpecificGeoJsonEntity(String category_table) {
        mSpecifiedGeoJsonEntity = mRepository.getSpecificGeoJsonEntity(category_table);
        return mSpecifiedGeoJsonEntity;
    }

    public void insert(GeoJsonListEntity geoJsonListEntity) {
        mRepository.insert(geoJsonListEntity);

    }
}
