package com.halfdotfull.atithi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by nexflare on 17/03/18.
 */

public class RetrofitSingelton {
    private static final Retrofit retrofit;
    private static final Retrofit retrofit1;
    static{
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.128:8888/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        retrofit1 = new Retrofit.Builder()
                .baseUrl("http://kingsmen.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();


    }
    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static <S> S createService1(Class<S> serviceClass) {
        return retrofit1.create(serviceClass);
    }
}
