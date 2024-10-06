package com.erepairs.app.models

import com.google.gson.annotations.SerializedName

data class Areas_Response(

    @SerializedName("id") val id: Int,
    @SerializedName("area_name") val area_name: String
)