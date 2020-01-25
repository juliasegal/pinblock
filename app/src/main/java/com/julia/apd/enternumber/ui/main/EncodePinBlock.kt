package com.julia.apd.enternumber.ui.main

import java.lang.Exception
import java.security.SecureRandom
import kotlin.experimental.xor

class InvalidPinExcpetion(message: String) : Exception(message)

class EncodePinBlock {

    companion object {
        const val MIN_PIN_LENGTH = 4
        const val MAX_PIN_LENGTH = 12

        private const val PAN = "1111222233334444"
        private const val CODE_LEN = 16
        private const val BLOCK_LEN = 8
    }

    fun encodePinBlock(pin: String): ByteArray {
        if (pin.length < MIN_PIN_LENGTH) {
            throw InvalidPinExcpetion("PIN length too short")
        } else if (pin.length > MAX_PIN_LENGTH) {
            throw InvalidPinExcpetion("PIN length too long")
        }
        if (!pin.contains(Regex("^[0-9]*$"))) {
            throw InvalidPinExcpetion("PIN contains illegal characters")
        }

        val preppedPIN = preparePIN(pin)
        val preppedPAN = preparePAN(PAN)
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

        val randomIndex = pin.length + codeStartIndex

        for (i in randomIndex until CODE_LEN) {
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
                    (pinItem xor pan[index]).setHiNibbleValue() xor block[blockIndex]
                else -> {
                    block[blockIndex] =
                        (pinItem xor pan[index]).setLowNibbleValue() xor block[blockIndex]
                    blockIndex++
                }
            }
        }
        return block
    }
}