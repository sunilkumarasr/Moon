package com.erepairs.app.models

import com.google.gson.annotations.SerializedName

data class Districts_ListResponse(

    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<Districts_Response>,
    @SerializedName("code") val code: Int
)