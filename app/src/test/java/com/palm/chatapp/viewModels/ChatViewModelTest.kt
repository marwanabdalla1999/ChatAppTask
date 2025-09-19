package com.palm.chatapp.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.palm.chatapp.chat.ChatViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChatViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `initial messages contain hello and welcome`() {
        val vm = ChatViewModel()
        assertEquals(listOf("Hello", "Welcome"), vm.messages.value)
    }

    @Test
    fun `addMessage appends to messages`() {
        val vm = ChatViewModel()
        vm.addMessage("New one")
        assertEquals(listOf("Hello", "Welcome", "New one"), vm.messages.value)
    }
}
