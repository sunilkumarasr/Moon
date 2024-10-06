package com.royalit.sreebell.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class BaseViewModel(application: Application?) : AndroidViewModel(application!!) {
    var observerEvents: MutableLiveData<Constants.ObserverEvents> =
        MutableLiveData<Constants.ObserverEvents>()
}

