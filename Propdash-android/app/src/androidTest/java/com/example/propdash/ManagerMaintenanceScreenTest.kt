package com.example.propdash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.propdash.components.manager.ManagerMaintenanceScreen
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.model.Property
import com.example.propdash.viewModel.manager.ManagerMaintenanceRequestViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ManagerMaintenanceScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorStateShowsErrorText() {
        // Arrange
        val mockViewModel = mockk<ManagerMaintenanceRequestViewModel>(relaxed = true)
        every { mockViewModel.isLoading } returns MutableStateFlow(false)
        every { mockViewModel.maintenanceRequests } returns MutableStateFlow(emptyList())
        every { mockViewModel.maintenanceError } returns MutableStateFlow("Error loading requests")
        every { mockViewModel.isRefreshing } returns MutableStateFlow(false)

        // Act
        composeTestRule.setContent {
            ManagerMaintenanceScreen(
                navigate = {},
                viewModel = mockViewModel
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Error loading requests").assertIsDisplayed() // Error message
        composeTestRule.onNodeWithText("No maintenance requests").assertDoesNotExist() // Ensure no data
    }

    @Test
    fun emptyStateShowsNoMaintenanceRequestsMessage() {
        // Arrange
        val mockViewModel = mockk<ManagerMaintenanceRequestViewModel>(relaxed = true)
        every { mockViewModel.isLoading } returns MutableStateFlow(false)
        every { mockViewModel.maintenanceRequests } returns MutableStateFlow(emptyList())
        every { mockViewModel.maintenanceError } returns MutableStateFlow(null)
        every { mockViewModel.isRefreshing } returns MutableStateFlow(false)

        // Act
        composeTestRule.setContent {
            ManagerMaintenanceScreen(
                navigate = {},
                viewModel = mockViewModel
            )
        }

        // Assert
        composeTestRule.onNodeWithText("No maintenance requests").assertIsDisplayed() // Empty message
    }

    @Test
    fun maintenanceRequestsAreDisplayed() {
        // Arrange
        val mockViewModel = mockk<ManagerMaintenanceRequestViewModel>(relaxed = true)
        val mockRequests = listOf(
            MaintenanceRequest(
                id = "1",
                title = "Leaking Pipe",
                resolved = false,
                property = Property(
                    id = "1",
                    imageUrl = listOf("https://example.com/image1.jpg"),
                    name = "Unit A",
                    description = "2 bed, 2 bath",
                    userId = "1",
                    createdAt = "2021-08-01T00:00:00.000Z",
                    updatedAt = "2021-08-01T00:00:00.000Z",
                    bookings = emptyList(),
                    maintenanceRequest = emptyList()
                ),
                imageUrl = listOf("https://example.com/image1.jpg"),
                description = "Pipe in the bathroom is leaking",
                createdAt = "2021-08-01T00:00:00.000Z",
                updatedAt = "2021-08-01T00:00:00.000Z",
                userId = "1",
                propertyId = "1",
                maintenanceRequestUpdates = emptyList()
            ),
            MaintenanceRequest(
                id = "1",
                title = "Broken Window",
                resolved = false,
                property = Property(
                    id = "1",
                    imageUrl = listOf("https://example.com/image1.jpg"),
                    name = "Unit A",
                    description = "2 bed, 2 bath",
                    userId = "1",
                    createdAt = "2021-08-01T00:00:00.000Z",
                    updatedAt = "2021-08-01T00:00:00.000Z",
                    bookings = emptyList(),
                    maintenanceRequest = emptyList()
                ),
                imageUrl = listOf("https://example.com/image1.jpg"),
                description = "Pipe in the bathroom is leaking",
                createdAt = "2021-08-01T00:00:00.000Z",
                updatedAt = "2021-08-01T00:00:00.000Z",
                userId = "1",
                propertyId = "1",
                maintenanceRequestUpdates = emptyList()
            )
        )
        every { mockViewModel.isLoading } returns MutableStateFlow(false)
        every { mockViewModel.maintenanceRequests } returns MutableStateFlow(mockRequests)
        every { mockViewModel.maintenanceError } returns MutableStateFlow(null)
        every { mockViewModel.isRefreshing } returns MutableStateFlow(false)

        // Act
        composeTestRule.setContent {
            ManagerMaintenanceScreen(
                navigate = {},
                viewModel = mockViewModel
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Leaking Pipe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Broken Window").assertIsDisplayed()
    }
}
