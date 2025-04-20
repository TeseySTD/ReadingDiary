package com.example.readingdiary.ui.compose.screens

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.readingdiary.MainActivity
import com.example.readingdiary.extensions.ComposeUITestExtensions.Companion.assertAreDisplayed
import com.example.readingdiary.models.Routes
import com.example.readingdiary.repo.BookRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var context: Context

    @Before
    fun setup() {
        context = composeTestRule.activity

        // Navigate to the Login screen
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription(Routes.Login.label).performClick()
        composeTestRule.waitForIdle()
    }

    @After
    fun finish(){
        composeTestRule.activityRule.scenario.close()
    }

    @Test
    fun testLoginScreenElementsExist() {
        // Check for the existence of login form elements
        composeTestRule.onAllNodesWithText("Login").assertAreDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
    }

    @Test
    fun testLoginButtonInitiallyDisabled() {
        // Login button should be disabled initially when form is empty
        composeTestRule.onNodeWithContentDescription("Login button").assertIsNotEnabled()
    }

    @Test
    fun testLoginFormValidation() {
        // Enter valid credentials
        composeTestRule.onNode(hasText("Login") and hasSetTextAction()).performTextInput("user@example.com")
        composeTestRule.onNode(hasText("Password") and hasSetTextAction()).performTextInput("password123")

        // Verify login button is enabled
        composeTestRule.onNodeWithContentDescription("Login button").assertIsEnabled()
    }

    @Test
    fun testTextFieldsInteraction() {
        // Test login field interaction
        composeTestRule.onNode(hasText("Login") and hasSetTextAction()).performTextInput("test@example.com")
        composeTestRule.onNode(hasText("test@example.com")).assertExists()

        // Test password field interaction
        // Note: Password is masked, so we can't verify the exact text is displayed
        composeTestRule.onNode(hasText("Password") and hasSetTextAction()).performTextInput("testpassword")

        // Clear login field
        composeTestRule.onNode(hasText("Login") and hasSetTextAction()).performTextClearance()
        composeTestRule.onNode(hasText("test@example.com")).assertDoesNotExist()
    }

    @Test
    fun testPasswordIsHidden() {
        // Enter password
        composeTestRule.onNode(hasText("Password") and hasSetTextAction()).performTextInput("secretpassword")

        // Verify that the actual password text is not visible
        // (this is a bit tricky in UI tests since the password is masked)
        composeTestRule.onNode(hasText("secretpassword")).assertDoesNotExist()
    }
}