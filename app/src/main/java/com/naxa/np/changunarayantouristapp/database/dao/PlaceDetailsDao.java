package com.naxa.np.changunarayantouristapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PlaceDetailsDao {


    @Query("SELECT * from PlacesDetailsEntity ORDER BY pid ASC")
    Flowable<List<PlacesDetailsEntity>> getAllPlacesDetailsEntity();

    @Query("SELECT * from PlacesDetailsEntity WHERE place_type LIKE :placeType AND category_type IN (:categoryType)")
    Flowable<List<PlacesDetailsEntity>> getPlacesDetailsEntityBYPlaceAndCategoryType(String placeType, List<String> categoryType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlacesDetailsEntity placesDetailsEntity);

    @Query("DELETE FROM PlacesDetailsEntity")
    void deleteAll();
}