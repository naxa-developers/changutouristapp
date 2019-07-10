package com.naxa.np.changunarayantouristapp.database.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;


import com.naxa.np.changunarayantouristapp.database.databaserepository.PlaceDetailsEntityRepository;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class PlaceDetailsEntityViewModel extends AndroidViewModel {
    private PlaceDetailsEntityRepository mRepository;

    private Flowable<List<PlacesDetailsEntity>> mAllEntityList;
    private Flowable<List<PlacesDetailsEntity>> mAllSpecificEntityList;
    private Single<PlacesDetailsEntity> mSpecificEntity;

    public PlaceDetailsEntityViewModel(Application application) {
        super(application);
        mRepository = new PlaceDetailsEntityRepository(application);

        mAllEntityList = mRepository.getAllPlacesDetailsEntity();
    }

    public Flowable<List<PlacesDetailsEntity>> getAllPlacesDetailsEntity() {
        return mAllEntityList;
    }

    public Flowable<List<PlacesDetailsEntity>> getPlacesDetailsEntityBYPlaceAndCategoryType(String placeType, String categoryType, String language) {
        mAllSpecificEntityList = mRepository.getPlacesDetailsEntityBYPlaceAndCategoryType(placeType, categoryType,language);
        return mAllSpecificEntityList;
    }

    public Single<PlacesDetailsEntity> getPlacesDetailsEntityBYQRCode(String qrCode, String language) {
        mSpecificEntity = mRepository.getPlacesDetailsEntityBYQRCode(qrCode, language);
        return mSpecificEntity;
    }

    public Flowable<List<PlacesDetailsEntity>> getNearByPlacesListByPlaceTypeAndNearByTypeList(String placeType, List<String> nearByPlacesTypeList, String language) {
        mAllSpecificEntityList = mRepository.getNearByPlacesListByPlaceTypeAndNearByTypeList(placeType, nearByPlacesTypeList, language);
        return mAllSpecificEntityList;
    }

    public void insert(PlacesDetailsEntity placesDetailsEntity) {
        mRepository.insert(placesDetailsEntity);

    }
}
