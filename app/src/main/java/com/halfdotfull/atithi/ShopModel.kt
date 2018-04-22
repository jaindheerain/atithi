package com.halfdotfull.atithi

/**
 * Created by nexflare on 21/03/18.
 */

data class ShopModel(val shops:ArrayList<Shop>)

data class Shop(val shop_id:String,val bhamashah_id:String,val shop_name:String,val description:String,val locality:String)