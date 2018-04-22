package com.halfdotfull.atithi.login.retrofit;

import com.halfdotfull.atithi.ShopModel;
import com.halfdotfull.atithi.login.model.loginResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by nexflare on 17/03/18.
 */

public interface LoginApi {
    @Multipart
    @POST("AtithiWeb/play_returns/api.php/")
    Call<loginResponse> getLoginResponse(@Part("action")RequestBody action, @Part("phone_no")RequestBody phone_no, @Part("user_id")RequestBody user_id);

    @Multipart
    @POST("AtithiWeb/play_returns/api.php")
    Call<ShopModel> getAllShops(@Part("action") RequestBody action);
}
