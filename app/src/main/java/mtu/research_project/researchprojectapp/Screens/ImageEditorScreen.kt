package mtu.research_project.researchprojectapp.Screens

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import mtu.research_project.researchprojectapp.R

@Composable
fun ImageEditorScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ImageEditorScreenContent()
    }
}

@Composable
fun ImageEditorScreenContent(){
    var photo by remember { mutableStateOf<Bitmap?>(null) }
    var croppedPhoto by remember { mutableStateOf<Bitmap?>(null) }
    var rotation by remember { mutableStateOf(0f) }
    var colorFilter by remember { mutableStateOf<ColorFilter?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Display Photo
        Image(
            painter = painterResource(id = R.drawable.o_donnels_crisps),
            contentDescription = "O'Donnels Crisps",
            modifier = Modifier
                .size(300.dp)
                .rotate(rotation)
        )


        // Rotate Photo
        Slider(
            value = rotation,
            onValueChange = { rotation = it },
            valueRange = 0f..360f
        )
        
        // Save Photo
        Button(onClick = { /* Save photo */ }) {
            Text(text ="Save Photo")
        }
    }
}