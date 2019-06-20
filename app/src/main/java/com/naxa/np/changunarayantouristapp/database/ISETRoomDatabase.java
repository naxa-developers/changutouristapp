package com.naxa.np.changunarayantouristapp.database;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.naxa.np.changunarayantouristapp.database.dao.GeoJsonCategoryDao;
import com.naxa.np.changunarayantouristapp.database.dao.GeoJsonListDao;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonListEntity;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;

import java.io.File;


/**
 * Created by samir on 4/22/2018.
 */

@Database(entities = { GeoJsonCategoryListEntity.class, GeoJsonListEntity.class
}, version = 1, exportSchema = false)

public abstract class ISETRoomDatabase extends RoomDatabase {

    public abstract GeoJsonCategoryDao geoJsonCategoryDao();
    public abstract GeoJsonListDao geoJsonListDao();


    private static ISETRoomDatabase INSTANCE;

    public static ISETRoomDatabase getDatabase(final Context context) {
        CreateAppMainFolderUtils createAppMainFolderUtils = new CreateAppMainFolderUtils(context, CreateAppMainFolderUtils.appmainFolderName);
        if (INSTANCE == null) {
            synchronized (ISETRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ISETRoomDatabase.class, createAppMainFolderUtils.getAppDataFolderName()+File.separator+"changunarayan_tourist_app")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
        }
    };



}
