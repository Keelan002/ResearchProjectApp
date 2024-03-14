package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.Utils.CustomTextField
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel

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


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Black)
    ){
        CustomTextField(
            value = imageTitle,
            onValueChange = { imageTitle = it },
            placeholder = "enter image title here",
            icon = null,
            modifier = Modifier
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
                PreviewExistingPhoto(
                    appViewModel = appViewModel,
                    navHController = navHController,
                    selectedImage = selectedImage,
                    imageTitle = imageTitle
                )
            }
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
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = lastCapturedImage,
                    contentDescription = "Last captured image",
                    modifier = Modifier
                        .scale(getImageScaleConstraints())
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
        ){
            PreviewImageBtns(
                text = "Retake photo",
                onClick = {
                    if (appViewModel.selectedCategory.value != null) {
                        navHController.navigate(Screens.MainCameraScreen.route)
                    }
                }
            )

            PreviewImageBtns(
                text = "Edit photo",
                onClick = {
                    navHController.navigate(Screens.ImageEditorScreen.route)
                }
            )


            PreviewImageBtns(
                text = "Submit",
                onClick = {
                    if (imageTitle.isNotBlank()){
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
fun PreviewExistingPhoto(
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
                        .scale(getImageScaleConstraints())
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ){
            /*TO-DO fix bug where photo in not deletd internally*/
            PreviewImageBtns(
                text = "Delete photo",
                onClick = {
                    appViewModel.removePhotoAndTitle(selectedImage.asAndroidBitmap())
                    navHController.navigate(Screens.CaptureScreen.route)
                }
            )

            PreviewImageBtns(
                text = "Edit photo",
                onClick = {
                    navHController.navigate(Screens.ImageEditorScreen.route)
                }
            )

            PreviewImageBtns(
                text = "Submit",
                onClick = {
                    if (imageTitle.isNotBlank()) {
                        navHController.navigate(Screens.CaptureScreen.route)
                    }
                }
            )
        }
    }
}

@Composable
private fun getImageScaleConstraints(): Float {

    val maxWidthValue = LocalDensity.current.run { 200.dp.toPx() }
    val maxHeightValue = LocalDensity.current.run { 200.dp.toPx() }

    val scale = minOf(maxWidthValue / 200f, maxHeightValue / 200f)

    return if (scale < 1) scale else 1f
}

@Composable
fun PreviewImageBtns(onClick: () -> Unit, text: String){
    Button(
        onClick = onClick ,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
        modifier = Modifier
            .size(120.dp, 60.dp)
            .padding(start = 24.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
        )
    }

    Spacer(modifier = Modifier.width(16.dp))
    
}


