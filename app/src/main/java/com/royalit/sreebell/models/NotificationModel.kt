package com.royalit.sreebell.models

import com.google.gson.annotations.SerializedName

data class NotificationModel(
    @SerializedName("id") var id:String,
    @SerializedName("title")var title:String,
    @SerializedName("body")var body:String,
    @SerializedName("image")var image:String,
    ) {
}