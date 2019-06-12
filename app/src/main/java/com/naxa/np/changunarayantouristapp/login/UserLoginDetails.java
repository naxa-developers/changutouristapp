package com.naxa.np.changunarayantouristapp.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginDetails {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("purpose_of_visit")
    @Expose
    private String purpose_of_visit;

    public UserLoginDetails(String name, String email, String country, String gender, String purpose_of_visit) {
        this.name = name;
        this.email = email;
        this.country = country;
        this.gender = gender;
        this.purpose_of_visit = purpose_of_visit;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPurpose_of_visit() {
        return purpose_of_visit;
    }

    public void setPurpose_of_visit(String purpose_of_visit) {
        this.purpose_of_visit = purpose_of_visit;
    }
}
