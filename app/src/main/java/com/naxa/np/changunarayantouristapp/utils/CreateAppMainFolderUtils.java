package com.naxa.np.changunarayantouristapp.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CreateAppMainFolderUtils {
    public static final String appmainFolderName = "Changunarayan Tourist App";
    Context context;
    private String mainFolderName ;
    private   final String databaseFolderName  = "data" ;
    private   final String mediaFolderName = ".media" ;
    private  final String mapFolderName = ".mapdata" ;

    public  static final String databaseFolderNameStatic  = "data" ;
    public  static final String mediaFolderNameStatic = ".media" ;
    public static final String mapFolderNameStatic = ".mapdata" ;

    public CreateAppMainFolderUtils(Context context, String mainFolderName) {
        this.context = context;
        this.mainFolderName = mainFolderName;
//        createMainFolder();
    }


    public void createMainFolder (){
        String myfolderLoc=Environment.getExternalStorageDirectory()+"/"+mainFolderName;
        File f=new File(myfolderLoc);
        if(!f.exists()) {
            if (!f.mkdir()) {
                Toast.makeText(context, myfolderLoc + " folder can't be created.", Toast.LENGTH_SHORT).show();

            } else {
                createDatabaseFolder();
                createMediaFolder();
                createMapFolder();
                Toast.makeText(context, myfolderLoc + " folder has been created.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getAppmainFolderName(){
     return Environment.getExternalStorageDirectory()+"/"+appmainFolderName;
    }

    @NotNull
    public static String getAppDataFolderName(){
        return Environment.getExternalStorageDirectory()+"/"+appmainFolderName+"/"+databaseFolderNameStatic;
    }

    @NotNull
    public static String getAppMediaFolderName(){
        return Environment.getExternalStorageDirectory()+"/"+appmainFolderName+"/"+mediaFolderNameStatic;
    }

    @NotNull
    public static String getAppMapDataFolderName(){
        return Environment.getExternalStorageDirectory()+"/"+appmainFolderName+"/"+mapFolderNameStatic;
    }

    public void createDatabaseFolder(){
        String myfolderLoc=Environment.getExternalStorageDirectory()+"/"+mainFolderName+"/"+databaseFolderName;

        File f=new File(myfolderLoc);
        if(!f.exists())
            if(!f.mkdir()){
                Toast.makeText(context, myfolderLoc+" folder can't be created.", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(context, myfolderLoc+" folder has been created.", Toast.LENGTH_SHORT).show();
            }
    }

    public void createMediaFolder(){
        String myfolderLoc=Environment.getExternalStorageDirectory()+"/"+mainFolderName+"/"+mediaFolderName;

        File f=new File(myfolderLoc);
        if(!f.exists())
            if(!f.mkdir()){
                Toast.makeText(context, myfolderLoc+" folder can't be created.", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(context, myfolderLoc+" folder has been created.", Toast.LENGTH_SHORT).show();
            }
    }

    public void createMapFolder(){
        String myfolderLoc=Environment.getExternalStorageDirectory()+"/"+mainFolderName+"/"+mapFolderName;

        File f=new File(myfolderLoc);
        if(!f.exists())
            if(!f.mkdir()){
                Toast.makeText(context, myfolderLoc+" folder can't be created.", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(context, myfolderLoc+" folder has been created.", Toast.LENGTH_SHORT).show();
            }
    }


    public boolean isFolderExist(String folderName){
        boolean isExist;

        File f = new File(Environment.getExternalStorageDirectory() + "/"+appmainFolderName+"/"+folderName);
        if(f.isDirectory()) {
            isExist = true;
        }else {
            isExist = false;
        }
        return isExist;
    }
}
