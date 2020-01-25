package com.julia.apd.enternumber.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val blockEncoder by lazy { EncodePinBlock() }
    private val _pinBlockEntry = MutableLiveData<String>()
    private val _errorStringRes = MutableLiveData<Int>()

    val pinBlockEntry: LiveData<String> = _pinBlockEntry
    val errorStringRes: LiveData<Int> = _errorStringRes
    val progress = MutableLiveData<Boolean>()

    fun computeBlock(pin: String) {
        progress.value = true
        viewModelScope.launch {
            try {
                val block = blockEncoder.encodePinBlock(pin)
                _pinBlockEntry.value = block.nibsToString()
                _errorStringRes.value = 0
            } catch (ex: EncodePinBlock.InvalidPinException) {
                _errorStringRes.value = ex.resourceId
            }
        }
        progress.value = false
    }
}
