package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class SignupList_Response(

    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: Signup_Response,
    @SerializedName("code") val code: Int
)