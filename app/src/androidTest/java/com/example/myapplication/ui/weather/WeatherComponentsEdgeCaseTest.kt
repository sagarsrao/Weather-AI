package com.example.myapplication.ui.weather

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class WeatherComponentsEdgeCaseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun hourlyItem_extremeHighTemperature() {
        composeTestRule.setContent {
            HourlyItem(time = "14:00", temp = 105.0, code = 0)
        }
        composeTestRule.onNodeWithText("105.0°").assertIsDisplayed()
    }

    @Test
    fun hourlyItem_extremeLowTemperature() {
        composeTestRule.setContent {
            HourlyItem(time = "04:00", temp = -50.0, code = 71)
        }
        composeTestRule.onNodeWithText("-50.0°").assertIsDisplayed()
    }

    @Test
    fun infoTile_extremelyLongLabelDoesNotCrash() {
        val longLabel = "This is a very extremely long label that might cause some overflow issues if not handled"
        composeTestRule.setContent {
            InfoTile(label = longLabel, value = "100")
        }
        composeTestRule.onNodeWithText(longLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText("100").assertIsDisplayed()
    }

    @Test
    fun infoTile_zeroPercentHumidity() {
        composeTestRule.setContent {
            InfoTile(label = "Humidity", value = "0%")
        }
        composeTestRule.onNodeWithText("0%").assertIsDisplayed()
    }

    @Test
    fun dailyItem_equalMinAndMaxTemperatures() {
        composeTestRule.setContent {
            DailyItem(date = "2025-01-01", maxTemp = 20.0, minTemp = 20.0, code = 3)
        }
        composeTestRule.onNodeWithText("20° / 20°").assertIsDisplayed()
    }
}
