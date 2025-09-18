package com.palm.chatapp.chatFragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.palm.chatapp.ui.ChatFragment
import com.palm.chatapp.ui.ChatViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest {
    private lateinit var mockViewModel: ChatViewModel
    private val messagesLiveData = MutableLiveData<List<String>>()

    @Before
    fun setup() {
        mockViewModel = mock(ChatViewModel::class.java)
        `when`(mockViewModel.messages).thenReturn(messagesLiveData)
    }

    @Test
    fun messagesAreDisplayedCorrectly() {
        val messages = listOf("Hello", "How are you?")
        messagesLiveData.postValue(messages)

        launchFragmentInContainer<ChatFragment>()

        onView(withText("Hello")).check(matches(isDisplayed()))
        onView(withText("How are you?")).check(matches(isDisplayed()))
    }

    @Test
    fun adapterUpdatesWithNewMessages() {
        val initialMessages = listOf("First message")
        messagesLiveData.postValue(initialMessages)

        val scenario = launchFragmentInContainer<ChatFragment>()

        onView(withText("First message")).check(matches(isDisplayed()))

        val newMessages = listOf("First message", "Second message")
        messagesLiveData.postValue(newMessages)

        onView(withText("Second message")).check(matches(isDisplayed()))
    }
}

