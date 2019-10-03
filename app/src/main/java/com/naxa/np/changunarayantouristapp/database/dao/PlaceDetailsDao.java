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


    @Query("SELECT * from PlacesDetailsEntity WHERE place_type LIKE :placeType AND category_type LIKE :categoryType AND language LIKE :language")
    Flowable<List<PlacesDetailsEntity>> getPlacesDetailsEntityBYPlaceAndCategoryType(String placeType, String categoryType, String language);


    @Query("SELECT * from PlacesDetailsEntity WHERE place_type LIKE :placeType AND category_type IN (:nearbyTypeList) AND language LIKE :language")
    Flowable<List<PlacesDetailsEntity>> getNearByPlacesListByPlaceTypeAndNearByTypeList(String placeType, List<String> nearbyTypeList,  String language);

    @Query("SELECT * from PlacesDetailsEntity WHERE qr_code  LIKE '%' || :qrcODE || '%' AND language LIKE :language")
    Single<PlacesDetailsEntity> getPlacesDetailsEntityBYQRCode(String qrcODE,  String language);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlacesDetailsEntity placesDetailsEntity);

    @Query("DELETE FROM PlacesDetailsEntity")
    void deleteAll();
}
