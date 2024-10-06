package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class PriceList(
    @SerializedName("id") val id: String,
    @SerializedName("product_id") val product_id: String,
    @SerializedName("district_id") val district_id: String,
    @SerializedName("distributor_price") val distributor_price: String,
    @SerializedName("general_trade_price") val general_trade_price: String,
)
