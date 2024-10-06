package com.erepairs.app.models

import com.google.gson.annotations.SerializedName

data class Common_Response(

    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: String,
    @SerializedName("code") val code: Int
)