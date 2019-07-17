package com.naxa.np.changunarayantouristapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;


@Dao
public interface GeoJsonCategoryDao {

    @Query("SELECT * from GeoJsonCategoryListEntity ORDER BY id ASC")
    Flowable<List<GeoJsonCategoryListEntity>> getGeoJsonCategoryList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GeoJsonCategoryListEntity geoJsonCategoryListEntity);


    @Query("SELECT * from GeoJsonCategoryListEntity WHERE language LIKE :language ORDER BY id ASC ")
    Maybe<List<GeoJsonCategoryListEntity>> getGeoJsonCategoryListByLanguage(String language);

    @Query("DELETE FROM GeoJsonCategoryListEntity")
    void deleteAll();
}
