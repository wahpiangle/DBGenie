package com.example.propdash

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.propdash.components.tenant.TenantGraph
import com.example.propdash.components.tenant.TenantMaintenanceScreen
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.model.Property
import com.example.propdash.viewModel.tenant.TenantMaintenanceViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class TenantMaintenanceScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockViewModel: TenantMaintenanceViewModel
    private val mockNavigate: (String) -> Unit = mockk(relaxed = true)

    @Before
    fun setup() {
        mockViewModel = mockk(relaxed = true)

        every { mockViewModel.maintenanceRequests.value } returns
                    listOf(
                        MaintenanceRequest(
                            id = "1",
                            title = "Leaky Faucet",
                            description = "The kitchen faucet is leaking.",
                            imageUrl = listOf("https://example.com/image1.jpg"),
                            resolved = false,
                            createdAt = "2023-11-01T10:00:00Z",
                            updatedAt = "2023-11-15T14:00:00Z",
                            userId = "user_123",
                            propertyId = "property_456",
                            property = Property(
                                id = "property_456",
                                name = "Sunrise Apartments",
                                description = "Modern apartments with spacious rooms.",
                                imageUrl = emptyList(),
                                createdAt = "2023-01-10T08:00:00Z",
                                updatedAt = "2023-11-20T18:00:00Z",
                                userId = "user_789",
                                bookings = emptyList(),
                                maintenanceRequest = emptyList()
                            ),
                            maintenanceRequestUpdates = emptyList()
                        )
                    )
        every { mockViewModel.maintenanceError.value } returns (null)
        every { mockViewModel.isRefreshing.value } returns (false)
        every { mockViewModel.isLoading.value } returns (false)
    }

    @Test
    fun testMaintenanceRequestsDisplay() {
        composeTestRule.setContent {
            TenantMaintenanceScreen(
                navigate = mockNavigate,
                viewModel = mockViewModel
            )
        }

        // Verify that the top bar is displayed
        composeTestRule.onNodeWithText("Maintenance Requests")
            .assertExists()
            .assertIsDisplayed()

        // Verify that the maintenance request card is displayed
        composeTestRule.onNodeWithText("Leaky Faucet")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Sunrise Apartments")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Unresolved")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testEmptyMaintenanceRequests() {
        // Set ViewModel state to an empty list
        every { mockViewModel.maintenanceRequests.value } returns (emptyList())

        composeTestRule.setContent {
            TenantMaintenanceScreen(
                navigate = mockNavigate,
                viewModel = mockViewModel
            )
        }

        // Verify that "No maintenance requests" message is displayed
        composeTestRule.onNodeWithText("No maintenance requests")
            .assertExists()
            .assertIsDisplayed()
    }


    @Test
    fun testNavigationOnClick() {
        composeTestRule.setContent {
            TenantMaintenanceScreen(
                navigate = mockNavigate,
                viewModel = mockViewModel
            )
        }

        // Simulate click on maintenance card
        composeTestRule.onNodeWithText("Leaky Faucet")
            .performClick()

        // Verify that the navigation function was called
        verify {
            mockNavigate(TenantGraph.TenantMaintenanceDetailScreen.createRoute("1"))
        }
    }
}
