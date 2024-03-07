package mtu.research_project.researchprojectapp.Screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mr0xf00.easycrop.rememberImageCropper
import mtu.research_project.researchprojectapp.ImageEdit.SimpleDemo
import mtu.research_project.researchprojectapp.R
import mtu.research_project.researchprojectapp.Theme.blackWhite
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.Theme.red
import mtu.research_project.researchprojectapp.Theme.yellow
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel


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


        SimpleDemo(
            modifier = Modifier,
            selectedImage = appViewModel.selectedImage,
            appViewModel = appViewModel,
            navController = navController
        )

}






