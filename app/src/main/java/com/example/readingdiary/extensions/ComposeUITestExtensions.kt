package com.example.readingdiary.extensions

import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assertIsDisplayed

class ComposeUITestExtensions {

    companion object{
        fun SemanticsNodeInteractionCollection.assertAreDisplayed(): SemanticsNodeInteractionCollection {
            val semNodes = fetchSemanticsNodes()
            semNodes.forEachIndexed { index, _ ->
                val semanticsNodeInteraction = get(index)
                semanticsNodeInteraction.assertIsDisplayed()
            }
            return this
        }
    }
}