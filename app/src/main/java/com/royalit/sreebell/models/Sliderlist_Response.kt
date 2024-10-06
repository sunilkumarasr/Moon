package com.royalit.sreebell.models

data class Sliderlist_Response(
    val Message: String,
    val Response: List<Slider_Response>,
    val Status: Boolean,
    val code: Int
)