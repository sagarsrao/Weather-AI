package com.example.myapplication.ui.weather

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test

class WeatherComponentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun infoTile_displaysLabelAndValueCorrectly() {
        composeTestRule.setContent {
            InfoTile(label = "Humidity", value = "50%")
        }

        composeTestRule.onNodeWithText("Humidity").assertIsDisplayed()
        composeTestRule.onNodeWithText("50%").assertIsDisplayed()
    }

    @Test
    fun infoTile_displaysWindCorrectly() {
        composeTestRule.setContent {
            InfoTile(label = "Wind", value = "12 km/h")
        }

        composeTestRule.onNodeWithText("Wind").assertIsDisplayed()
        composeTestRule.onNodeWithText("12 km/h").assertIsDisplayed()
    }

    @Test
    fun hourlyItem_displaysDataCorrectly() {
        composeTestRule.setContent {
            HourlyItem(time = "14:00", temp = 28.5, code = 0)
        }

        composeTestRule.onNodeWithText("14:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("28.5°").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clear sky").assertIsDisplayed()
    }

    @Test
    fun hourlyItem_displaysRainyDataCorrectly() {
        composeTestRule.setContent {
            HourlyItem(time = "15:00", temp = 22.0, code = 61)
        }

        composeTestRule.onNodeWithText("15:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("22.0°").assertIsDisplayed()
        composeTestRule.onNodeWithText("Rainy").assertIsDisplayed()
    }

    @Test
    fun dailyItem_displaysDataCorrectly() {
        composeTestRule.setContent {
            DailyItem(date = "2024-05-10", maxTemp = 30.0, minTemp = 20.0, code = 1)
        }

        composeTestRule.onNodeWithText("2024-05-10").assertIsDisplayed()
        composeTestRule.onNodeWithText("Partly Cloudy").assertIsDisplayed()
        composeTestRule.onNodeWithText("30° / 20°").assertIsDisplayed()
    }

    @Test
    fun dailyItem_displaysNegativeTempsCorrectly() {
        composeTestRule.setContent {
            DailyItem(date = "2024-12-25", maxTemp = -5.0, minTemp = -15.0, code = 71)
        }

        composeTestRule.onNodeWithText("2024-12-25").assertIsDisplayed()
        composeTestRule.onNodeWithText("Snowy").assertIsDisplayed()
        composeTestRule.onNodeWithText("-5° / -15°").assertIsDisplayed()
    }
}
