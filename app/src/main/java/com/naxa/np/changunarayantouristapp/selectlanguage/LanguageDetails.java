package com.naxa.np.changunarayantouristapp.selectlanguage;

import java.util.ArrayList;
import java.util.List;

public class LanguageDetails {

    String title;
    String languagekey;

    public LanguageDetails(String title, String languagekey) {
        this.title = title;
        this.languagekey = languagekey;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguagekey() {
        return languagekey;
    }

    public void setLanguagekey(String languagekey) {
        this.languagekey = languagekey;
    }


    public static List<LanguageDetails> getLanguageDetails(){
        List<LanguageDetails> languageDetails = new ArrayList<>();

        languageDetails.add(new LanguageDetails("English", "en"));
        languageDetails.add(new LanguageDetails("Nepali (नेपाली)", "np"));
        languageDetails.add(new LanguageDetails("Chinese (中文)", "ch"));
        return  languageDetails;
    }

}
