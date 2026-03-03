package com.example.a47

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BindGenerator : Service() {
    private val binder = RandomBinder()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null
    private val _number = MutableStateFlow(0)
    val number: StateFlow<Int> = _number

    inner class RandomBinder : Binder() {
        val service: BindGenerator
            get() = this@BindGenerator
    }

    override fun onBind(intent: Intent?): IBinder {
        startGenerating()
        return binder
    }
    override fun onUnbind(intent: Intent?): Boolean {
        stopGenerating()
        return super.onUnbind(intent)
    }
    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }
    private fun startGenerating() {
        job?.cancel()
        job = serviceScope.launch {
            while (isActive) {
                delay(1000)
                _number.value = (0..100).random()
            }
        }
    }
    private fun stopGenerating() {
        job?.cancel()
    }
}