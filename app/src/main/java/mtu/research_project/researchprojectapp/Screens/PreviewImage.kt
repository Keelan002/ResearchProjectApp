package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.Utils.CustomButton
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
                cameraViewModel = cameraViewModel,
                navHController = navHController,
                selectedImage = selectedImage,
                imageTitle = imageTitle
            )
        }
    }





}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = Color.Black
            )
        },
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.White,
            containerColor = backgroundColor,
            focusedIndicatorColor = secondaryColor,
            unfocusedIndicatorColor = secondaryColor,
            disabledIndicatorColor = Color.White,
        ),
    )

}

@Composable
fun PreviewNewPhoto(
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel,
    navHController: NavHostController,
    lastCapturedImage: ImageBitmap,
    imageTitle: String
){

    Image(
        bitmap = lastCapturedImage,
        contentDescription = "last captured image"
    )

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
                appViewModel.selectedImage = lastCapturedImage
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

@Composable
fun EditExistingPhoto(
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel,
    navHController: NavHostController,
    selectedImage: ImageBitmap,
    imageTitle: String
){
    Image(
        bitmap = selectedImage,
        contentDescription = "last captured image"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){

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
                if (imageTitle != ""){
                    navHController.navigate(Screens.CaptureScreen.route)
                }
            }
        )
    }
}