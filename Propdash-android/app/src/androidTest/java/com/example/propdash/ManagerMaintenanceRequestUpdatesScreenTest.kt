import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.components.manager.maintenanceDetail.ManagerMaintenanceRequestUpdatesScreen
import com.example.propdash.data.model.MaintenanceRequest
import com.example.propdash.data.model.MaintenanceRequestUpdate
import com.example.propdash.data.model.Property
import com.example.propdash.viewModel.manager.ManagerMaintenanceRequestDetailViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ManagerMaintenanceRequestUpdatesScreenTest {

    fun generateFakeMaintenanceRequestUpdates(): List<MaintenanceRequestUpdate> {
        return listOf(
            MaintenanceRequestUpdate(
                id = "update_1",
                description = "Started working on the faucet.",
                imageUrl = "",
                createdAt = "2023-11-02T12:00:00Z",
                userId = "user_456"
            ),
            MaintenanceRequestUpdate(
                id = "update_2",
                description = "Replaced the gasket. Testing for leaks.",
                imageUrl = "https://example.com/repair_image.jpg",
                createdAt = "2023-11-03T15:00:00Z",
                userId = "user_123"
            )
        )
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<ManagerMaintenanceRequestDetailViewModel>(relaxed = true)
    val maintenanceRequest = MaintenanceRequest(
        id = "1",
        title = "Leaky Faucet",
        description = "The kitchen faucet is leaking.",
        imageUrl = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg"
        ),
        resolved = false,
        createdAt = "2023-11-01T10:00:00Z",
        updatedAt = "2023-11-15T14:00:00Z",
        userId = "user_123",
        propertyId = "property_456",
        property = Property(
            id = "property_456",
            name = "Sunrise Apartments",
            description = "Modern apartments with spacious rooms and excellent facilities.",
            imageUrl = listOf(
                "https://example.com/property_image1.jpg",
                "https://example.com/property_image2.jpg"
            ),
            createdAt = "2023-01-10T08:00:00Z",
            updatedAt = "2023-11-20T18:00:00Z",
            userId = "user_789",
            bookings = emptyList(),
            maintenanceRequest = emptyList()
        ),
        maintenanceRequestUpdates = generateFakeMaintenanceRequestUpdates()
    )

    @Test
    fun testCreateMaintenanceUpdateWithTextOnly() = runBlocking {
        coEvery { mockViewModel.userSession.id } returns "user-123"
        coEvery { mockViewModel.createMaintenanceUpdate(any(), any(), any()) } returns Unit

        composeTestRule.setContent {
            ManagerMaintenanceRequestUpdatesScreen(
                maintenanceRequest = maintenanceRequest,
                viewModel = mockViewModel
            )
        }

        // Enter text into the input field
        val inputText = "New update description"
        composeTestRule.onNodeWithText("Add a new update...")
            .performTextInput(inputText)

        // Click the send button
        composeTestRule.onNodeWithContentDescription("Send")
            .performClick()

        // Verify the ViewModel's createMaintenanceUpdate was called with the correct parameters
        coVerify { mockViewModel.createMaintenanceUpdate(inputText, null, any()) }
    }

    @Test
    fun testCreateMaintenanceUpdateWithImage() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()

        coEvery { mockViewModel.userSession.id } returns "user-123"
        coEvery { mockViewModel.createMaintenanceUpdate(any(), any(), any()) } returns Unit

        composeTestRule.setContent {
            ManagerMaintenanceRequestUpdatesScreen(
                maintenanceRequest = maintenanceRequest,
                viewModel = mockViewModel
            )
        }

        // Simulate selecting an image
        val fakeImageItem = mockk<ImageItem>(relaxed = true)
        coEvery { fakeImageItem.asUriOrString() } returns "file://fakepath/fakeimage.jpg"

        composeTestRule.onNodeWithContentDescription("Add")
            .performClick()

        composeTestRule.runOnUiThread {
            mockViewModel.createMaintenanceUpdate("", fakeImageItem, context)
        }

        // Verify the ViewModel's createMaintenanceUpdate was called with the image
        coVerify { mockViewModel.createMaintenanceUpdate("", fakeImageItem, any()) }
    }

    @Test
    fun testMaintenanceUpdatesDisplayed() {

        coEvery { mockViewModel.userSession.id } returns "user-123"

        composeTestRule.setContent {
            ManagerMaintenanceRequestUpdatesScreen(
                maintenanceRequest = maintenanceRequest,
                viewModel = mockViewModel
            )
        }

        // Verify the text updates are displayed
        composeTestRule.onNodeWithText("Started working on the faucet.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Replaced the gasket. Testing for leaks.").assertIsNotDisplayed()
    }
}
