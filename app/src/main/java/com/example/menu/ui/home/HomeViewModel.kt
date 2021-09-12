package com.example.menu.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.menu.data.SecurePreferences

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to Southern Cross Wellington Hospital!\nPlease scan your Patient Room QR to begin"
    }
    public var text: LiveData<String> = _text
}