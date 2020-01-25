package com.julia.apd.enternumber.ui.main

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertFailsWith


class EncodePinBlockTest {

    @Test
    fun encodePinBlock() {
        val encoder = EncodePinBlock()
        val pin = "1234"
        val block = encoder.encodePinBlock(pin).nibsToString()
        val expected = "341216123412341234"
        assertTrue(expected.take(1 + pin.length) == block.take(1 + pin.length))
    }

    @Test
    fun encodeLongPinBlock() {
        val encoder = EncodePinBlock()
        val pin = "123456789"
        val block = encoder.encodePinBlock(pin).nibsToString()
        val expected = "391216744BA"
        assertTrue(expected.take(1 + pin.length) == block.take(1 + pin.length))
    }

    @Test
    fun encodeTooLongPinBlock() {
        assertFailsWith<EncodePinBlock.InvalidPinException> {
            val encoder = EncodePinBlock()
            val pin = "123456789101112"
            encoder.encodePinBlock(pin)
        }
    }

    @Test
    fun encodeTooShortPinBlock() {
        assertFailsWith<EncodePinBlock.InvalidPinException> {
            val encoder = EncodePinBlock()
            val pin = "12"
            encoder.encodePinBlock(pin)
        }
    }

    @Test
    fun encodeBadPinBlock() {
        assertFailsWith<EncodePinBlock.InvalidPinException> {
            val encoder = EncodePinBlock()
            val pin = "Hello!"
            encoder.encodePinBlock(pin)
        }
    }
}