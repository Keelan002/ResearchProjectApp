package mtu.research_project.researchprojectapp.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel

@Composable
fun CaptureScreen(navController: NavHostController, appViewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CaptureScreenContent(navController, appViewModel)
    }

    appViewModel.RunAddCategoryDialog()
    appViewModel.RunAddSubCategoryDialog(appViewModel)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CaptureScreenContent(navHController: NavHostController, appViewModel: AppViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isViewingSub by remember { mutableStateOf(false) }

    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val permissionState = cameraPermissionState.status.isGranted
    val onRequestPermission = cameraPermissionState::launchPermissionRequest


    val selectedCategory by appViewModel.selectedCategory.observeAsState()
    val updatedSelectedCategory = rememberUpdatedState(selectedCategory)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    if (isViewingSub){
                        GoBackToCategoriesBtn(appViewModel)
                    }


                    Text(
                        text = "App Name",
                        )

                    if(appViewModel.selectedCategory.value == null){
                        isViewingSub = false
                        AddCategoryTopAppBarBtn(appViewModel)
                    }else{
                        isViewingSub = true
                        AddSubCategoryTopAppBarBtn(appViewModel)
                    }

                },
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
                Text(
                    text = "Please select a category",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )


                if (appViewModel.selectedCategory.value == null){
                    DisplayCategories(appViewModel)
                }
                else{
                    DisplaySubCategories(appViewModel)
                }

                DisplayImages(appViewModel)

            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (appViewModel.selectedCategory.value != null) {
                        navHController.navigate(Screens.MainCameraScreen.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                contentColor = Color.Black,
                containerColor = if (updatedSelectedCategory.value == null) Color.Gray else secondaryColor
            ) {
                Text("Capture photo")
            }
        }
    )
}

@Composable
fun PrimaryTabRowDemo(selectedTabIndex: Int, onTabSelected: (Int) -> Unit, navController: NavHostController) {
    val periodLabels = listOf(
        "Capture",
        "Gallery",
    )


    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                PrimaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                )
            }
        },
        backgroundColor = secondaryColor
    ) {
        periodLabels.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                    if (index == 0)
                        navController.navigate(Screens.CaptureScreen.route)
                    else
                        navController.navigate(Screens.GalleryScreen.route)
                },
                text = {
                    Text(
                        text = title
                    )
                }
            )
        }
    }
}

@Composable
fun PrimaryIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    height: Dp = 2.dp
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(color)
    )
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp)
    ) {

        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(start = 32.dp)
        )
    }
}

@Composable
fun DisplayCategories(appViewModel: AppViewModel){
    LazyColumn{
        items(appViewModel.categories){category ->
            CustomButton(
                text = category.name,
                onClick = {
                    appViewModel.setSelectedCategory(category)
                    Log.d("SELECTED CATEGORY", "${appViewModel.selectedCategory.value}")
                },
                appViewModel = appViewModel
            )
        }
    }
}

@Composable
fun DisplaySubCategories(appViewModel: AppViewModel){
    LazyColumn{
        items(appViewModel.subCategories){subCategory ->
            CustomButton(
                text = subCategory.name,
                onClick = {
                  appViewModel.setSelectedSubCategory(subCategory)
                },
                appViewModel = appViewModel
            )

        }
    }
}

@Composable
fun AddCategoryTopAppBarBtn(appViewModel: AppViewModel){
    Button(
        modifier = Modifier
            .padding(start = 250.dp),
        onClick = {
            appViewModel.showAddCategoryDialog()
        },
        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),

        ) {
        Text(
            text = "add category + ",
            color = Color.Black
        )
    }
}

@Composable
fun AddSubCategoryTopAppBarBtn(appViewModel: AppViewModel){
    Button(
        modifier = Modifier
            .padding(start = 250.dp),
        onClick = {
            appViewModel.showAddSubCategoryDialog()
        },
        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),

        ) {
        Text(
            text = "add subcategory + ",
            color = Color.Black
        )
    }
}

@Composable
fun DisplayImages(appViewModel: AppViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(appViewModel.selectedCategory.value?.photos?.size ?: 0) { index ->
            val photo = appViewModel.selectedCategory.value?.photos?.get(index)
            photo?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(4.dp),
                )
            }
        }
    }
}

@Composable
fun GoBackToCategoriesBtn(appViewModel: AppViewModel){
    Button(
        onClick = {
            appViewModel.setSelectedCategory(null)
        },
        colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),
    ){
        Text(
            text = "go back"
        )
    }
}



