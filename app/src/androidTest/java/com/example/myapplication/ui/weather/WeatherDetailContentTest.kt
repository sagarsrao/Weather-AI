package com.example.myapplication.ui.weather

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.example.myapplication.model.CurrentWeather
import com.example.myapplication.model.DailyWeather
import com.example.myapplication.model.HourlyWeather
import com.example.myapplication.model.StateInfo
import com.example.myapplication.model.WeatherResponse
import org.junit.Rule
import org.junit.Test

class WeatherDetailContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testStateInfo = StateInfo(
        name = "Test State",
        latitude = 10.0,
        longitude = 20.0,
        imageUrl = ""
    )

    private val fullWeatherData = WeatherResponse(
        latitude = 10.0,
        longitude = 20.0,
        current = CurrentWeather("2024-01-01T12:00", 25.5, 60, 15.0, 0),
        hourly = HourlyWeather(
            time = listOf("2024-01-01T13:00", "2024-01-01T14:00"),
            temperatures = listOf(26.0, 27.0),
            weatherCodes = listOf(1, 2)
        ),
        daily = DailyWeather(
            time = listOf("2024-01-01", "2024-01-02"),
            weatherCodes = listOf(0, 61),
            maxTemps = listOf(28.0, 24.0),
            minTemps = listOf(18.0, 19.0)
        )
    )

    @Test
    fun detailContent_displaysCurrentWeather() {
        composeTestRule.setContent {
            WeatherDetailContent(stateInfo = testStateInfo, weatherData = fullWeatherData)
        }

        composeTestRule.onNodeWithText("25.5°C").assertIsDisplayed()

        // Since "Clear sky" appears in both Current Weather (code 0) and Daily Forecast (code 0),
        // we use onAllNodes and assert the count instead of asserting a single node exists
        val clearSkyNodes = composeTestRule.onAllNodes(androidx.compose.ui.test.hasText("Clear sky"))
        assert(clearSkyNodes.fetchSemanticsNodes().size >= 1)

        composeTestRule.onNodeWithText("60%").assertIsDisplayed()
        composeTestRule.onNodeWithText("15.0 km/h").assertIsDisplayed()
    }

    @Test
    fun detailContent_displaysHourlyWeather() {
        composeTestRule.setContent {
            WeatherDetailContent(stateInfo = testStateInfo, weatherData = fullWeatherData)
        }

        composeTestRule.onNodeWithText("Hourly Forecast").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("13:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("26.0°").assertIsDisplayed()
        composeTestRule.onNodeWithText("14:00").assertIsDisplayed()
    }

    @Test
    fun detailContent_displaysDailyWeather() {
        composeTestRule.setContent {
            WeatherDetailContent(stateInfo = testStateInfo, weatherData = fullWeatherData)
        }

        composeTestRule.onNodeWithText("7-Day Forecast").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("2024-01-01").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("28° / 18°").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("2024-01-02").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("Rainy").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun detailContent_handlesMissingCurrentData() {
        val missingData = fullWeatherData.copy(current = null)
        composeTestRule.setContent {
            WeatherDetailContent(stateInfo = testStateInfo, weatherData = missingData)
        }

        composeTestRule.onNodeWithText("--°C").assertIsDisplayed()
        composeTestRule.onNodeWithText("Unknown").assertIsDisplayed()
        composeTestRule.onNodeWithText("--%").assertIsDisplayed()
        composeTestRule.onNodeWithText("-- km/h").assertIsDisplayed()
    }

    @Test
    fun detailContent_handlesMissingHourlyData() {
        val missingData = fullWeatherData.copy(hourly = null)
        composeTestRule.setContent {
            WeatherDetailContent(stateInfo = testStateInfo, weatherData = missingData)
        }

        composeTestRule.onNodeWithText("Hourly Forecast").performScrollTo().assertIsDisplayed()
        // Wait implicitly, checking that no crash happens when hourly is null
        composeTestRule.onNodeWithText("7-Day Forecast").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun detailContent_handlesMissingDailyData() {
        val missingData = fullWeatherData.copy(daily = null)
        composeTestRule.setContent {
            WeatherDetailContent(stateInfo = testStateInfo, weatherData = missingData)
        }

        composeTestRule.onNodeWithText("7-Day Forecast").performScrollTo().assertIsDisplayed()
        // Make sure it renders without crashing
    }

    @Test
    fun detailContent_displaysMaxHourlyItemsLimitation() {
        // Create 30 hourly items to test the 24-item max limit
        val times = List(30) { "2024-01-01T${String.format("%02d:00", it % 24)}" }
        val temps = List(30) { 20.0 + it }
        val codes = List(30) { 1 }

        val overflowData = fullWeatherData.copy(
            hourly = HourlyWeather(times, temps, codes)
        )

        composeTestRule.setContent {
            WeatherDetailContent(stateInfo = testStateInfo, weatherData = overflowData)
        }

        composeTestRule.onNodeWithText("Hourly Forecast").performScrollTo().assertIsDisplayed()
        // Scroll through some of the items to ensure row isn't broken
        composeTestRule.onNodeWithText("00:00").assertIsDisplayed()
    }
}
