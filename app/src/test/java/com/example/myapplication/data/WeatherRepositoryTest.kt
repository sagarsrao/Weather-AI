package com.example.myapplication.data

import com.example.myapplication.api.WeatherApiService
import com.example.myapplication.model.WeatherResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryTest {

    private lateinit var apiService: WeatherApiService
    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        apiService = mockk()
        repository = WeatherRepository(apiService)
    }

    @Test
    fun getFullWeather_successReturnsData() = runBlocking {
        // Arrange
        val mockResponse = WeatherResponse(
            latitude = 12.0,
            longitude = 34.0,
            current = null,
            hourly = null,
            daily = null
        )
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.success(mockResponse)

        // Act
        val result = repository.getFullWeather(12.0, 34.0)

        // Assert
        assertTrue(result.isSuccessful)
        assertEquals(mockResponse, result.body())
        assertEquals(12.0, result.body()?.latitude)
    }

    @Test
    fun getFullWeather_apiErrorReturnsError() = runBlocking {
        // Arrange
        val errorBody = "{\"error\": \"Not Found\"}".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(404, errorBody)

        // Act
        val result = repository.getFullWeather(12.0, 34.0)

        // Assert
        assertFalse(result.isSuccessful)
        assertEquals(404, result.code())
    }

    @Test
    fun getFullWeather_serverErrorReturnsError() = runBlocking {
        // Arrange
        val errorBody = "Internal Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(500, errorBody)

        // Act
        val result = repository.getFullWeather(12.0, 34.0)

        // Assert
        assertFalse(result.isSuccessful)
        assertEquals(500, result.code())
    }

    @Test
    fun getFullWeather_unauthorizedErrorReturnsError() = runBlocking {
        // Arrange
        val errorBody = "Unauthorized".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(401, errorBody)

        // Act
        val result = repository.getFullWeather(10.0, 20.0)

        // Assert
        assertFalse(result.isSuccessful)
        assertEquals(401, result.code())
    }

    @Test
    fun getFullWeather_badRequestErrorReturnsError() = runBlocking {
        // Arrange
        val errorBody = "Bad Request".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(400, errorBody)

        // Act
        val result = repository.getFullWeather(10.0, 20.0)

        // Assert
        assertFalse(result.isSuccessful)
        assertEquals(400, result.code())
    }

    @Test
    fun getFullWeather_exceptionIsThrown() = runBlocking {
        // Arrange
        coEvery { apiService.getFullWeather(any(), any()) } throws RuntimeException("Network Error")

        // Act & Assert
        try {
            repository.getFullWeather(10.0, 20.0)
            org.junit.Assert.fail("Expected exception")
        } catch (e: Exception) {
            assertEquals("Network Error", e.message)
        }
    }

    @Test
    fun getFullWeather_forbiddenErrorReturnsError() = runBlocking {
        val errorBody = "Forbidden".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(403, errorBody)

        val result = repository.getFullWeather(10.0, 20.0)

        assertFalse(result.isSuccessful)
        assertEquals(403, result.code())
    }

    @Test
    fun getFullWeather_requestTimeoutReturnsError() = runBlocking {
        val errorBody = "Request Timeout".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(408, errorBody)

        val result = repository.getFullWeather(10.0, 20.0)

        assertFalse(result.isSuccessful)
        assertEquals(408, result.code())
    }

    @Test
    fun getFullWeather_tooManyRequestsReturnsError() = runBlocking {
        val errorBody = "Too Many Requests".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(429, errorBody)

        val result = repository.getFullWeather(10.0, 20.0)

        assertFalse(result.isSuccessful)
        assertEquals(429, result.code())
    }

    @Test
    fun getFullWeather_badGatewayReturnsError() = runBlocking {
        val errorBody = "Bad Gateway".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(502, errorBody)

        val result = repository.getFullWeather(10.0, 20.0)

        assertFalse(result.isSuccessful)
        assertEquals(502, result.code())
    }

    @Test
    fun getFullWeather_serviceUnavailableReturnsError() = runBlocking {
        val errorBody = "Service Unavailable".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.getFullWeather(any(), any()) } returns Response.error(503, errorBody)

        val result = repository.getFullWeather(10.0, 20.0)

        assertFalse(result.isSuccessful)
        assertEquals(503, result.code())
    }
}
