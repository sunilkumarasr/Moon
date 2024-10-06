package com.royalit.sreebell.ui.payment_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentDetailsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Help"
    }
    val text: LiveData<String> = _text
}