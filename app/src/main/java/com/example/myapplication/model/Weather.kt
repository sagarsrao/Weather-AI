package com.example.myapplication.model

import com.example.myapplication.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentWeather?,
    val hourly: HourlyWeather?,
    val daily: DailyWeather?
)

@JsonClass(generateAdapter = true)
data class CurrentWeather(
    val time: String,
    @Json(name = "temperature_2m") val temperature: Double,
    @Json(name = "relative_humidity_2m") val humidity: Int,
    @Json(name = "wind_speed_10m") val windSpeed: Double,
    @Json(name = "weather_code") val weatherCode: Int
)

@JsonClass(generateAdapter = true)
data class HourlyWeather(
    val time: List<String>,
    @Json(name = "temperature_2m") val temperatures: List<Double>,
    @Json(name = "weather_code") val weatherCodes: List<Int>
)

@JsonClass(generateAdapter = true)
data class DailyWeather(
    val time: List<String>,
    @Json(name = "weather_code") val weatherCodes: List<Int>,
    @Json(name = "temperature_2m_max") val maxTemps: List<Double>,
    @Json(name = "temperature_2m_min") val minTemps: List<Double>
)

data class StateInfo(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String
)

fun Int.toWeatherDescription(): String {
    return when (this) {
        0 -> "Clear sky"
        1, 2, 3 -> "Partly Cloudy"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        61, 63, 65 -> "Rainy"
        71, 73, 75 -> "Snowy"
        80, 81, 82 -> "Rain Showers"
        95, 96, 99 -> "Thunderstorm"
        else -> "Unknown"
    }
}

// All states initially map to a local placeholder
// You can add unique images to res/drawable and update these IDs
val indianStates = listOf(
    StateInfo("Andhra Pradesh", 15.9129, 79.7400, "https://images.unsplash.com/photo-1590050752117-23a9d7fc21a3"),
    StateInfo("Arunachal Pradesh", 28.2180, 94.7278, "https://images.unsplash.com/photo-1626244101212-001099e036e4"),
    StateInfo("Assam", 26.2006, 92.9376, "https://images.unsplash.com/photo-1588091129653-532130095861"),
    StateInfo("Bihar", 25.0961, 85.3131, "https://images.unsplash.com/photo-1616843413587-9e3a37f7bbd8"),
    StateInfo("Chhattisgarh", 21.2787, 81.8661, "https://images.unsplash.com/photo-1605374468625-78351cc92437"),
    StateInfo("Goa", 15.2993, 74.1240, "https://images.unsplash.com/photo-1512780563841-382928810775"),
    StateInfo("Gujarat", 22.2587, 71.1924, "https://images.unsplash.com/photo-1599933023673-c288d8afb7ad"),
    StateInfo("Haryana", 29.0588, 76.0856, "https://images.unsplash.com/photo-1627834377411-8da5f4f09de8"),
    StateInfo("Himachal Pradesh", 31.1048, 77.1734, "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23"),
    StateInfo("Jharkhand", 23.6102, 85.2799, "https://images.unsplash.com/photo-1622312675038-17559e210134"),
    StateInfo("Karnataka", 15.3173, 75.7139, "https://images.unsplash.com/photo-1548013146-72479768bbaa"),
    StateInfo("Kerala", 10.8505, 76.2711, "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944"),
    StateInfo("Madhya Pradesh", 22.9734, 78.6569, "https://images.unsplash.com/photo-1610243405788-b271d50c7689"),
    StateInfo("Maharashtra", 19.7515, 75.7139, "https://images.unsplash.com/photo-1562979314-bee7453e911c"),
    StateInfo("Manipur", 24.6637, 93.9063, "https://images.unsplash.com/photo-1618342417724-4f9cf2e25681"),
    StateInfo("Meghalaya", 25.4670, 91.3662, "https://images.unsplash.com/photo-1512401830635-4200c012803b"),
    StateInfo("Mizoram", 23.1645, 92.9376, "https://images.unsplash.com/photo-1617475143093-6c68e1694f29"),
    StateInfo("Nagaland", 26.1584, 94.5624, "https://images.unsplash.com/photo-1618329027137-a521b641777d"),
    StateInfo("Odisha", 20.9517, 85.0985, "https://images.unsplash.com/photo-1596402184320-417d7178b2cd"),
    StateInfo("Punjab", 31.1471, 75.3412, "https://images.unsplash.com/photo-1582294432130-f20387538c23"),
    StateInfo("Rajasthan", 27.0238, 74.2179, "https://images.unsplash.com/photo-1477587458883-47145ed94245"),
    StateInfo("Sikkim", 27.5330, 88.5122, "https://images.unsplash.com/photo-1550974825-7550d4022637"),
    StateInfo("Tamil Nadu", 11.1271, 78.6569, "https://images.unsplash.com/photo-1582510003544-4d00b7f74220"),
    StateInfo("Telangana", 18.1124, 79.0193, "https://images.unsplash.com/photo-1605374468625-78351cc92437"),
    StateInfo("Tripura", 23.9408, 91.9882, "https://images.unsplash.com/photo-1616843413587-9e3a37f7bbd8"),
    StateInfo("Uttar Pradesh", 26.8467, 80.9462, "https://images.unsplash.com/photo-1583144548395-fca0f419d27a"),
    StateInfo("Uttarakhand", 30.0668, 79.0193, "https://images.unsplash.com/photo-1588665717621-e8d1217e997e"),
    StateInfo("West Bengal", 22.9868, 87.8550, "https://images.unsplash.com/photo-1558431382-27e303142255"),
    StateInfo("Delhi", 28.7041, 77.1025, "https://images.unsplash.com/photo-1587474260584-1f21950a8f4b"),
    StateInfo("Jammu & Kashmir", 33.7782, 76.5762, "https://images.unsplash.com/photo-1598091383021-15ddea10925d"),
    StateInfo("Ladakh", 34.1526, 77.5771, "https://images.unsplash.com/photo-1581793745862-99fde7fa73d2")
)
