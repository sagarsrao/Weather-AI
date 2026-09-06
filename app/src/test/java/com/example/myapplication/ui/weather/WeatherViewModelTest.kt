package com.example.myapplication.ui.weather

import app.cash.turbine.test
import com.example.myapplication.data.WeatherRepository
import com.example.myapplication.model.CurrentWeather
import com.example.myapplication.model.WeatherResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var repository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isLoading() = runTest {
        assertEquals(WeatherViewModel.WeatherState.Loading, viewModel.weatherState.value)
    }

    @Test
    fun fetchWeather_success_updatesStateToSuccess() = runTest {
        // Arrange
        val mockResponse = WeatherResponse(
            latitude = 12.0,
            longitude = 34.0,
            current = CurrentWeather("2024-01-01T12:00", 25.0, 50, 10.0, 0),
            hourly = null,
            daily = null
        )
        coEvery { repository.getFullWeather(any(), any()) } returns Response.success(mockResponse)

        // Act & Assert
        viewModel.weatherState.test {
            // Initial state
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())

            // Trigger fetch
            viewModel.fetchWeather(12.0, 34.0)

            // State changes to Success
            val successState = awaitItem() as WeatherViewModel.WeatherState.Success
            assertEquals(mockResponse, successState.weatherData)
            assertEquals(25.0, successState.weatherData.current?.temperature)
        }
    }

    @Test
    fun fetchWeather_failure_updatesStateToError() = runTest {
        // Arrange
        val errorBody = "Not Found".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { repository.getFullWeather(any(), any()) } returns Response.error(404, errorBody)

        // Act & Assert
        viewModel.weatherState.test {
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())

            viewModel.fetchWeather(12.0, 34.0)

            val errorState = awaitItem() as WeatherViewModel.WeatherState.Error
            assertTrue(errorState.message.contains("Failed to load weather data"))
        }
    }

    @Test
    fun fetchWeather_exception_updatesStateToError() = runTest {
        // Arrange
        coEvery { repository.getFullWeather(any(), any()) } throws RuntimeException("Network timeout")

        // Act & Assert
        viewModel.weatherState.test {
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())

            viewModel.fetchWeather(12.0, 34.0)

            val errorState = awaitItem() as WeatherViewModel.WeatherState.Error
            assertTrue(errorState.message.contains("An error occurred"))
            assertTrue(errorState.message.contains("Network timeout"))
        }
    }

    @Test
    fun fetchWeather_nullBody_updatesStateToError() = runTest {
        // Arrange
        coEvery { repository.getFullWeather(any(), any()) } returns Response.success(null)

        // Act & Assert
        viewModel.weatherState.test {
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())

            viewModel.fetchWeather(12.0, 34.0)

            val errorState = awaitItem() as WeatherViewModel.WeatherState.Error
            assertTrue(errorState.message.contains("Failed to load weather data"))
        }
    }

    @Test
    fun fetchWeather_extremeCoordinates_success() = runTest {
        val mockResponse = WeatherResponse(
            latitude = -90.0,
            longitude = 180.0,
            current = null,
            hourly = null,
            daily = null
        )
        coEvery { repository.getFullWeather(-90.0, 180.0) } returns Response.success(mockResponse)

        viewModel.weatherState.test {
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())
            viewModel.fetchWeather(-90.0, 180.0)
            val successState = awaitItem() as WeatherViewModel.WeatherState.Success
            assertEquals(-90.0, successState.weatherData.latitude, 0.0)
        }
    }

    @Test
    fun fetchWeather_zeroCoordinates_success() = runTest {
        val mockResponse = WeatherResponse(
            latitude = 0.0,
            longitude = 0.0,
            current = null,
            hourly = null,
            daily = null
        )
        coEvery { repository.getFullWeather(0.0, 0.0) } returns Response.success(mockResponse)

        viewModel.weatherState.test {
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())
            viewModel.fetchWeather(0.0, 0.0)
            val successState = awaitItem() as WeatherViewModel.WeatherState.Success
            assertEquals(0.0, successState.weatherData.latitude, 0.0)
        }
    }

    @Test
    fun fetchWeather_rapidConsecutiveCalls_overridesState() = runTest {
        val mockResponse = WeatherResponse(latitude = 1.0, longitude = 1.0, current = null, hourly = null, daily = null)
        coEvery { repository.getFullWeather(any(), any()) } returns Response.success(mockResponse)

        viewModel.weatherState.test {
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())

            viewModel.fetchWeather(1.0, 1.0)
            viewModel.fetchWeather(2.0, 2.0) // override

            // Due to conflation in StateFlow, we might just see one Success, or Loading -> Success
            val state = awaitItem()
            assertTrue(state is WeatherViewModel.WeatherState.Loading || state is WeatherViewModel.WeatherState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun fetchWeather_errorFormatMatchesExactly() = runTest {
        val errorBody = "Custom Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { repository.getFullWeather(any(), any()) } returns Response.error(500, errorBody)

        viewModel.weatherState.test {
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())
            viewModel.fetchWeather(12.0, 34.0)
            val errorState = awaitItem() as WeatherViewModel.WeatherState.Error
            assertEquals("Failed to load weather data: Response.error()", errorState.message)
        }
    }

    @Test
    fun fetchWeather_emptyArraysInPayload_success() = runTest {
        val mockResponse = WeatherResponse(
            latitude = 12.0,
            longitude = 34.0,
            current = null,
            hourly = com.example.myapplication.model.HourlyWeather(emptyList(), emptyList(), emptyList()),
            daily = com.example.myapplication.model.DailyWeather(emptyList(), emptyList(), emptyList(), emptyList())
        )
        coEvery { repository.getFullWeather(any(), any()) } returns Response.success(mockResponse)

        viewModel.weatherState.test {
            assertEquals(WeatherViewModel.WeatherState.Loading, awaitItem())
            viewModel.fetchWeather(12.0, 34.0)
            val successState = awaitItem() as WeatherViewModel.WeatherState.Success
            assertTrue(successState.weatherData.hourly?.time?.isEmpty() == true)
        }
    }
}
