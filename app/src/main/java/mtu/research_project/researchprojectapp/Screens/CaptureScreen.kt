package mtu.research_project.researchprojectapp.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import mtu.research_project.researchprojectapp.Theme.primaryColor
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.Utils.CustomButton
import mtu.research_project.researchprojectapp.Utils.CustomTextField
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel


@Composable
fun CaptureScreen(
    navController: NavHostController,
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CaptureScreenContent(navController, appViewModel, cameraViewModel)
    }

    appViewModel.RunAddCategoryDialog()
    appViewModel.RunAddSubCategoryDialog(appViewModel)

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CaptureScreenContent(
    navHController: NavHostController,
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel
) {


    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val permissionState = cameraPermissionState.status.isGranted
    val onRequestPermission = cameraPermissionState::launchPermissionRequest

    val titles = appViewModel.listOfTitles
    val selectedCategory by appViewModel.selectedCategory.observeAsState()
    val updatedSelectedCategory = rememberUpdatedState(selectedCategory)
    val context = LocalContext.current
    var text by rememberSaveable { mutableStateOf("") }
    var buttonClicked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(

                modifier = Modifier
                    .size(width = 411.dp, height = 64.dp),

                title = {
                        CustomTextField(
                            value = text,
                            onValueChange = { text = it },
                            placeholder = "Search"
                        )
                },


                colors = TopAppBarDefaults.topAppBarColors(containerColor = secondaryColor),

                navigationIcon = {
                    if (appViewModel.selectedCategory.value != null){
                        IconButton(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .size(width = 48.dp, height = 48.dp),
                            onClick = {
                            appViewModel.setSelectedCategory(null)
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size( width = 24.dp, height = 24.dp ),
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back", tint = Color.Black
                            )
                        }
                    }
                },


                actions = {
                    Row {
                        IconButton(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .size(width = 48.dp, height = 48.dp),
                            onClick = {
                                if (!appViewModel.isViewingSub.value){
                                    appViewModel.showAddCategoryDialog()
                                }else{
                                    appViewModel.showAddSubCategoryDialog()
                                }
                            }
                        ){
                            Icon(
                                modifier = Modifier

                                    .size( width = 24.dp, height = 24.dp ),
                                imageVector = Icons.Default.Add,
                                contentDescription = "add category icon"
                            )
                        }

                        if (appViewModel.isViewingSub.value){
                            PickImageFromGallery(
                                context = context,
                                appViewModel = appViewModel,
                                navHController = navHController,
                                cameraViewModel = cameraViewModel
                            )
                        }

                    }
                }
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
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                )

                if(appViewModel.selectedCategory.value != null && buttonClicked){
                    PickImageFromGallery(
                        context = context,
                        appViewModel = appViewModel,
                        navHController = navHController,
                        cameraViewModel = cameraViewModel
                       )
                }



                if (appViewModel.selectedCategory.value == null){
                    DisplayCategories(appViewModel)
                }else{
                    DisplaySubCategories(appViewModel)
                }

                DisplayImages(titles, appViewModel, navHController)

            }
        },

        floatingActionButton = {
            CapturePhotoBtn(appViewModel = appViewModel, navHController = navHController)
        },

    )
}

@Composable
fun DisplayCategories(appViewModel: AppViewModel){
    LazyColumn{
        items(appViewModel.categories){category ->
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                text = category.name,
                onClick = {
                    appViewModel.updateIsViewingSubBool(true)
                    appViewModel.setSelectedCategory(category)
                    Log.d("SELECTED CATEGORY", "${appViewModel.selectedCategory.value}")
                },
            )
        }
    }
}

@Composable
fun DisplaySubCategories(appViewModel: AppViewModel) {
    val selectedCategory = appViewModel.selectedCategory.value ?: return

    LazyColumn {
        items(selectedCategory.subCategories ?: emptyList()) { category ->
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                text = category.name,
                onClick = { appViewModel.setSelectedCategory(category) },
            )
        }
    }
}

@Composable
fun DisplayImages(titles: List<String>, appViewModel: AppViewModel, navHController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    )  {
        itemsIndexed(titles) { index, title ->
            Column {
                Text(
                    text = title,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
                val photo = appViewModel.selectedCategory.value?.photos?.getOrNull(index)
                photo?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable {
                                appViewModel.updateIsEditingExistingPhotoBool(true)
                                appViewModel.selectedImage = photo.asImageBitmap()
                                navHController.navigate(Screens.ImagePreviewScreen.route)
                            }
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(4.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun PickImageFromGallery(
    context: Context,
    appViewModel: AppViewModel,
    navHController: NavHostController,
    cameraViewModel: CameraViewModel
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        appViewModel.updateIsEditingExistingPhotoBool(false)
        val bitmap = uri?.let { appViewModel.loadImageFromUriAsBitmap(context, it) }
        cameraViewModel.updateCapturedPhotoState(bitmap)
        navHController.navigate(Screens.ImagePreviewScreen.route)
    }

    //FloatingActionButton(
    //    onClick = { launcher.launch("image/*") },
    //    modifier = Modifier
    //        .size(400.dp, 50.dp)
    //        .padding(start = 15.dp)
    //        .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
    //    contentColor = Color.Black,
    //    containerColor = secondaryColor
    //) {
    //    Text(text = "Pick an image ")
    //}

    IconButton(
        modifier = Modifier
            .padding(top = 10.dp)
            .size(width = 48.dp, height = 48.dp),
        onClick = {
            launcher.launch("image/*")
        }
    ) {
        Icon(
            modifier = Modifier

                .size(width = 24.dp, height = 24.dp),
            imageVector = Icons.Default.ArrowUpward,
            contentDescription = "add category icon"
        )
    }
}

@Composable
fun CapturePhotoBtn(appViewModel: AppViewModel, navHController: NavHostController){
    if (appViewModel.selectedCategory.value != null){
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
            containerColor = secondaryColor
        ) {
            Text("Capture photo")
        }
    }
}



