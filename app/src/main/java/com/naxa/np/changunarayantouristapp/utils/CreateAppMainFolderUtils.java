package com.naxa.np.changunarayantouristapp.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class CreateAppMainFolderUtils {
    public static final String appmainFolderName = "Changunarayan Tourist App";
    Context context;
    String mainFolderName ;
    public static final String databaseFolderName  = "data" ;
    public static final String mediaFolderName = "media" ;

    public CreateAppMainFolderUtils(Context context, String mainFolderName) {
        this.context = context;
        this.mainFolderName = mainFolderName;
    }


    public void createMainFolder (){
        String myfolderLoc=Environment.getExternalStorageDirectory()+"/"+mainFolderName;

        File f=new File(myfolderLoc);
        if(!f.exists())
            if(!f.mkdir()){
                Toast.makeText(context, myfolderLoc+" folder can't be created.", Toast.LENGTH_SHORT).show();

            }
            else{
            createDatabaseFolder();
            createMediaFolder();
                Toast.makeText(context, myfolderLoc+" folder has been created.", Toast.LENGTH_SHORT).show();
            }
    }

    public String getAppmainFolderName(){
     return Environment.getExternalStorageDirectory()+"/"+mainFolderName;
    }
    public String getAppDataFolderName(){
        return Environment.getExternalStorageDirectory()+"/"+mainFolderName+"/"+databaseFolderName;
    }

    public String getAppMediaFolderName(){
        return Environment.getExternalStorageDirectory()+"/"+mainFolderName+"/"+mediaFolderName;
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
