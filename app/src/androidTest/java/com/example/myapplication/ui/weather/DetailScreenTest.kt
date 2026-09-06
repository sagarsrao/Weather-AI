package com.example.myapplication.ui.weather

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.myapplication.data.WeatherRepository
import com.example.myapplication.model.CurrentWeather
import com.example.myapplication.model.StateInfo
import com.example.myapplication.model.WeatherResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class DetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testStateInfo = StateInfo(
        name = "Maharashtra",
        latitude = 19.75,
        longitude = 75.71,
        imageUrl = ""
    )

    @Test
    fun detailScreen_showsLoadingState() {
        val mockRepo = mockk<WeatherRepository>(relaxed = true)
        val viewModel = WeatherViewModel(mockRepo) // Will start in Loading state

        composeTestRule.setContent {
            DetailScreen(stateInfo = testStateInfo, viewModel = viewModel, onBack = {})
        }

        composeTestRule.onNodeWithText("Maharashtra").assertIsDisplayed()
        // Wait for CircularProgressIndicator (has no default text, but we can check if it exists implicitly by assuming error/success aren't there)
        composeTestRule.onNodeWithText("Error", substring = true).assertDoesNotExist()
        composeTestRule.onNodeWithText("Hourly Forecast").assertDoesNotExist()
    }

    @Test
    fun detailScreen_showsErrorState() {
        val mockRepo = mockk<WeatherRepository>()
        coEvery { mockRepo.getFullWeather(any(), any()) } returns Response.error(404, mockk(relaxed = true))

        val viewModel = WeatherViewModel(mockRepo)

        composeTestRule.setContent {
            DetailScreen(stateInfo = testStateInfo, viewModel = viewModel, onBack = {})
        }

        // Wait for error text
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            viewModel.weatherState.value is WeatherViewModel.WeatherState.Error
        }

        composeTestRule.onNodeWithText("Failed to load weather data", substring = true).assertIsDisplayed()
    }

    @Test
    fun detailScreen_showsSuccessState() {
        val mockRepo = mockk<WeatherRepository>()
        val successData = WeatherResponse(
            latitude = 19.75,
            longitude = 75.71,
            current = CurrentWeather("2024-01-01T12:00", 30.0, 50, 10.0, 0),
            hourly = null,
            daily = null
        )
        coEvery { mockRepo.getFullWeather(any(), any()) } returns Response.success(successData)

        val viewModel = WeatherViewModel(mockRepo)

        composeTestRule.setContent {
            DetailScreen(stateInfo = testStateInfo, viewModel = viewModel, onBack = {})
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            viewModel.weatherState.value is WeatherViewModel.WeatherState.Success
        }

        composeTestRule.onNodeWithText("30.0°C").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clear sky").assertIsDisplayed()
    }

    @Test
    fun detailScreen_backButtonTriggersCallback() {
        var backPressed = false
        val mockRepo = mockk<WeatherRepository>(relaxed = true)
        val viewModel = WeatherViewModel(mockRepo)

        composeTestRule.setContent {
            DetailScreen(
                stateInfo = testStateInfo,
                viewModel = viewModel,
                onBack = { backPressed = true }
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        assertTrue(backPressed)
    }
}
