package com.julia.apd.enternumber.ui.main

import java.lang.Exception
import java.lang.StringBuilder
import java.security.SecureRandom
import java.util.*
import kotlin.experimental.xor

class PinSizeException(message: String) : Exception(message)

class EncodePinBlock {

    companion object {
        const val MIN_PIN_LENGTH = 4
        const val MAX_PIN_LENGTH = 12


        private const val PAN = "1111222233334444"
        private const val PAN_TEST = "4321987654321098"
        private const val CODE_LEN = 16
        private const val BLOCK_LEN = 8
    }

    fun encodePinBlock(pin: String): ByteArray {
        if (pin.length < MIN_PIN_LENGTH) {
            throw PinSizeException("Pin length too short")
        } else if (pin.length > MAX_PIN_LENGTH) {
            throw PinSizeException("Pin length too long")
        }

        val preppedPIN = preparePIN(pin)
        val preppedPAN = preparePAN(PAN_TEST)
        return getBlock(preppedPIN, preppedPAN)
    }

    private fun preparePIN(pin: String): ByteArray {
        val isoCode = 3
        val isoTypeIndex = 0
        val lenIndex = 1
        val codeStartIndex = 2

        val preparedPIN = ByteArray(CODE_LEN)
        preparedPIN[isoTypeIndex] = isoCode.toByte()
        preparedPIN[lenIndex] = pin.length.toByte()

        pin.mapIndexed { index, c ->
            preparedPIN[index + codeStartIndex] = Character.getNumericValue(c).toByte()
        }

        val randomIndex = pin.length + codeStartIndex - 1

        for (i in randomIndex..CODE_LEN - 1) {
            preparedPIN[i] = SecureRandom().nextInt(0xF).toByte()
        }
        return preparedPIN
    }

    private fun preparePAN(pan: String): ByteArray {
        val maxPanSize = 12
        val panStartIndex = 4
        val preparedPAN = ByteArray(CODE_LEN)
        val shortenPAN = pan.takeLast(maxPanSize)

        shortenPAN.mapIndexed { index, c ->
            preparedPAN[index + panStartIndex] = Character.getNumericValue(c).toByte()
        }
        return preparedPAN
    }

    private fun getBlock(pin: ByteArray, pan: ByteArray): ByteArray {
        val block = ByteArray(BLOCK_LEN)
        var blockIndex = 0
        pin.mapIndexed { index, pinItem ->
            when {
                index % 2 == 0 -> block[blockIndex] =
                    setHiNibbleValue(pinItem xor pan[index]) xor block[blockIndex]
                else -> {
                    block[blockIndex++] =
                        setLowNibbleValue(pinItem xor pan[index]) xor block[blockIndex]
                    blockIndex++
                }
            }
        }
        return block
    }

    fun toString(byteArray: ByteArray): String {
        val retString = StringBuilder()

        byteArray.map {
            retString.append("%x".format(getHiNibbleValue(it)))
            retString.append("%x".format(getLowNibbleValue(it)))
        }
        return retString.toString().toUpperCase(Locale.ENGLISH)
    }

    private fun setHiNibbleValue(value: Byte): Byte = (0xF0 and (value.toInt() shl 4)).toByte()

    private fun setLowNibbleValue(value: Byte): Byte = (0x0F and value.toInt()).toByte()

    private fun getHiNibbleValue(value: Byte): Byte = ((0xF0 and value.toInt()) ushr 4).toByte()

    private fun getLowNibbleValue(value: Byte): Byte = (0x0F and value.toInt()).toByte()
}