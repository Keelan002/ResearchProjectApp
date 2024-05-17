package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavController
import mtu.research_project.researchprojectapp.ImageEdit.EditExistingImage
import mtu.research_project.researchprojectapp.ImageEdit.EditTakenPhoto
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel

/**
 * set up of image editor screen
 *
 * @param appViewModel apps viewmodel
 * @param navController used for navigation
 * @param cameraViewModel camera viewmodel
 */
@Composable
fun ImageEditorScreen(
    appViewModel: AppViewModel,
    navController: NavController,
    cameraViewModel: CameraViewModel
){
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        ImageEditorScreenContent(appViewModel, navController, cameraViewModel)
    }
}

/**
 * image editor screen content displays one of two
 * functions dependant on isEditingExistingImage bool
 *
 * @param appViewModel apps viewmodel
 * @param navController for navigation
 * @param cameraViewModel camera viewmodel
 */
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






