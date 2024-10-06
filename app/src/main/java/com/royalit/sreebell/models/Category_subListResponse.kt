package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class Category_subListResponse(

    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<Category_subResponse>,
    @SerializedName("code") val code: Int
)

data class NotificationListResponse(

    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<NotificationModel>,
    @SerializedName("code") val code: Int
)