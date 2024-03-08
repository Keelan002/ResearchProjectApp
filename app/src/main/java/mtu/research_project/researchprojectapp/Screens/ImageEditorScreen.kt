package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavController
import mtu.research_project.researchprojectapp.ImageEdit.EditExistingImage
import mtu.research_project.researchprojectapp.ImageEdit.EditTakenPhoto
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel


@Composable
fun ImageEditorScreen(
    appViewModel: AppViewModel,
    navController: NavController,
    cameraViewModel: CameraViewModel
){
    Column(
        modifier = Modifier
            .background(primaryColor)
            .fillMaxSize()
    ) {
        ImageEditorScreenContent(appViewModel, navController, cameraViewModel)
    }
}

@Composable
fun ImageEditorScreenContent(
    appViewModel: AppViewModel,
    navController: NavController,
    cameraViewModel: CameraViewModel
){

    val isEditingExistingImage = appViewModel.isEditingExistingPhoto.value
    val capturedImage = cameraViewModel.state.value.capturedImage?.asImageBitmap()

    if (isEditingExistingImage){
        EditExistingImage(
            selectedImage = appViewModel.selectedImage,
            appViewModel = appViewModel,
            navController = navController
        )
    }else{
        EditTakenPhoto(
            selectedImage = capturedImage,
            appViewModel = appViewModel,
            navController = navController,
            cameraViewModel = cameraViewModel
        )
    }
}






