package com.example.readingdiary.ui.compose.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.readingdiary.MainActivity
import com.example.readingdiary.models.Routes
import com.example.readingdiary.models.User
import com.example.readingdiary.services.UserService
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        val user = User("Alice", "123")
        UserService.registerUser(user = user)
        UserService.loginUser(user = user)

        // Navigate to the Profile screen
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription(Routes.Profile.label).performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testProfileContentDisplaysAllElements() {
        composeTestRule.waitForIdle()

        // Profile icon
        composeTestRule.onNodeWithContentDescription("Profile Icon").assertIsDisplayed()

        // Header text
        composeTestRule.onNodeWithText("Users`s profile").assertIsDisplayed()

        // Username label and value
        composeTestRule.onNodeWithText("User name:").assertIsDisplayed()
        composeTestRule.onNodeWithText("Alice").assertIsDisplayed()

        // Logout button and icon
        composeTestRule.onNodeWithText("Logout").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Logout").assertIsDisplayed()
    }

    @Test
    fun testLogoutButtonCallsCallback() {
        val user = User("Alice", "123")
        UserService.registerUser(user = user)
        UserService.loginUser(user = user)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription("Logout").assertExists().performClick()

        assertTrue(!UserService.isAuthenticated(user))
    }
}

