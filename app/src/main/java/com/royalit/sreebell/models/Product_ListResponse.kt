package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class Product_ListResponse(

    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<Product_Response>,
    @SerializedName("code") val code: Int
)