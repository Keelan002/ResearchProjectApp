package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel

@Composable
fun AddSubScreen(appViewModel: AppViewModel, navHostController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        appViewModel.RunAddSubCategoryDialog(navHostController)
    }
}