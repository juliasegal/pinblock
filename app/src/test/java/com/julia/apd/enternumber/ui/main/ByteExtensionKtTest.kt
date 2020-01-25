package com.julia.apd.enternumber.ui.main

import org.junit.Test

import org.junit.Assert.*

class ByteExtensionKtTest {

    @Test
    fun setHiNibbleValue() {
        val three = 0x03
        val hiThree = 0x30
        assertTrue(hiThree.toByte() == three.toByte().setHiNibbleValue())

        val eFeF = 0xFF
        val hiEf = 0xF0
        assertTrue(hiEf.toByte() == eFeF.toByte().setHiNibbleValue())

        val a = 0x78
        val hIa = 0x80
        assertTrue(hIa.toByte() == a.toByte().setHiNibbleValue())
    }

    @Test
    fun setLowNibbleValue() {
        val four = 0x34
        val lowFour = 0x04
        assertTrue(lowFour.toByte() == four.toByte().setLowNibbleValue())

        val ef = 0x0f
        val eFeF = 0xFF
        assertTrue(ef.toByte() == eFeF.toByte().setLowNibbleValue())

        val a = 0x78
        val b = 0x08
        assertTrue(b.toByte() == a.toByte().setLowNibbleValue())
    }

    @Test
    fun getHiNibbleValue() {
        val three = 0x34
        assertTrue(3.toByte() == three.toByte().getHiNibbleValue())

        val eFa = 0xFA
        assertTrue(0x0F.toByte() == eFa.toByte().getHiNibbleValue())

        val a = 0x78
        assertTrue(0x07.toByte() == a.toByte().getHiNibbleValue())
    }

    @Test
    fun getLowNibbleValue() {
        val four = 0x34
        assertTrue(4.toByte() == four.toByte().getLowNibbleValue())

        val eFeF = 0xFA
        assertTrue(0x0A.toByte() == eFeF.toByte().getLowNibbleValue())

        val a = 0x78
        assertTrue(0x08.toByte() == a.toByte().getLowNibbleValue())
    }

    @Test
    fun nibblesToString() {
        val num = ByteArray(4)
        num[0] = 0x70
        num[1] = 0x3F
        num[2] = 0x1A
        num[3] = 0x83.toByte()

        val expected = "703F1A83"
        val st = num.nibsToString()
        assertTrue(expected == st)
    }
}