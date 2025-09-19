package com.palm.chatapp.chatFragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.palm.chatapp.chat.ChatFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest {
    @Test
    fun showsMessages_worksWithViewLifecycleOwner() {
        val scenario = launchFragmentInContainer<ChatFragment>()
        scenario.onFragment { fragment ->
            fragment.viewModel.addMessage("seed")
        }
        scenario.recreate()
        scenario.onFragment { fragment ->
            fragment.viewModel.addMessage("afterRecreate")
        }
        onView(withText("Hello")).check(matches(isDisplayed()))
        onView(withText("Welcome")).check(matches(isDisplayed()))
    }
}
