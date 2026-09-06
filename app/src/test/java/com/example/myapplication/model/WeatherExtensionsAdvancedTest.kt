package com.example.myapplication.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class WeatherExtensionsAdvancedTest {

    @Test
    fun testDescriptionForCode_NegativeOne() {
        assertEquals("Unknown", (-1).toWeatherDescription())
    }

    @Test
    fun testDescriptionForCode_Zero() {
        assertEquals("Clear sky", 0.toWeatherDescription())
    }

    @Test
    fun testDescriptionForCode_One() {
        assertEquals("Partly Cloudy", 1.toWeatherDescription())
    }

    @Test
    fun testDescriptionForCode_Two() {
        assertEquals("Partly Cloudy", 2.toWeatherDescription())
    }

    @Test
    fun testDescriptionForCode_Three() {
        assertEquals("Partly Cloudy", 3.toWeatherDescription())
    }

    @Test
    fun testDescriptionForCode_FortyFive() {
        assertEquals("Foggy", 45.toWeatherDescription())
    }

    @Test
    fun testDescriptionForCode_SixtyOne() {
        assertEquals("Rainy", 61.toWeatherDescription())
    }

    @Test
    fun testDescriptionForCode_NinetyFive() {
        assertEquals("Thunderstorm", 95.toWeatherDescription())
    }

    @Test
    fun testStateInfoInitialization() {
        val state = StateInfo("Test", 10.0, 20.0, "https://example.com/image.jpg")
        assertNotNull(state)
        assertEquals("Test", state.name)
        assertEquals(10.0, state.latitude, 0.0)
        assertEquals(20.0, state.longitude, 0.0)
        assertEquals("https://example.com/image.jpg", state.imageUrl)
    }
}
