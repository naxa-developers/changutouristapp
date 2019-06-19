package com.naxa.np.changunarayantouristapp.network;

import com.naxa.np.changunarayantouristapp.login.UserLoginDetails;
import com.naxa.np.changunarayantouristapp.login.UserLoginResponse;
import com.naxa.np.changunarayantouristapp.selectlanguage.LanguageDetails;
import com.naxa.np.changunarayantouristapp.selectlanguage.LanguageDetailsResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NetworkApiInterface {
    @POST(UrlConstant.FETCH_LANGUAGE_LIST_URL)
    @FormUrlEncoded
    Observable<LanguageDetailsResponse> getLanguages(@Field("api_key") String key);

    @POST(UrlConstant.POST_USER_DETAILS_URL)
    @FormUrlEncoded
    Observable<UserLoginResponse> getUserregistrationResponse(@Field("api_key") String api_key, @Field("data") String jsonData);
}