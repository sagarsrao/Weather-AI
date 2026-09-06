package com.example.myapplication.ui.weather

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeScreenExtendedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_clickMiddleTierState() {
        var selectedState = ""
        composeTestRule.setContent {
            HomeScreen(onStateSelected = { selectedState = it })
        }

        // Scroll to Kerala and click
        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(hasText("Kerala"))
        composeTestRule.onNodeWithText("Kerala").performClick()

        assertEquals("Kerala", selectedState)
    }

    @Test
    fun homeScreen_clickLastState() {
        var selectedState = ""
        composeTestRule.setContent {
            HomeScreen(onStateSelected = { selectedState = it })
        }

        // Scroll to the very bottom
        composeTestRule.onNode(hasScrollToNodeAction())
            .performScrollToNode(hasText("Ladakh"))
        composeTestRule.onNodeWithText("Ladakh").performClick()

        assertEquals("Ladakh", selectedState)
    }
}
