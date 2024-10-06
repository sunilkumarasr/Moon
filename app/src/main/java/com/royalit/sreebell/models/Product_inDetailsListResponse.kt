package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class Product_inDetailsListResponse(


    @SerializedName("Status") val status : Boolean,
    @SerializedName("Message") val message : String,
    @SerializedName("Response") val response :Product_inDetailsResponse,
    @SerializedName("code") val code : Int
)