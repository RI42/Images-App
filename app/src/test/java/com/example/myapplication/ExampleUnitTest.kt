package com.example.myapplication

import com.google.common.truth.Truth.assertThat
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val preloadSize = 5

    @Test
    fun getNewPos0() {
        val currPos = 0
        val positionStart = 0
        val itemCount = 1
        val expected = 0
        val newPos = getNewPos(currPos, positionStart, itemCount, preloadSize)
        assertThat(newPos).isEqualTo(expected)
    }

    @Test
    fun getNewPos1() {
        val currPos = 1
        val positionStart = 0
        val itemCount = 1
        val expected = 0
        val newPos = getNewPos(currPos, positionStart, itemCount, preloadSize)
        assertThat(newPos).isEqualTo(expected)
    }

    @Test
    fun getNewPos2() {
        val currPos = 1
        val positionStart = 2
        val itemCount = 10
        val expected = 1
        val newPos = getNewPos(currPos, positionStart, itemCount, preloadSize)
        assertThat(newPos).isEqualTo(expected)
    }

    @Test
    fun getNewPos3() {
        val currPos = 3
        val positionStart = 2
        val itemCount = 10
        val expected = 2
        val newPos = getNewPos(currPos, positionStart, itemCount, preloadSize)
        assertThat(newPos).isEqualTo(expected)
    }

    @Test
    fun getNewPos4() {
        val currPos = 5
        val positionStart = 2
        val itemCount = 10
        val expected = 2
        val newPos = getNewPos(currPos, positionStart, itemCount, preloadSize)
        assertThat(newPos).isEqualTo(expected)
    }

    @Test
    fun getNewPos5() {
        val currPos = 5
        val positionStart = 0
        val itemCount = 4
        val expected = 1
        val newPos = getNewPos(currPos, positionStart, itemCount, preloadSize)
        assertThat(newPos).isEqualTo(expected)
    }
}

private fun getNewPos(currPos: Int, positionStart: Int, itemCount: Int, preloadSize: Int): Int {
    return -1
}