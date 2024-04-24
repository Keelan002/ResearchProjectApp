package mtu.research_project.researchprojectapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun DataScreenContent(appViewModel: AppViewModel) {

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){

        Text(
            text = "Scanned data for ${appViewModel.selectedImage?.imageTitle}",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        DisplayData(appViewModel = appViewModel)
    }
}

@Composable
fun DisplayData(appViewModel: AppViewModel){
    val cleanData = appViewModel.cleanData

    // Check if cleanData is not null
    if (cleanData != null) {
        // Convert keys and values to lists
        val keysList = cleanData.keys?.toList()
        val valuesList = cleanData.values?.toList()

        // Check if keysList is not null
        if (keysList != null) {
            // Column with vertical scrolling
            Column(
                modifier = Modifier
                    .verticalScroll(
                        state = rememberScrollState()
                    )
            ) {
                // Iterate over the indices of the lists
                for (i in keysList.indices) {
                    // Create a Row for each key-value pair
                    Row {
                        // TextField for the key
                        TextField(
                            value = keysList[i], // Display the key
                            onValueChange = { /* No action for now */ },
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f), // Occupy half of the available space
                            readOnly = true // Make the TextField read-only
                        )

                        // TextField for the value
                        TextField(
                            value = "${valuesList?.get(i)}", // Display the value
                            onValueChange = { /* No action for now */ },
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f), // Occupy half of the available space
                            readOnly = true // Make the TextField read-only
                        )
                    }
                }
            }
        }
    } else {
        // Handle the case when cleanData is null
        Text(
            text = "No data available",
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}