package com.julia.apd.enternumber.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julia.apd.enternumber.R

class MainViewModel : ViewModel() {
    private val _pinBlockEntry = MutableLiveData<String>()
    private val _errorStringRes = MutableLiveData<Int>()

    val pinBlockEntry : LiveData<String> = _pinBlockEntry
    val errorStringRes : LiveData<Int> = _errorStringRes

    fun computeBlock(pin: String) {
        if (pin.length < 4 || pin.length > 12) {
            _errorStringRes.value = R.string.error_pin_wrong_size
        }
        else {
            _pinBlockEntry.value = pin
        }
    }
}
