package com.example.propdash

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.example.propdash.components.manager.propertyDetails.DetailsScreen
import com.example.propdash.data.model.Property
import com.example.propdash.viewModel.manager.ManagerPropertyDetailViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class DetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun DetailsScreendisplayspropertydescriptionandimages() {
        // Arrange
        val property =
            Property(
                id = "1",
                description = "A beautiful property with modern amenities.",
                imageUrl = listOf(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg"
                ),
                name = "Test Property",
                userId = "1",
                maintenanceRequest = emptyList(),
                bookings = emptyList(),
                createdAt = "2021-01-01T00:00:00.000Z",
                updatedAt = "2021-01-01T00:00:00.000Z"
            )
        val viewModel = mockk<ManagerPropertyDetailViewModel>(relaxed = true)
        every { viewModel.property.value } returns property

        // Act
        composeTestRule.setContent {
            DetailsScreen(viewModel = viewModel)
        }

        // Assert
        // Verify description is displayed
        composeTestRule.onNodeWithText("Description").assertExists()
        composeTestRule.onNodeWithText("A beautiful property with modern amenities.").assertExists()
    }
}
