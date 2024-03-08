package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import mtu.research_project.researchprojectapp.ImageEdit.EditExistingImage
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel


@Composable
fun ImageEditorScreen(appViewModel: AppViewModel, navController: NavController){
    Column(
        modifier = Modifier
            .background(primaryColor)
            .fillMaxSize()
    ) {
        ImageEditorScreenContent(appViewModel, navController)
    }
}

@Composable
fun ImageEditorScreenContent(appViewModel: AppViewModel, navController: NavController){


    val isEditingExistingImage = appViewModel.isEditingExistingPhoto.value

    if (isEditingExistingImage){
        EditExistingImage(
            selectedImage = appViewModel.selectedImage,
            appViewModel = appViewModel,
            navController = navController
        )
    }



}






