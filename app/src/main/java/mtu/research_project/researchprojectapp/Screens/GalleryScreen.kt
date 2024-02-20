package mtu.research_project.researchprojectapp.Screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import mtu.research_project.researchprojectapp.AppModel.Category
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import java.util.Optional

@Composable
fun GalleryScreen(navHController: NavHostController, appViewModel: AppViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GalleryScreenContent(navHController = navHController, appViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GalleryScreenContent(navHController: NavHostController, appViewModel: AppViewModel){
    var selectedTabIndex by remember { mutableStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "App name") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = secondaryColor),
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp)
                    .background(primaryColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PrimaryTabRowDemo(
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { tabIndex ->
                            selectedTabIndex = tabIndex
                        },
                        navController = navHController
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 30.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    DisplayCategoriesInGallery(
                        appViewModel, navHController
                    )
                }

            }
        }
    )
}

@Composable
fun CustomBox(
    text: String,
    navHController: NavHostController,
    appViewModel: AppViewModel,
    category: Category
) {

    Box(
        modifier = Modifier
            .clickable {
                appViewModel.setSelectedCategory(category)
                Log.d("SELECTED CATEGORY IN GALLERY", "${appViewModel.selectedCategory.value}")
                //navHController.navigate(Screens.ImageScreen.route)
            }
            .height(100.dp)
            .background(secondaryColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

@Composable
fun DisplayCategoriesInGallery(appViewModel: AppViewModel, navHController: NavHostController){
    LazyColumn{
        items(appViewModel.categories){category ->
            appViewModel.selectedCategory.value?.let {
                CustomButton(
                    text = category.name,
                    onClick = {
                        appViewModel.setSelectedCategory(category)
                        Log.d("SELECTED CATEGORY", "${appViewModel.selectedCategory.value}")
                        navHController.navigate(Screens.ImageScreen.route)
                    }
                )
            }
        }
    }
}



