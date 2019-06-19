package com.naxa.np.changunarayantouristapp.selectlanguage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LanguageDetails {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("alias")
    @Expose
    private String alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


//    public static List<LanguageDetails> getLanguageDetails(){
//        List<LanguageDetails> languageDetails = new ArrayList<>();
//
//        languageDetails.add(new LanguageDetails("English", "en"));
//        languageDetails.add(new LanguageDetails("Nepali (नेपाली)", "np"));
//        languageDetails.add(new LanguageDetails("Chinese (中文)", "ch"));
//        return  languageDetails;
//    }

}
