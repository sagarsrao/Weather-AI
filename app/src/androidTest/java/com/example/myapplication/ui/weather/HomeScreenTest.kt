package com.example.myapplication.ui.weather

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysTopAppBar() {
        composeTestRule.setContent {
            HomeScreen(onStateSelected = {})
        }

        composeTestRule.onNodeWithText("Weather AI").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysInitialStates() {
        composeTestRule.setContent {
            HomeScreen(onStateSelected = {})
        }

        // Check if some of the first states in the list are displayed
        composeTestRule.onNodeWithText("Andhra Pradesh").assertIsDisplayed()
        composeTestRule.onNodeWithText("Arunachal Pradesh").assertIsDisplayed()
    }

    @Test
    fun homeScreen_scrollsToRevealStates() {
        composeTestRule.setContent {
            HomeScreen(onStateSelected = {})
        }

        // Use hasText mapping instead of direct scroll and checking for multiple nodes in case of lazy layouts
        composeTestRule.onNode(androidx.compose.ui.test.hasScrollToNodeAction())
            .performScrollToNode(androidx.compose.ui.test.hasText("Ladakh"))

        composeTestRule.onNodeWithText("Ladakh").assertIsDisplayed()

        composeTestRule.onNode(androidx.compose.ui.test.hasScrollToNodeAction())
            .performScrollToNode(androidx.compose.ui.test.hasText("Delhi"))

        composeTestRule.onNodeWithText("Delhi").assertIsDisplayed()
    }

    @Test
    fun homeScreen_clickingStateTriggersCallback() {
        var selectedState = ""
        composeTestRule.setContent {
            HomeScreen(onStateSelected = { selectedState = it })
        }

        // Click the first state card
        composeTestRule.onNodeWithText("Andhra Pradesh").performClick()

        assertEquals("Andhra Pradesh", selectedState)
    }
}
