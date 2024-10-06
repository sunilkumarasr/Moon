package com.erepairs.app.models

import com.google.gson.annotations.SerializedName

data class OrderHisProductDe_Response(

    @SerializedName("products_id") val products_id : String,
    @SerializedName("product_name") val product_name : String,
    @SerializedName("qty") val qty : String,
    @SerializedName("pqty") val pqty : String,
    @SerializedName("price") val price : String,
    @SerializedName("product_quintals") val product_quintals : String,
    @SerializedName("product_bags") val product_bags : String,
    @SerializedName("image") val image : String
)