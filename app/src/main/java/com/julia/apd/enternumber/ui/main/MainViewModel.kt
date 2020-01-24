package com.julia.apd.enternumber.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julia.apd.enternumber.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.experimental.xor


class MainViewModel : ViewModel() {
    private val _pinBlockEntry = MutableLiveData<String>()
    private val _errorStringRes = MutableLiveData<Int>()

    val pinBlockEntry: LiveData<String> = _pinBlockEntry
    val errorStringRes: LiveData<Int> = _errorStringRes
    val progress = MutableLiveData<Boolean>()

    fun computeBlock(pin: String) {
        if (pin.length < 4 || pin.length > 12) {
            _errorStringRes.value = R.string.error_pin_wrong_size
        } else {
            progress.value = true
            viewModelScope.launch {
                _pinBlockEntry.value = withContext(Dispatchers.IO) { encodePinBlock(pin) }
            }
            progress.value = false
        }
    }

    private fun encodePinBlock(pin: String): String {
        val preppedPIN = preparePIN(pin)
        val preppedPAN = preparePAN(PAN_TEST)
        val block = getBlock(preppedPIN, preppedPAN)
        return convertToString(block)
    }

    private fun preparePIN(pin: String): ByteArray {
        val preparedPIN = ByteArray(CODE_LEN)

        preparedPIN[0] = ISO_CODE_3.toByte()
        preparedPIN[1] = pin.length.toByte()

        pin.mapIndexed { index, c -> preparedPIN[index + 2] = Character.getNumericValue(c).toByte() }

        val randomIndex = pin.length + 2
        for (i in randomIndex..15) {
            preparedPIN[i] = (0..9).random().toByte()
        }
        return preparedPIN
    }

    private fun preparePAN(pan: String): ByteArray {
        val preparedPAN = ByteArray(CODE_LEN)

        for (i in 0..3) {
            preparedPAN[i] = 0
        }

        val shortenPAN = pan.takeLast(12)
        shortenPAN.mapIndexed { index, c -> preparedPAN[index + 4] = Character.getNumericValue(c).toByte() }

        return preparedPAN
    }

    private fun getBlock(pin: ByteArray, pan: ByteArray) : ByteArray {
        val block = ByteArray(CODE_LEN)
        pin.mapIndexed { index, b -> block[index] = (b xor (pan[index])) }
        return block
    }

    private fun convertToString(byteArray: ByteArray): String {
        val retString = StringBuilder()
        byteArray.map { retString.append(String.format("%x", it)) }
        return retString.toString()
    }

    private fun setHiNibbleValue(value: Byte): Byte = (0xF0 and (value.toInt() shl 4)).toByte()

    private fun setLowNibbleValue(value: Byte): Byte = (0x0F and value.toInt()).toByte()

    companion object {
        const val ISO_CODE_3 = 3
        const val PAN = "1111222233334444"
        const val PAN_TEST = "4321987654321098"
        const val CODE_LEN = 16
    }
}
