package com.example.myapplication.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class WeatherResponseParsingTest {

    private lateinit var moshi: Moshi
    private lateinit var adapter: JsonAdapter<WeatherResponse>

    @Before
    fun setup() {
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        adapter = moshi.adapter(WeatherResponse::class.java)
    }

    @Test
    fun parseFullJsonPayload_mapsCorrectly() {
        val json = """
            {
                "latitude": 12.0,
                "longitude": 34.0,
                "current": {
                    "time": "2024-01-01T12:00",
                    "temperature_2m": 25.0,
                    "relative_humidity_2m": 50,
                    "wind_speed_10m": 10.0,
                    "weather_code": 0
                },
                "hourly": {
                    "time": ["2024-01-01T13:00"],
                    "temperature_2m": [26.0],
                    "weather_code": [1]
                },
                "daily": {
                    "time": ["2024-01-01"],
                    "weather_code": [0],
                    "temperature_2m_max": [30.0],
                    "temperature_2m_min": [15.0]
                }
            }
        """.trimIndent()

        val result = adapter.fromJson(json)
        assertNotNull(result)
        assertEquals(12.0, result?.latitude)
        assertEquals(25.0, result?.current?.temperature)
        assertEquals(1, result?.hourly?.weatherCodes?.first())
        assertEquals(30.0, result?.daily?.maxTemps?.first())
    }

    @Test
    fun parseMissingCurrentBlock_mapsToNull() {
        val json = """
            {
                "latitude": 12.0,
                "longitude": 34.0,
                "hourly": {
                    "time": [],
                    "temperature_2m": [],
                    "weather_code": []
                }
            }
        """.trimIndent()

        val result = adapter.fromJson(json)
        assertNull(result?.current)
        assertNull(result?.daily)
        assertNotNull(result?.hourly)
    }

    @Test
    fun parseMissingHourlyBlock_mapsToNull() {
         val json = """
            {
                "latitude": 12.0,
                "longitude": 34.0,
                "current": {
                    "time": "2024-01-01T12:00",
                    "temperature_2m": 25.0,
                    "relative_humidity_2m": 50,
                    "wind_speed_10m": 10.0,
                    "weather_code": 0
                }
            }
        """.trimIndent()

        val result = adapter.fromJson(json)
        assertNull(result?.hourly)
        assertNotNull(result?.current)
    }

    @Test
    fun parseMissingDailyBlock_mapsToNull() {
        val json = """
            {
                "latitude": 12.0,
                "longitude": 34.0
            }
        """.trimIndent()

        val result = adapter.fromJson(json)
        assertNull(result?.daily)
    }

    @Test
    fun parseUnexpectedFields_ignoresThem() {
        val json = """
            {
                "latitude": 12.0,
                "longitude": 34.0,
                "unexpected_field": "some data",
                "another_unexpected_object": {}
            }
        """.trimIndent()

        val result = adapter.fromJson(json)
        assertNotNull(result)
        assertEquals(12.0, result?.latitude)
    }

    @Test(expected = JsonDataException::class)
    fun parseEmptyJson_failsParsing() {
        val json = "{}"
        adapter.fromJson(json)
    }

    @Test(expected = JsonDataException::class)
    fun parseMalformedDataTypes_failsParsing() {
        val json = """
            {
                "latitude": "twelve",
                "longitude": 34.0
            }
        """.trimIndent()
        adapter.fromJson(json)
    }

    @Test
    fun parseExplicitNulls_mapsCorrectly() {
        val json = """
            {
                "latitude": 12.0,
                "longitude": 34.0,
                "current": null,
                "hourly": null,
                "daily": null
            }
        """.trimIndent()

        val result = adapter.fromJson(json)
        assertNotNull(result)
        assertNull(result?.current)
        assertNull(result?.hourly)
        assertNull(result?.daily)
    }
}
