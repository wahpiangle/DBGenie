package com.example.propdash

import android.content.Context
import android.net.Uri
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.data.model.User
import com.example.propdash.data.repository.PropertyRepository
import com.example.propdash.viewModel.manager.ManagerCreatePropertyViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MultipartBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class ManagerCreatePropertyViewModelTest {

    @get:Rule
    val rule = createComposeRule()

    private val propertyRepository = mockk<PropertyRepository>()
    private val userSession = mockk<User>(relaxed = true)
    private val navigate = mockk<(String) -> Unit>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)
    private lateinit var viewModel: ManagerCreatePropertyViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = ManagerCreatePropertyViewModel(userSession, navigate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun createPropertyTest() = runTest {
        // Arrange
        val name = "Test Property"
        val description = "This is a test property description."
        val uri = mockk<Uri>()
        val imageItem = ImageItem.FromUri(uri)
        val mockFile = mockk<File>(relaxed = true)
        val multipartBody = mockk<MultipartBody.Part>(relaxed = true)

        val preparedFiles = listOf(multipartBody)

        coEvery {
            propertyRepository.createProperty(
                userSession.cookie,
                name,
                description,
                userSession.id,
                preparedFiles
            )
        } returns Response.success(
            mockk(relaxed = true)
        )
    }

    @Test
    fun createProperty_failure_with_exception() = runBlocking {
        // Arrange
        val name = "Test Property"
        val description = "This is a test property description."
        val uri = mockk<Uri>()
        val imageItem = ImageItem.FromUri(uri)

        coEvery {
            propertyRepository.createProperty(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } throws Exception("Network error")

        // Act
        viewModel.createProperty(name, description, listOf(imageItem), context)

        // Assert
        assertEquals(
            "There was an error creating the property. Please try again.",
            viewModel.createPropertyError.value
        )
    }
}
