package com.halfdotfull.atithi.blog.retrofit

import com.halfdotfull.atithi.blog.Model.BlogModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Created by nexflare on 19/03/18.
 */
interface BlogApi {
    @Multipart
    @POST("AtithiWeb/play_returns/api.php")
    fun getBlogs(@Part("action")action:RequestBody, @Part("phone_no") phone_no:RequestBody,
                 @Part("user_id") user_id:RequestBody, @Part("token") token:RequestBody,
                 @Part("type")type:RequestBody,@Part("page") page:RequestBody): Call<BlogModel>

    @Multipart
    @POST("AtithiWeb/play_returns/api.php")
    fun getPlan(@Part("action")action:RequestBody,@Part("token") token:RequestBody,
                @Part("user_id")user_id: RequestBody,@Part("phone_no") phone_no: RequestBody,
                 @Part("hour")type:RequestBody,@Part("lat") lati:RequestBody,
                @Part("lon") loni:RequestBody,@Part("radius") radius:RequestBody,@Part("interests") interests:RequestBody): Call<ArrayList<String>>


}