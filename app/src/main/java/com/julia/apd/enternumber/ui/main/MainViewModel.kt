package com.julia.apd.enternumber.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.apd.enternumber.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel : ViewModel() {
    private val blockEncoder by lazy { EncodePinBlock() }
    private val _pinBlockEntry = MutableLiveData<String>()
    private val _errorStringRes = MutableLiveData<Int>()

    val pinBlockEntry: LiveData<String> = _pinBlockEntry
    val errorStringRes: LiveData<Int> = _errorStringRes
    val progress = MutableLiveData<Boolean>()

    fun computeBlock(pin: String) {
        if (pin.length < EncodePinBlock.MIN_PIN_LENGTH || pin.length > EncodePinBlock.MAX_PIN_LENGTH) {
            _errorStringRes.value = R.string.error_pin_wrong_size
        } else {
            progress.value = true
            viewModelScope.launch {
                _pinBlockEntry.value = withContext(Dispatchers.IO) {
                    val block = blockEncoder.encodePinBlock(pin)
                    blockEncoder.toString(block)
                }
            }
            progress.value = false
        }
    }
}
