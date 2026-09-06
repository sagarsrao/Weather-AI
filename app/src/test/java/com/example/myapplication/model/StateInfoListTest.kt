package com.example.myapplication.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StateInfoListTest {

    @Test
    fun listContainsExactly31States() {
        assertEquals("List should contain exactly 31 states and union territories", 31, indianStates.size)
    }

    @Test
    fun listHasNoDuplicateNames() {
        val names = indianStates.map { it.name }
        val uniqueNames = names.toSet()
        assertEquals("There should be no duplicate state names", names.size, uniqueNames.size)
    }

    @Test
    fun allLatitudesAreWithinValidBounds() {
        // Approximate bounds for India: Lat 8.0 to 37.5
        indianStates.forEach { state ->
            assertTrue(
                "Latitude for ${state.name} is out of bounds: ${state.latitude}",
                state.latitude in 8.0..38.0
            )
        }
    }

    @Test
    fun allLongitudesAreWithinValidBounds() {
        // Approximate bounds for India: Lon 68.0 to 97.5
        indianStates.forEach { state ->
            assertTrue(
                "Longitude for ${state.name} is out of bounds: ${state.longitude}",
                state.longitude in 68.0..98.0
            )
        }
    }

    @Test
    fun allImageUrlsAreValidHttpsLinks() {
        indianStates.forEach { state ->
            assertTrue(
                "URL for ${state.name} is invalid: ${state.imageUrl}",
                state.imageUrl.isNotBlank() && state.imageUrl.startsWith("https://")
            )
        }
    }
}
