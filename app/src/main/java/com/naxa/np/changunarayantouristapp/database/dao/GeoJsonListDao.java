package com.naxa.np.changunarayantouristapp.database.dao;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonListEntity;

import java.util.List;


@Dao
public interface GeoJsonListDao {

    @Query("SELECT * from GeoJsonListEntity ORDER BY id ASC")
    LiveData<List<GeoJsonListEntity>> getGeoJsonListEntity();

    @Query("SELECT * from GeoJsonListEntity WHERE category_table =:category_table")
    LiveData<GeoJsonListEntity> getGeoJsonListByTableName(String category_table);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GeoJsonListEntity geoJsonListEntity);

    @Query("DELETE FROM GeoJsonListEntity")
    void deleteAll();
}
