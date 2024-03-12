package com.example.jobscan.ui.connections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConnectionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Connections Fragment"
    }
    val text: LiveData<String> = _text
}