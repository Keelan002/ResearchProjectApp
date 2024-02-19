package mtu.research_project.researchprojectapp.Screens

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissionScreen(cameraViewModel: CameraViewModel, appViewModel: AppViewModel, navController: NavHostController) {

    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    MainContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        cameraViewModel = cameraViewModel,
        appViewModel = appViewModel,
        navController = navController
    )
}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    cameraViewModel: CameraViewModel,
    appViewModel: AppViewModel,
    navController: NavHostController

) {

    if (hasPermission) {
        CameraScreen(cameraViewModel, appViewModel, cameraViewModel, navController)
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}