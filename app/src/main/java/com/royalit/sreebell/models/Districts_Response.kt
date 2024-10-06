package com.erepairs.app.models

import com.google.gson.annotations.SerializedName

data class Districts_Response(

    @SerializedName("id") val id: Int,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("name") val name: String
)