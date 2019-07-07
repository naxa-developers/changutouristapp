package com.naxa.np.changunarayantouristapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface PlaceDetailsDao {


    @Query("SELECT * from PlacesDetailsEntity ORDER BY pid ASC")
    Flowable<List<PlacesDetailsEntity>> getAllPlacesDetailsEntity();


    @Query("SELECT * from PlacesDetailsEntity WHERE place_type LIKE :placeType AND category_type LIKE :categoryType")
    Flowable<List<PlacesDetailsEntity>> getPlacesDetailsEntityBYPlaceAndCategoryType(String placeType, String categoryType);


    @Query("SELECT * from PlacesDetailsEntity WHERE place_type LIKE :placeType AND type IN (:nearbyTypeList)")
    Flowable<List<PlacesDetailsEntity>> getNearByPlacesListByPlaceTypeAndNearByTypeList(String placeType, List<String> nearbyTypeList);

    @Query("SELECT * from PlacesDetailsEntity WHERE qr_code  LIKE '%' || :qrcODE || '%' ")
    Single<PlacesDetailsEntity> getPlacesDetailsEntityBYQRCode(String qrcODE);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlacesDetailsEntity placesDetailsEntity);

    @Query("DELETE FROM PlacesDetailsEntity")
    void deleteAll();
}
