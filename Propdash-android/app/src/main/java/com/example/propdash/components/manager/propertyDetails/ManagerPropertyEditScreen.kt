package com.example.propdash.components.manager.propertyDetails

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.example.propdash.components.manager.ManagerScreen
import com.example.propdash.components.manager.createProperty.ImageItem
import com.example.propdash.components.manager.createProperty.InputField
import com.example.propdash.components.manager.shared.BottomNavBar
import com.example.propdash.ui.theme.dark
import com.example.propdash.ui.theme.light
import com.example.propdash.ui.theme.primary
import com.example.propdash.viewModel.manager.ManagerPropertyDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerPropertyEditScreen(
    navigate: (String) -> Unit,
    viewModel: ManagerPropertyDetailViewModel,
) {
    val property = viewModel.property.collectAsState()
    var propertyImages = remember(property.value) {
        mutableStateListOf<ImageItem>()
    }
    val context = LocalContext.current
    val name = remember(property.value) { mutableStateOf(property.value?.name ?: "") }
    val description = remember(property.value) { mutableStateOf(property.value?.description ?: "") }
    val rentalPerMonth =
        remember(property.value) { mutableStateOf(property.value?.rentalPerMonth ?: "") }
    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var rentalPerMonthError by remember { mutableStateOf(false) }
    val recomposeToggleState = remember { mutableStateOf(false) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { it ->
            propertyImages = mutableStateListOf(
                *it.map { ImageItem.FromUri(it) }.toTypedArray()
            )
            recomposeToggleState.value = !recomposeToggleState.value //hardcode to trigger recompose
        }
    Scaffold(
        bottomBar = {
            BottomNavBar(
                ManagerScreen.ManagerPropertyScreen.route,
                navigate
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Property",
                        color = light,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigate(
                            ManagerScreen.ManagerPropertyScreen.route
                        )
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = light
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = dark)
            )
        },
        containerColor = dark
    ) { padding ->
        if (property.value == null) {
            CircularProgressIndicator(
                color = primary,
            )
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            InputField(
                "Name",
                name.value,
                {
                    name.value = it;
                    nameError = name.value.isEmpty()
                },
                nameError
            )
            InputField(
                "Description",
                description.value, {
                    description.value = it;
                    descriptionError = description.value.isEmpty()
                },
                descriptionError
            )
            InputField(
                "Rental per Month",
                rentalPerMonth.value,
                {
                    rentalPerMonth.value = it;
                    rentalPerMonthError =
                        rentalPerMonth.value.isEmpty() || rentalPerMonth.value.toDoubleOrNull() == null
                },
                rentalPerMonthError,
                true
            )
            Text("Images", color = light, modifier = Modifier.padding(vertical = 16.dp))
            if (propertyImages.size == 0) {
                IconButton(
                    onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    modifier = Modifier
                        .width(420.dp)
                        .height(250.dp)
                        .border(1.dp, primary, RoundedCornerShape(8.dp))
                ) {
                    Icon(Icons.Filled.AddCircleOutline, "Add Image", tint = light)
                }
            } else {
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { propertyImages.size },
                    modifier = Modifier
                        .width(420.dp)
                        .height(250.dp),
                    preferredItemWidth = 420.dp,
                    itemSpacing = 8.dp
                ) { index ->
                    val image = propertyImages[index]
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(image.asUriOrString())
                                    .build(),
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                        IconButton(
                            onClick = {
                                propertyImages.removeAt(index)
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Remove Image",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    viewModel.updateProperty(
                        name = name.value,
                        description = description.value,
                        rentalPerMonth = rentalPerMonth.value,
                        imageItemList = propertyImages,
                        updateImage = propertyImages.size > 0,
                        context = context
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primary)
            ) {
                Text("Update Property")
            }

//            hardcode to trigger recompose
            Text(
                recomposeToggleState.value.toString(),
                color = Color.Transparent,
            )
        }

    }

}