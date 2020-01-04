package com.example.realm_sample_app

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmResults

class ViewModel : ViewModel() {
    val memo = MutableLiveData<String>()
    val list = MutableLiveData<RealmResults<ListObject>>()
    val newMemo = MutableLiveData<String>()
    val deleteId = MutableLiveData<Int>()

    fun clearMemo() {
        memo.value = ""
    }

    enum class KEY {
        REALM_ID
    }
}