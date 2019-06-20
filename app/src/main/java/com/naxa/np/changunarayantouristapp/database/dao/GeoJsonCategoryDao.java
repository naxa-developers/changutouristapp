package com.naxa.np.changunarayantouristapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;

import java.util.List;

import io.reactivex.Maybe;


@Dao
public interface GeoJsonCategoryDao {

    @Query("SELECT * from GeoJsonCategoryListEntity ORDER BY id ASC")
    LiveData<List<GeoJsonCategoryListEntity>> getGeoJsonCategoryList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GeoJsonCategoryListEntity geoJsonCategoryListEntity);


    @Query("DELETE FROM GeoJsonCategoryListEntity")
    void deleteAll();
}
