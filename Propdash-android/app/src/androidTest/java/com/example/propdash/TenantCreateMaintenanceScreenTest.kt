package com.example.propdash

import android.content.Context
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.components.tenant.TenantCreateMaintenanceScreen
import com.example.propdash.data.model.Property
import com.example.propdash.viewModel.tenant.TenantMaintenanceViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TenantCreateMaintenanceScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockNavigate: (String) -> Unit = mockk(relaxed = true)
    private val mockViewModel: TenantMaintenanceViewModel = mockk(relaxed = true)

    @Before
    fun setup() {
        // Mock the properties flow to provide dummy data
        val properties = listOf(
            Property(
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
            Property(
                id = "property_789",
                name = "Sunset Apartments",
                description = "Classic apartments with a view.",
                imageUrl = emptyList(),
                createdAt = "2023-01-10T08:00:00Z",
                updatedAt = "2023-11-20T18:00:00Z",
                userId = "user_789",
                bookings = emptyList(),
                maintenanceRequest = emptyList()
            )
        )
        every { mockViewModel.propertiesOfTenant.value } returns (properties)
    }

    @Test
    fun testInputValidation_displaysErrorsWhenFieldsAreEmpty() {
        composeTestRule.setContent {
            TenantCreateMaintenanceScreen(navigate = mockNavigate, viewModel = mockViewModel)
        }

        composeTestRule.onAllNodesWithText("Create Maintenance Request")[1].performClick()

        composeTestRule.onNodeWithText("Title").assertExists() // Check title field
        composeTestRule.onNodeWithText("Images").assertExists() // Check images section
        composeTestRule.onNodeWithText("Select Property").assertExists() // Check property selection

        composeTestRule.onNodeWithText("This field is required").assertExists()
    }

    @Test
    fun testCreateMaintenanceRequest_callsViewModelMethod() {
        // Prepare inputs
        val title = "Leaking Faucet"
        val description = "The faucet in the bathroom is leaking."
        val propertyId = "1"
        val images = listOf(ImageItem.FromUri(mockk())) // Mock image URIs
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeTestRule.setContent {
            TenantCreateMaintenanceScreen(navigate = mockNavigate, viewModel = mockViewModel)
        }

        // Enter input values
        composeTestRule.onNodeWithText("Title").performTextInput(title)
        composeTestRule.onNodeWithText("Description").performTextInput(description)
        composeTestRule.onNodeWithText("Select Property").performClick()
        composeTestRule.onNodeWithText("Sunrise Apartments").performClick()
        composeTestRule.onAllNodesWithText("Create Maintenance Request")[1].performClick()

        // Add image simulation
        composeTestRule.runOnUiThread {
            mockViewModel.createMaintenanceRequest(description, title, images, propertyId, context)
        }

        verify {
            mockViewModel.createMaintenanceRequest(
                description = description,
                title = title,
                images = images,
                propertyId = propertyId,
                context = context
            )
        }
    }
}
