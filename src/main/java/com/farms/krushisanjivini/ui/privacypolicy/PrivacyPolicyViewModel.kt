package com.farms.krushisanjivini.ui.privacypolicy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PrivacyPolicyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
       // value = "This is About us Fragment"
    }
    val text: LiveData<String> = _text
}