package com.example.myapplication.model

import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherModelTest {

    @Test
    fun testWeatherDescription_ClearSky() {
        assertEquals("Clear sky", 0.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_PartlyCloudy_1() {
        assertEquals("Partly Cloudy", 1.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_PartlyCloudy_2() {
        assertEquals("Partly Cloudy", 2.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_PartlyCloudy_3() {
        assertEquals("Partly Cloudy", 3.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Foggy_45() {
        assertEquals("Foggy", 45.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Foggy_48() {
        assertEquals("Foggy", 48.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Drizzle_51() {
        assertEquals("Drizzle", 51.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Drizzle_53() {
        assertEquals("Drizzle", 53.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Drizzle_55() {
        assertEquals("Drizzle", 55.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Rainy_61() {
        assertEquals("Rainy", 61.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Rainy_63() {
        assertEquals("Rainy", 63.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Rainy_65() {
        assertEquals("Rainy", 65.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Snowy_71() {
        assertEquals("Snowy", 71.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Snowy_73() {
        assertEquals("Snowy", 73.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Snowy_75() {
        assertEquals("Snowy", 75.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_RainShowers_80() {
        assertEquals("Rain Showers", 80.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_RainShowers_81() {
        assertEquals("Rain Showers", 81.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_RainShowers_82() {
        assertEquals("Rain Showers", 82.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Thunderstorm_95() {
        assertEquals("Thunderstorm", 95.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Thunderstorm_96() {
        assertEquals("Thunderstorm", 96.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Thunderstorm_99() {
        assertEquals("Thunderstorm", 99.toWeatherDescription())
    }

    @Test
    fun testWeatherDescription_Unknown() {
        assertEquals("Unknown", (-1).toWeatherDescription())
        assertEquals("Unknown", 10.toWeatherDescription())
        assertEquals("Unknown", 100.toWeatherDescription())
    }
}
