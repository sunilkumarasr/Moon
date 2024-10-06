package com.royalit.sreebell.models

import com.erepairs.app.models.Search_Response
import com.google.gson.annotations.SerializedName

data class Search_ListResponse(

    @SerializedName("Status") val status : Boolean,
    @SerializedName("Message") val message : String,
    @SerializedName("Response") val response : List<Search_Response>,
    @SerializedName("code") val code : Int
)