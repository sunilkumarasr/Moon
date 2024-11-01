package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class PaymentDetailsSubmit_Response(

    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: PaymentDetailsSubmitResponse,
    @SerializedName("code") val code: Int
)