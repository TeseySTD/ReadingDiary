package com.example.readingdiary.ui.compose.screens

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.readingdiary.MainActivity
import com.example.readingdiary.models.Routes
import com.example.readingdiary.repo.NoteRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotesScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var context: Context

    @Before
    fun setup() {
        context = composeTestRule.activity

        // Navigate to the Notes screen
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription(Routes.Notes.label).performClick()
        composeTestRule.waitForIdle()

        // Verify we are on the Notes screen
        composeTestRule.onNodeWithText(Routes.Notes.route).assertIsDisplayed()
    }

    @After
    fun finish(){
        composeTestRule.activityRule.scenario.close()

        NoteRepository.reset()
    }

    @Test
    fun testNotesListElementsExist() {
        // Check for the existence of note list items
        composeTestRule.waitForIdle()
        composeTestRule.onAllNodesWithContentDescription("Note item").onFirst().assertExists()
    }

    @Test
    fun testNoteItemHasRequiredElements() {
        // Verify that note items contain all necessary components
        composeTestRule.waitForIdle()

        // Find first note item
        val firstNoteItem = composeTestRule.onAllNodesWithContentDescription("Note item").onFirst()
        firstNoteItem.assertExists()

        // Check for required elements within the note item
        composeTestRule.onNodeWithContentDescription("Delete note").assertExists()

        composeTestRule.onNodeWithText("Content:", substring = true).assertExists()
    }

    @Test
    fun testDeleteNoteInteraction() {
        // Count notes before deletion
        val initialNoteCount = composeTestRule.onAllNodesWithContentDescription("Note item").fetchSemanticsNodes().size

        // Find delete button and click it
        composeTestRule.onNodeWithContentDescription("Delete note").performClick()

        // Wait for UI to update
        composeTestRule.waitForIdle()

        // Verify note count decreased
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithContentDescription("Note item").fetchSemanticsNodes().size < initialNoteCount
        }
    }

    @Test
    fun testNoteItemDisplaysCorrectInfo() {
        // Verify notes display correct information
        composeTestRule.waitForIdle()

        // Find first note item
        val firstNoteItem = composeTestRule.onAllNodesWithContentDescription("Note item").onFirst()
        firstNoteItem.assertExists()

        // Check for note content
        composeTestRule.onNodeWithText("Content:", substring = true).assertExists()
    }


}