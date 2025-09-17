package com.palm.chatapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    private val _messages = MutableLiveData(listOf("Hello", "Welcome"))
    val messages: LiveData<List<String>> get() = _messages

    fun addMessage(msg: String) {
        val updated = _messages.value.orEmpty() + msg
        _messages.value = updated
    }
}
