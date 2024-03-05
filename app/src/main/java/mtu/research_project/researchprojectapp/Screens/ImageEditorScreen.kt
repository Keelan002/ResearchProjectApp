package mtu.research_project.researchprojectapp.Screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.mr0xf00.easycrop.rememberImageCropper
import mtu.research_project.researchprojectapp.R
import mtu.research_project.researchprojectapp.Theme.blackWhite
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.Theme.red
import mtu.research_project.researchprojectapp.Theme.yellow
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel


@Composable
fun ImageEditorScreen( cameraViewModel: CameraViewModel){
    Column(
        modifier = Modifier
            .background(primaryColor)
            .fillMaxSize()
    ) {
        ImageEditorScreenContent(cameraViewModel)
    }
}

@Composable
fun ImageEditorScreenContent(cameraViewModel: CameraViewModel){
    var photo by remember { mutableStateOf<Bitmap?>(null) }
    var croppedPhoto by remember { mutableStateOf<Bitmap?>(null) }
    var rotation by remember { mutableStateOf(0f) }
    var colorFilter by remember { mutableStateOf<Color?>(null) }
    
    var activateEdit by remember { mutableStateOf<Boolean>(false) }



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Display Photo
        Image(
            painter = painterResource(id = R.drawable.o_donnels_crisps),
            contentDescription = "O'Donnels Crisps",
            colorFilter = if (colorFilter == Color.Black) {
                ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
            } else {
                colorFilter?.let { ColorFilter.tint(color = it, BlendMode.Darken) }
            },
            modifier = Modifier
                .size(300.dp)
                .rotate(rotation)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 80.dp, top = 30.dp)
                .align(Alignment.CenterHorizontally)
        ){
            ChangeColorBtn(
                color = blackWhite,
                onClick = { colorFilter = Color.Black }
            )
            Spacer(modifier = Modifier.width(32.dp))
            ChangeColorBtn(
                color = red,
                onClick = { colorFilter = red }
            )
            Spacer(modifier = Modifier.width(32.dp))
            ChangeColorBtn(
                color = yellow,
                onClick = { colorFilter = yellow }
            )
            Spacer(modifier = Modifier.width(32.dp))
            ChangeColorBtn(
                color = Color.Black,
                onClick = { cameraViewModel.showEditImageDialog() }
            )
        }

        Slider(
            value = rotation,
            onValueChange = { rotation = it },
            valueRange = 0f..360f,
            modifier = Modifier
                .padding(start = 30.dp, end = 30.dp)
        )

    }

    cameraViewModel.SimpleDemo( selectedImage = cameraViewModel.state.value.capturedImage?.asImageBitmap() )


    
}

@Composable
fun ChangeColorBtn(color: Color, onClick: () -> Unit) {
    Button(
        onClick =  onClick ,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {

    }
}



