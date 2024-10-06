package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class ProductImage(

    @SerializedName("products_id") val products_id : Int,
    @SerializedName("id") val id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("fullPath") val fullPath : String,
)
