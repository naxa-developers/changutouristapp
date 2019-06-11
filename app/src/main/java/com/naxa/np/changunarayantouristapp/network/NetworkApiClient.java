package com.naxa.np.changunarayantouristapp.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.github.simonpercic.oklog3.BuildConfig;
import com.github.simonpercic.oklog3.OkLogInterceptor;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getAPIClient() {

        if (retrofit == null) {
            OkLogInterceptor okLogInterceptor = OkLogInterceptor.builder().build();
            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new StethoInterceptor());

            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(3);

            okHttpBuilder.dispatcher(dispatcher);
            okHttpBuilder.addInterceptor(okLogInterceptor);

            OkHttpClient okHttpClient = okHttpBuilder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(UrlConstant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;

    }




    public static Map<String, String> getHeaders(String token) {
        Map<String, String> map = new HashMap<>();

        if (BuildConfig.DEBUG) {
            map.put("Content-Type", "application/json");
            map.put("charset", "utf-8");
            map.put("Authorization", "Bearer " + token);
        } else {
            map.put("Content-Type", "application/json");
            map.put("charset", "utf-8");
            map.put("Authorization", "Bearer " + token);
        }

        return map;
    }


}
