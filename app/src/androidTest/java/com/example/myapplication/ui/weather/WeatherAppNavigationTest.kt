package com.example.myapplication.ui.weather

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class WeatherAppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appNavigatesToDetailAndBack() {
        // Start the app
        composeTestRule.setContent {
            WeatherApp()
        }

        // Home Screen is displayed
        composeTestRule.onNodeWithText("Weather AI").assertIsDisplayed()
        composeTestRule.onNodeWithText("Goa").assertIsDisplayed()

        // Click on a state
        composeTestRule.onNodeWithText("Goa").performClick()

        // Detail screen is displayed (Top Bar should show state name)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(androidx.compose.ui.test.hasText("Goa")).fetchSemanticsNodes().isNotEmpty()
        }

        // Go back
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Home screen is back
        composeTestRule.onNodeWithText("Weather AI").assertIsDisplayed()
    }
}
