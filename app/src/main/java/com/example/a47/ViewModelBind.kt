package com.example.a47

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//class ViewModelBind : ViewModel() {
//    private val _number = MutableStateFlow(0)
//    val number: StateFlow<Int> = _number
//    private var service: BindGenerator? = null
//    private var job: Job? = null
//
//    fun bindService(service: BindGenerator) {
//        this.service = service
//        job?.cancel()
//        job = viewModelScope.launch {
//            service.number.collect { value ->
//                _number.value = value
//            }
//        }
//    }
//
//    fun unbindService() {
//        job?.cancel()
//        service = null
//    }
//}