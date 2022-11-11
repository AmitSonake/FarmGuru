package com.farms.krushisanjivini.ui.executive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExecutiveViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
       // value = "This is Call us helpline Fragment"
    }
    val text: LiveData<String> = _text
}