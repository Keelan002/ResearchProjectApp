package mtu.research_project.researchprojectapp.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import mtu.research_project.researchprojectapp.Theme.secondaryColor
import mtu.research_project.researchprojectapp.Utils.CategoryBox
import mtu.research_project.researchprojectapp.Utils.FilteredCustomTextField
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

    val selectedCategory by appViewModel.currentSelectedCategory.observeAsState()
    val searchQuery by appViewModel.searchQuery.observeAsState()

    val updatedSelectedCategory = rememberUpdatedState(selectedCategory)
    val context = LocalContext.current
    var text by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .size(width = 411.dp, height = 50.dp),
                title = {
                    (if (!appViewModel.isViewingSub.value) "APP NAME" else appViewModel.getLastCategory()?.name)?.let {
                        Text(
                            fontSize = 30.sp,
                            text = it,
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 20.dp, top = 10.dp)
                        )
                    }
                },


                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),

                navigationIcon = {
                    if (appViewModel.categoryNavigationStack.isNotEmpty()){
                        IconButton(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .size(width = 48.dp, height = 48.dp),
                            onClick = {
                                appViewModel.removeLastCategory()
                                val lastCategory = appViewModel.getLastCategory()
                                if (lastCategory != null) {
                                appViewModel.setSelectedCategory(lastCategory)
                                }else{
                                    appViewModel.setSelectedCategory(null)
                                    appViewModel.isViewingSub.value = false
                                }
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size( width = 24.dp, height = 24.dp ),
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back", tint = Color.Cyan
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
                                    navHController.navigate(Screens.AddSubScreen.route)
                                }
                            }
                        ){
                            Icon(
                                modifier = Modifier
                                    .size( width = 24.dp, height = 24.dp ),
                                imageVector = Icons.Default.Add,
                                contentDescription = "add category icon",
                                tint = Color.Cyan
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
                    .background(Color.Black)
            ) {


                FilteredCustomTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = "Search",
                    icon = Icons.Default.Search,
                    modifier = Modifier,
                    appViewModel = appViewModel
                )



                if (!searchQuery.isNullOrEmpty()){
                    DisplayFilteredCategories(appViewModel)
                }else{
                    if (appViewModel.currentSelectedCategory.value == null || appViewModel.categoryNavigationStack.isEmpty()){
                        DisplayCategories(appViewModel)
                    }else{
                        DisplaySubCategoriesAndImages(
                            appViewModel = appViewModel,
                            navHController = navHController
                        )
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                if (appViewModel.isViewingSub.value){
                    PickImageFromGallery(
                        context = context,
                        appViewModel = appViewModel,
                        navHController = navHController,
                        cameraViewModel = cameraViewModel
                    )
                    CapturePhotoBtn(
                        appViewModel = appViewModel,
                        navHController = navHController
                    )
                }
            }
        }
    )
}


@Composable
fun DisplayCategories(appViewModel: AppViewModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 0.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(appViewModel.topLvlCategories) { category ->
            CategoryBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = category.name,
                onClick = {
                    appViewModel.updateIsViewingSubBool(true)
                    appViewModel.setSelectedCategory(category)
                    appViewModel.addCatgeoryToNavStack(category)
                    Log.d("NAV STACK", "${appViewModel.categoryNavigationStack}")
                    Log.d("SELECTED CATEGORY", "${appViewModel.currentSelectedCategory.value}")
                },
                fileCount = appViewModel.countSubCategoriesAndImages(category)
            )
        }
    }
}

@Composable
fun DisplaySubCategoriesAndImages(
    appViewModel: AppViewModel,
    navHController: NavHostController
) {
    val selectedCategory = appViewModel.getLastCategory()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 0.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(selectedCategory?.subCategories ?: emptyList()) { category ->
            CategoryBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = category.name,
                onClick = {
                    appViewModel.setSelectedCategory(category)
                    appViewModel.addCatgeoryToNavStack(category)
                    Log.d("NAV STACK", "${appViewModel.categoryNavigationStack}")
                    Log.d("SELECTED CATEGORY", "${appViewModel.currentSelectedCategory.value}")
                },
                fileCount = appViewModel.countSubCategoriesAndImages(category)
            )
        }

        items(selectedCategory?.photos ?: emptyList()) { photo ->
            Column {
                Text(
                    text = photo.imageTitle,
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
                Image(
                    bitmap = photo.image.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clickable {
                            appViewModel.updateIsEditingExistingPhotoBool(true)
                            appViewModel.selectedImage = photo
                            navHController.navigate(Screens.ImagePreviewScreen.route)
                        }
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(4.dp)
                )
            }
        }
    }
}


@Composable
fun DisplayFilteredCategories(appViewModel: AppViewModel) {
    val filteredCategories by appViewModel.filteredCategories.observeAsState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 0.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(filteredCategories ?: emptyList()) { category ->
            CategoryBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = category.name,
                onClick = {

                },
                fileCount = appViewModel.countSubCategoriesAndImages(category)
            )
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
        if (uri != null){
            appViewModel.updateIsEditingExistingPhotoBool(false)
            val bitmap = uri.let { appViewModel.loadImageFromUriAsBitmap(context, it) }
            cameraViewModel.updateCapturedPhotoState(bitmap)
            navHController.navigate(Screens.ImagePreviewScreen.route)
        }
    }
    FloatingActionButton(
        onClick = {
            launcher.launch("image/*")
        },
        modifier = Modifier
            .size(width = 200.dp, height = 56.dp),
        containerColor = Color.Black
    ) {
        Column {
            Icon(
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = secondaryColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Upload photo",
                color = secondaryColor
            )
        }
    }
}

@Composable
fun CapturePhotoBtn(appViewModel: AppViewModel, navHController: NavHostController){
    FloatingActionButton(
        onClick = {
            if (appViewModel.currentSelectedCategory.value != null) {
                navHController.navigate(Screens.MainCameraScreen.route)
            }
        },
        modifier = Modifier
            .size(width = 200.dp, height = 56.dp),
        containerColor = Color.Black
    ) {
        Column {
            Icon(
                imageVector = Icons.Default.CameraEnhance,
                contentDescription = null,
                tint = secondaryColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Capture photo",
                color = secondaryColor
            )
        }

    }
}






