package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class LoginList_Response(

    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: Login_Response,
    @SerializedName("code") val code: Int
)