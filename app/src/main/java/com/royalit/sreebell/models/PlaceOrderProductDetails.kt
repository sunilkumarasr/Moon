package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceOrderProductDetails(
    @SerializedName("products_id") val products_id: String,
    @SerializedName("product_name") val product_name: String,
    @SerializedName("qty") val qty: String,
    @SerializedName("pqty") val pqty: String,
    @SerializedName("price") val price: String,
    @SerializedName("image") val image: String,
    @SerializedName("tax_number") val tax_number: String,
    @SerializedName("item_name") val item_name: String,
    @SerializedName("batch_name") val batch_name: String,

): Serializable
