package com.naxa.np.changunarayantouristapp.network;

import com.naxa.np.changunarayantouristapp.login.UserLoginResponse;
import com.naxa.np.changunarayantouristapp.map.mapcategory.GeojsonCategoriesListResponse;
import com.naxa.np.changunarayantouristapp.mayormessage.MayorMessagesListResponse;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlaceListDetailsResponse;
import com.naxa.np.changunarayantouristapp.placedetailsview.ratingplace.PlaceRatingResponse;
import com.naxa.np.changunarayantouristapp.selectlanguage.LanguageDetailsResponse;
import com.naxa.np.changunarayantouristapp.touristinformationguide.TouristInformationGuideListResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
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


    @POST(UrlConstant.POST_USER_Verification_URL)
    @FormUrlEncoded
    Observable<UserLoginResponse> getUserVerificationResponse(@Field("api_key") String api_key, @Field("data") String jsonData);

    @POST(UrlConstant.FETCH_GEOJSON_CATEGORIES_LIST)
    @FormUrlEncoded
    Observable<GeojsonCategoriesListResponse> getGeoJsonCategoriesListResponse(@Field("api_key") String key, @Field("language") String language);


    @POST(UrlConstant.FETCH_GEOJSON_FILES)
    @FormUrlEncoded
    Observable<ResponseBody> getGeoJsonDetails(@Field("api_key") String api_key, @Field("cat_table") String catTable);


    @POST(UrlConstant.FETCH_PLACE_DETAILS)
    @FormUrlEncoded
    Observable<MainPlaceListDetailsResponse> getMainPlacesListDetails(@Field("api_key") String api_key, @Field("language") String language);

    @POST(UrlConstant.FETCH_MAYOR_MESSAGE_DETAILS)
    @FormUrlEncoded
    Observable<MayorMessagesListResponse> getMaoyorMessagesListDetails(@Field("api_key") String api_key);

    @POST(UrlConstant.FETCH_TOURIST_INFORMATION_GUIDE_DETAILS)
    @FormUrlEncoded
    Observable<TouristInformationGuideListResponse> getTouristInformationGuideListResponse(@Field("api_key") String api_key, @Field("language") String language);

    @POST(UrlConstant.POST_PLACE_RATING)
    @FormUrlEncoded
    Observable<PlaceRatingResponse> postPlaceStarRating(@Field("api_key") String api_key, @Field("id") String placeId, @Field("rating") String rating, @Field("category_table") String category_type);
}
