package com.halfdotfull.atithi.marketplace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.halfdotfull.atithi.R;
import com.halfdotfull.atithi.RetrofitSingelton;
import com.halfdotfull.atithi.Shop;
import com.halfdotfull.atithi.ShopModel;
import com.halfdotfull.atithi.login.retrofit.LoginApi;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopList extends AppCompatActivity {
    RecyclerView recyclerView;
    LoginApi api;
    public static ArrayList<Shop> shopModelArrayList;
    ShopsAdapter shopsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        recyclerView=findViewById(R.id.recyclerShopList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        api= RetrofitSingelton.createService(LoginApi.class);
        shopModelArrayList=new ArrayList<>();
        shopModelArrayList.add(new Shop("1","A198-98TV-90B1",
                "RoopSons", "A shop that provide cheap ghagra and lehenga",
                "Delhi"));
        shopModelArrayList.add(new Shop("2","D120-D1FA-A0BL","Jindals",
                "Find best designer handmade pots in the city,","Noida"));
        shopsAdapter=new ShopsAdapter(ShopList.this,shopModelArrayList);
        recyclerView.setAdapter(shopsAdapter);

        RequestBody get_shops = RequestBody.create(MediaType.parse("text/plain"), "get_shops");
        api.getAllShops(get_shops).enqueue(new Callback<ShopModel>() {
            @Override
            public void onResponse(Call<ShopModel> call, Response<ShopModel> response) {
                if (response.isSuccessful()){
                    Log.d("TAGGER",String.valueOf(response.body()));
                    shopModelArrayList.addAll(response.body().getShops());

                    shopsAdapter.updateArrayList(shopModelArrayList);
                }
            }

            @Override
            public void onFailure(Call<ShopModel> call, Throwable t) {

            }
        });
    }
}
