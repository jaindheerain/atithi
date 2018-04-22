package com.halfdotfull.atithi.marketplace

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.halfdotfull.atithi.R
import com.halfdotfull.atithi.RetrofitSingelton
import com.halfdotfull.atithi.Shop
import com.halfdotfull.atithi.ShopModel
import com.halfdotfull.atithi.login.retrofit.LoginApi
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ShopList : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var api: LoginApi
    lateinit var shopsAdapter: ShopsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)
        recyclerView = findViewById(R.id.recyclerShopList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        api = RetrofitSingelton.createService(LoginApi::class.java)
        shopModelArrayList = ArrayList()
        shopModelArrayList.add(Shop("1", "A198-98TV-90B1",
                "RoopSons", "A shop that provide cheap ghagra and lehenga",
                "Delhi"))
        shopModelArrayList.add(Shop("2", "D120-D1FA-A0BL", "Jindals",
                "Find best designer handmade pots in the city,", "Noida"))
        shopsAdapter = ShopsAdapter(this@ShopList, shopModelArrayList)
        recyclerView.adapter = shopsAdapter

        val get_shops = RequestBody.create(MediaType.parse("text/plain"), "get_shops")
        api.getAllShops(get_shops).enqueue(object : Callback<ShopModel> {
            override fun onResponse(call: Call<ShopModel>, response: Response<ShopModel>) {
                if (response.isSuccessful) {
                    Log.d("TAGGER", response.body().toString())
                    shopModelArrayList.addAll(response.body()!!.shops)

                    shopsAdapter.updateArrayList(shopModelArrayList)
                }
            }

            override fun onFailure(call: Call<ShopModel>, t: Throwable) {

            }
        })
    }

    companion object {
        lateinit var shopModelArrayList: ArrayList<Shop>
    }
}
