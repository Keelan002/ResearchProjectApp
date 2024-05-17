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

/**
 * content of the check permission screen
 *
 * @param cameraViewModel camera viewmodel
 * @param appViewModel apps viewmodel
 * @param navController for navigation
 */
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

/**
 * used to check if app has permission to use the camera
 *
 * @param hasPermission bool to see if app has permission
 * @param onRequestPermission lambda for functionality on request
 * @param cameraViewModel camera viewmodel
 * @param appViewModel apps viewmodel
 * @param navController for navigation
 */
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