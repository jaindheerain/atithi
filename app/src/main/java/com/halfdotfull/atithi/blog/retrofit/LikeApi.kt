package com.halfdotfull.atithi.blog.retrofit

import com.halfdotfull.atithi.blog.LikeResponse
import com.halfdotfull.atithi.blog.Model.BlogModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Created by nexflare on 20/03/18.
 */
public interface LikeApi{
    @Multipart
    @POST("AtithiWeb/play_returns/api.php")
    fun getLike(@Part("action")action: RequestBody, @Part("phone_no") phone_no: RequestBody,
                 @Part("user_id") user_id: RequestBody, @Part("token") token: RequestBody,
                 @Part("blog_id")blog_id: RequestBody): Call<LikeResponse>
}