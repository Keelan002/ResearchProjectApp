package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mtu.research_project.researchprojectapp.Utils.CustomButton
import mtu.research_project.researchprojectapp.Utils.CustomTextField
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel
import androidx.compose.ui.platform.LocalDensity

@Composable
fun ImagePreviewScreen(appViewModel: AppViewModel, cameraViewModel: CameraViewModel, navHController: NavHostController){
    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        ImagePreviewScreeContent(
            appViewModel = appViewModel,
            cameraViewModel = cameraViewModel,
            navHController = navHController
        )
    }
}

@Composable
fun ImagePreviewScreeContent(
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel,
    navHController: NavHostController
){

    var imageTitle by rememberSaveable { mutableStateOf("") }
    val lastCapturedImage = cameraViewModel.state.value.capturedImage?.asImageBitmap()
    val selectedImage = appViewModel.selectedImage
    val isEditingExistingPhoto = appViewModel.isEditingExistingPhoto.value

    CustomTextField(
        value = imageTitle,
        onValueChange = { imageTitle = it },
        label = "enter image title here"
    )


    if (!isEditingExistingPhoto){
        if (lastCapturedImage != null ) {
            PreviewNewPhoto(
                appViewModel = appViewModel,
                cameraViewModel = cameraViewModel,
                navHController = navHController,
                lastCapturedImage = lastCapturedImage,
                imageTitle = imageTitle
            )
        }
    }

    if (isEditingExistingPhoto){
        if (selectedImage != null) {
            EditExistingPhoto(
                appViewModel = appViewModel,
                navHController = navHController,
                selectedImage = selectedImage,
                imageTitle = imageTitle
            )
        }
    }
}



@Composable
fun PreviewNewPhoto(
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel,
    navHController: NavHostController,
    lastCapturedImage: ImageBitmap,
    imageTitle: String
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = lastCapturedImage,
                    contentDescription = "Last captured image",
                    modifier = Modifier
                        .scale(getImageScaleConstraints(
                            maxHeight = 200, maxWidth = 200
                        ))
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ){
            CustomButton(
                text = "Retake photo",
                onClick = {
                    if (appViewModel.selectedCategory.value != null) {
                        navHController.navigate(Screens.MainCameraScreen.route)
                    }
                }
            )

            CustomButton(
                text = "Edit photo",
                onClick = {
                    navHController.navigate(Screens.ImageEditorScreen.route)
                }
            )

            CustomButton(
                text = "Submit",
                onClick = {
                    if (imageTitle != ""){
                        appViewModel.addTitleToList(imageTitle)
                        cameraViewModel.state.value.capturedImage?.let { appViewModel.addPhotoToCategory(it) }
                        navHController.navigate(Screens.CaptureScreen.route)
                    }
                }
            )
        }
    }
}

@Composable
fun EditExistingPhoto(
    appViewModel: AppViewModel,
    navHController: NavHostController,
    selectedImage: ImageBitmap,
    imageTitle: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = selectedImage,
                    contentDescription = "Last captured image",
                    modifier = Modifier
                        .scale(getImageScaleConstraints(
                            maxHeight = 200, maxWidth = 200
                        ))
                )
            }
        }

        /*TO-DO fix bug where photo in not deletd internally*/
        CustomButton(
            text = "Delete photo",
            onClick = {
                appViewModel.removePhotoAndTitle(selectedImage.asAndroidBitmap())
                navHController.navigate(Screens.CaptureScreen.route)
            }
        )

        CustomButton(
            text = "Edit photo",
            onClick = {
                navHController.navigate(Screens.ImageEditorScreen.route)
            }
        )

        CustomButton(
            text = "Submit",
            onClick = {
                if (imageTitle.isNotBlank()) {
                    navHController.navigate(Screens.CaptureScreen.route)
                }
            }
        )
    }
}

@Composable
private fun getImageScaleConstraints(maxWidth: Int, maxHeight: Int): Float {
    // Set the desired maximum size of the image

    val maxWidthValue = LocalDensity.current.run { maxWidth.dp.toPx() }

    val maxHeightValue = LocalDensity.current.run { maxHeight.dp.toPx() }

    // Calculate the scale based on the constraints
    val scale = minOf(maxWidthValue / 200f, maxHeightValue / 200f)

    return if (scale < 1) scale else 1f
}