package com.naxa.np.changunarayantouristapp.database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.naxa.np.changunarayantouristapp.database.databaserepository.PlaceDetailsEntityRepository;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

import java.util.List;

import io.reactivex.Flowable;

public class PlaceDetailsEntityViewModel extends AndroidViewModel {
    private PlaceDetailsEntityRepository mRepository;

    private Flowable<List<PlacesDetailsEntity>> mAllEntityList;
    private Flowable<List<PlacesDetailsEntity>> mAllSpecificEntityList;

    public PlaceDetailsEntityViewModel(Application application) {
        super(application);
        mRepository = new PlaceDetailsEntityRepository(application);

        mAllEntityList = mRepository.getAllPlacesDetailsEntity();
    }

    public Flowable<List<PlacesDetailsEntity>> getAllPlacesDetailsEntity() {
        return mAllEntityList;
    }

    public Flowable<List<PlacesDetailsEntity>> getPlacesDetailsEntityBYPlaceAndCategoryType(String placeType, List<String> categoryType) {
        mAllSpecificEntityList = mRepository.getPlacesDetailsEntityBYPlaceAndCategoryType(placeType, categoryType);
        return mAllSpecificEntityList;
    }

    public void insert(PlacesDetailsEntity placesDetailsEntity) {
        mRepository.insert(placesDetailsEntity);

    }
}
