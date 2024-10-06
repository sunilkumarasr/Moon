package com.royalit.sreebell.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category_Response(

    @SerializedName("categories_id") val categories_id : Int,
    @SerializedName("category_name") val category_name : String,
    @SerializedName("category_image") val category_image : String
) : Parcelable