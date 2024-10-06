package com.erepairs.app.models

import com.google.gson.annotations.SerializedName
import com.royalit.sreebell.models.Signup_Response

data class Apikey_Response(

    @SerializedName("api_key") val api_key: String
)

data class AppMaintananceResponseModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<AppModeResponseModel>,
    @SerializedName("code") val code: Int
)

data class AppModeResponseModel(
    @SerializedName("app_mode") val message: String,
    @SerializedName("reason") val reason: String?,

)