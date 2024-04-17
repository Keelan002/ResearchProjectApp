package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel

@Composable
fun DataScreen(appViewModel: AppViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        DataScreenContent(appViewModel)
    }
}

@Composable
fun DataScreenContent(appViewModel: AppViewModel){
    Column {
        Text(
            text = appViewModel.cleanData.toString(),
            color = Color.White
        )
    }
}