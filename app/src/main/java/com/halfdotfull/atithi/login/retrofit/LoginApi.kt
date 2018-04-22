package com.halfdotfull.atithi.login.retrofit

import com.halfdotfull.atithi.ShopModel
import com.halfdotfull.atithi.login.model.loginResponse

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Created by nexflare on 17/03/18.
 */

interface LoginApi {
    @Multipart
    @POST("AtithiWeb/play_returns/api.php/")
    fun getLoginResponse(@Part("action") action: RequestBody, @Part("phone_no") phone_no: RequestBody, @Part("user_id") user_id: RequestBody): Call<loginResponse>

    @Multipart
    @POST("AtithiWeb/play_returns/api.php")
    fun getAllShops(@Part("action") action: RequestBody): Call<ShopModel>
}
