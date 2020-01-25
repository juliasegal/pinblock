package com.julia.apd.enternumber.ui.main

import java.lang.StringBuilder
import java.util.*

fun Byte.setHiNibbleValue(): Byte = (0xF0 and (this.toInt() shl 4)).toByte()
fun Byte.setLowNibbleValue(): Byte = (0x0F and this.toInt()).toByte()
fun Byte.getHiNibbleValue(): Byte = ((0xF0 and this.toInt()) ushr 4).toByte()
fun Byte.getLowNibbleValue(): Byte = (0x0F and this.toInt()).toByte()

fun ByteArray.nibsToString(): String {
    val retString = StringBuilder()
    this.map {
        retString.append("%x".format(it.getHiNibbleValue()))
        retString.append("%x".format(it.getLowNibbleValue()))
    }
    return retString.toString().toUpperCase(Locale.ENGLISH)
}
