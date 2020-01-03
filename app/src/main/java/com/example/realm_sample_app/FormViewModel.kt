package com.example.realm_sample_app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FormViewModel : ViewModel() {
    val memo = MutableLiveData<String>()
    val list = MutableLiveData<List<String>>()

    fun clearMemo() {
        memo.value = ""
    }
}