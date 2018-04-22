package com.halfdotfull.atithi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by nexflare on 17/03/18.
 */

object RetrofitSingelton {
    private val retrofit: Retrofit
    private val retrofit1: Retrofit

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build()

        retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.43.128:8888/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        retrofit1 = Retrofit.Builder()
                .baseUrl("http://kingsmen.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()


    }

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    fun <S> createService1(serviceClass: Class<S>): S {
        return retrofit1.create(serviceClass)
    }
}
