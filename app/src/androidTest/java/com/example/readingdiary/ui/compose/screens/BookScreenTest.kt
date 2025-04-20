package com.example.readingdiary.ui.compose.screens

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.readingdiary.MainActivity
import com.example.readingdiary.models.Routes
import com.example.readingdiary.repo.BookRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BooksScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var context: Context

    @Before
    fun setup() {
        context = composeTestRule.activity

        // Navigate to the Books screen
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription(Routes.Books.label).performClick()
        composeTestRule.waitForIdle()

        // Verify we are on the Books screen
        composeTestRule.onNodeWithText(Routes.Books.route).assertIsDisplayed()
    }

    @After
    fun finish(){
        composeTestRule.activityRule.scenario.close()

        BookRepository.reset()
    }

    @Test
    fun testBookListElementsExist() {
        composeTestRule.onAllNodesWithContentDescription("Book item").onFirst().assertExists()
    }

    @Test
    fun testBookItemHasRequiredElements() {
        composeTestRule.onAllNodesWithContentDescription("Book item").onFirst().assertExists()

        composeTestRule.onNodeWithContentDescription("Book selection").assertExists()
        composeTestRule.onNodeWithContentDescription("Book rating").assertExists()
        composeTestRule.onNodeWithContentDescription("Delete book").assertExists()
    }

    @Test
    fun testBookSelectionInteraction() {
        composeTestRule.onNodeWithContentDescription("Book selection").performClick()

        composeTestRule.onNodeWithText("Make reading plan").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Book selection").performClick()

        composeTestRule.onNodeWithText("Make reading plan").assertDoesNotExist()
    }

    @Test
    fun testDeleteBookInteraction() {
        composeTestRule.onNodeWithContentDescription("Delete book").performClick()
    }

    @Test
    fun testRatingInteraction() {
        composeTestRule.onNodeWithContentDescription("Book rating").performClick()
    }

    @Test
    fun testReadingPlanButtonClick() {
        composeTestRule.onNodeWithContentDescription("Book selection").performClick()

        composeTestRule.onNodeWithText("Make reading plan").assertIsDisplayed()

        composeTestRule.onNodeWithText("Make reading plan").performClick()

        // В реальном тесте здесь может быть проверка открытия нового экрана
        // или появления диалога с планом чтения
    }
}