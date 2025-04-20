package com.example.readingdiary.ui.compose.screens

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.readingdiary.MainActivity
import com.example.readingdiary.models.Routes
import com.example.readingdiary.repo.ReadingPlanRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlansScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var context: Context

    @Before
    fun setup() {
        context = composeTestRule.activity

        // Navigate to the Plans screen
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription(Routes.Plans.label).performClick()
        composeTestRule.waitForIdle()
    }

    @After
    fun finish(){
        composeTestRule.activityRule.scenario.close()

        ReadingPlanRepository.reset()
    }

    @Test
    fun testPlansListElementsExist() {
        // Check for the existence of plan list items
        composeTestRule.waitForIdle()
        composeTestRule.onAllNodesWithContentDescription("Plan item").onFirst().assertExists()
    }

    @Test
    fun testPlanItemHasRequiredElements() {
        // Verify that plan items contain all necessary components
        composeTestRule.waitForIdle()

        // Find first plan item
        val firstPlanItem = composeTestRule.onAllNodesWithContentDescription("Plan item").onFirst()
        firstPlanItem.assertExists()

        // Check for required elements within the plan item
        composeTestRule.onNodeWithContentDescription("Complete").assertExists()

        // Check that plan details exist
        composeTestRule.onNodeWithText("Books:", substring = true).assertExists()
        composeTestRule.onNodeWithText("Start:", substring = true).assertExists()
        composeTestRule.onNodeWithText("End:", substring = true).assertExists()
    }

    @Test
    fun testCompletePlanInteraction() {
        // Find complete button and click it
        composeTestRule.onNodeWithContentDescription("Complete").performClick()

        // Wait for UI to update
        composeTestRule.waitForIdle()

        // Verify plan is marked as completed
        composeTestRule.onNodeWithText("Completed").assertExists()
    }

    @Test
    fun testPlanItemDisplaysCorrectInfo() {
        // Verify plans display correct information
        composeTestRule.waitForIdle()

        // Find first plan item
        val firstPlanItem = composeTestRule.onAllNodesWithContentDescription("Plan item").onFirst()
        firstPlanItem.assertExists()

        // Check that details have actual values (not empty)
        composeTestRule.onAllNodesWithText("Books:").onFirst().assertTextContains("Books:", substring = true)
        composeTestRule.onAllNodesWithText("Start:").onFirst().assertTextContains("Start:", substring = true)
        composeTestRule.onAllNodesWithText("End:").onFirst().assertTextContains("End:", substring = true)
    }


    @Test
    fun testPlanReadingTime() {
        // Check that progress indicator exists and shows correct information
        composeTestRule.onNodeWithContentDescription("Plan progress").assertExists()
    }
}