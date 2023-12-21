package com.aiang.ui.screen.Timer

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TimerScreenViewModel: ViewModel() {
    private var timer: CountDownTimer? = null
    private var startTime: Long = 0L
    var currentTime by mutableStateOf(0L)
    var isFinished by mutableStateOf(false)
    var timeString by mutableStateOf("")

    fun setInitialTime(time: Long) {
        val initialTimeMillis = time * 60 * 1000
        startTime = initialTimeMillis
        currentTime = initialTimeMillis
        isFinished = false

        timer = object : CountDownTimer(initialTimeMillis, 1000) {
            override fun onTick(p0: Long) {
                currentTime = p0
                timeString = DateUtils.formatElapsedTime(currentTime / 1000)
                Log.i("Timer", currentTime.toString())
                Log.i("timeString", timeString)
            }

            override fun onFinish() {
                isFinished = true
                Log.i("onFinishViewModel", isFinished.toString())
            }
        }
    }

    fun startTimer() {
        Log.i("Timer", "startTimer")
        timer?.start()
    }

    fun pauseTimer() {
        Log.i("Timer", "pauseTimer")
        timer?.cancel()
        timeString = DateUtils.formatElapsedTime(currentTime / 1000)
    }
}