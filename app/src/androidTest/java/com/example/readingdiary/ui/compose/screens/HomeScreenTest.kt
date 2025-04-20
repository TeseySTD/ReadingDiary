package com.example.readingdiary.ui.compose.screens

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.readingdiary.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var context: Context

    @Before
    fun setup() {
            context = composeTestRule.activity
        }

    @Test
    fun testInitialElementsAreDisplayed() {
        composeTestRule.onNodeWithText("Hello, reader!").assertIsDisplayed()

        composeTestRule.onNodeWithText("What reading today?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Enter your book title").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Confirm").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Toggle controls").assertIsDisplayed()
    }

    @Test
    fun testBookTitleInput() {
        composeTestRule.onNode(hasSetTextAction()).performTextInput("War and Peace")

        composeTestRule.onNode(hasText("War and Peace")).assertExists()

        composeTestRule.onNodeWithContentDescription("Confirm").performClick()

        composeTestRule.onNodeWithText("You are reading: War and Peace").assertIsDisplayed()
    }

    @Test
    fun testControlsToggle() {
        composeTestRule.onNodeWithText("Brightness:").assertDoesNotExist()

        composeTestRule.onNodeWithContentDescription("Toggle controls").performClick()

        composeTestRule.onNodeWithText("Brightness: 50%").assertIsDisplayed()
        composeTestRule.onNodeWithText("Messages: Off").assertIsDisplayed()
    }

    @Test
    fun testNotificationToggle() {
        composeTestRule.onNodeWithContentDescription("Toggle controls").performClick()

        composeTestRule.onNodeWithText("Messages: Off").assertIsDisplayed()

        composeTestRule.onNode(isToggleable()).performClick()

        composeTestRule.onNodeWithText("Messages: On").assertIsDisplayed()
    }

    @Test
    fun testHideControls() {
        composeTestRule.onNodeWithContentDescription("Toggle controls").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Brightness:", substring = true).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Toggle controls").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Brightness:", substring = true).assertDoesNotExist()
    }

}