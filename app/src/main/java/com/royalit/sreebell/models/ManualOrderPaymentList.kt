package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class ManualOrderPaymentList(


    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<ManualOrderPaymentResponse>,
    @SerializedName("code") val code: Int
)

