package com.naxa.np.changunarayantouristapp.placedetailsview;

import java.io.Serializable;

public class FileNameAndUrlPojo implements Serializable {
    String name;
    String fileUrl;


    public FileNameAndUrlPojo(String name, String fileUrl) {
        this.name = name;
        this.fileUrl = fileUrl;
    }

    public FileNameAndUrlPojo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
