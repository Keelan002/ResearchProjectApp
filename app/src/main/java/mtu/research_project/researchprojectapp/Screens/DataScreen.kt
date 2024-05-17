package mtu.research_project.researchprojectapp.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mtu.research_project.researchprojectapp.AppModel.LabelData
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel

/**
 * set up of data screen
 *
 * @param appViewModel apps viewmodel
 * @param navController for navigation
 */
@Composable
fun DataScreen(appViewModel: AppViewModel, navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        DataScreenContent(appViewModel)
    }
}

/**
 * data screen content where extracted label data is displayed
 *
 * @param appViewModel apps viewmodel
 */
@Composable
fun DataScreenContent(appViewModel: AppViewModel) {

    Column (
        modifier = Modifier
            .fillMaxWidth()
    ){

        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Button(
                onClick = {
                    Log.d("IS CLICKED", "TRUE")
                    appViewModel.setLabelData(appViewModel.setData)
                    /*Log.d("SELECTED IMAGE LABEL DATA",
                        appViewModel.selectedImage?.labelData.toString()
                    )*/
                },

            ) {
                Text(
                    text = "Another Destination",
                    color = Color.White
                )
            }

            Text(
                text = "Scanned data for ${appViewModel.selectedImage?.imageTitle}",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }


       appViewModel.setData = DisplayData(appViewModel = appViewModel)


    }
}

/**
 * used to format the display of the label data
 *
 * @param appViewModel apps viewmodel
 * @return the label data
 */
@Composable
fun DisplayData(appViewModel: AppViewModel): LabelData?{
    val cleanData = appViewModel.cleanData
    val keysList = cleanData?.keys?.toMutableList()
    val valuesList = cleanData?.values?.toMutableList()

    // Check if cleanData is not null
    if (cleanData != null) {
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

                    var keyData by rememberSaveable { mutableStateOf(keysList[i]) }
                    var valuesData by rememberSaveable { mutableStateOf(valuesList?.get(i) ?: "") }

                    // Create a Row for each key-value pair
                    Row {
                        // TextField for the key
                        TextField(
                            value = keyData,
                            onValueChange = {
                                keyData = it
                                keysList[i] = keyData
                                Log.d("KEYSLIST", "$keysList")
                                Log.d("SETDATA", "${appViewModel.setData}")
                            },
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f), // Occupy half of the available space
                        )

                        // TextField for the value
                        TextField(
                            value = valuesData, // Display the value
                            onValueChange = {
                                valuesData = it
                                valuesList?.set(i, valuesData)
                            },
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f), // Occupy half of the available space
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
    val newLabelData = appViewModel.selectedImage?.imageTitle?.let {
        LabelData(
        labelDataName = it,
        keys = keysList,
        values = valuesList
        )
    }
    return newLabelData
}

