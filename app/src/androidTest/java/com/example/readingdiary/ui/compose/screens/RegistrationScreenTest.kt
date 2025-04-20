package com.example.readingdiary.ui.compose.screens

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.readingdiary.MainActivity
import com.example.readingdiary.models.Routes
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var context: Context

    @Before
    fun setup() {
        context = composeTestRule.activity
        // Navigate to Registration screen via bottom nav bar
        composeTestRule
            .onNodeWithText(Routes.Registration.label)
            .performClick()
        composeTestRule
            .onNodeWithText(Routes.Registration.route)
            .assertIsDisplayed()
    }

    @After
    fun finish(){
        composeTestRule.activityRule.scenario.close()
    }

    @Test
    fun testInitialElementsAreDisplayed() {
        // Labels
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Birth Date (YYYY-MM-DD)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Accept Privacy Policy").assertIsDisplayed()

        // Register button should start disabled
        composeTestRule
            .onNodeWithText("Register")
            .assertIsNotEnabled()
    }

    @Test
    fun testLoginAndBirthDateInput() {
        // Enter login
        composeTestRule
            .onNode(
                hasText("Login") and hasSetTextAction()
            )
            .performTextInput("testuser")
        // Enter birth date
        composeTestRule
            .onNode(
                hasText("Birth Date (YYYY-MM-DD)") and hasSetTextAction()
            )
            .performTextInput("2000-01-01")

        // Verify input reflected
        composeTestRule.onNodeWithText("testuser").assertExists()
        composeTestRule.onNodeWithText("2000-01-01").assertExists()
    }

    @Test
    fun testPolicySwitchInteraction() {
        // Switch should be off initially
        composeTestRule
            .onNode(isToggleable())
            .assertIsOff()

        // Toggle switch on
        composeTestRule
            .onNode(isToggleable())
            .performClick()
            .assertIsOn()
    }

    @Test
    fun testRegisterButtonEnablement() {
        // Fill all fields
        composeTestRule
            .onNode(hasText("Login") and hasSetTextAction())
            .performTextInput("user1")
        composeTestRule
            .onNode(hasText("Password") and hasSetTextAction())
            .performTextInput("pass123")
        composeTestRule
            .onNode(hasText("Birth Date (YYYY-MM-DD)") and hasSetTextAction())
            .performTextInput("2000-01-01")
        composeTestRule
            .onNode(isToggleable())
            .performClick()

        composeTestRule.waitForIdle()

        // Register button should now be enabled
        composeTestRule
            .onNodeWithText("Register")
            .assertIsEnabled()
    }
}
