package mtu.research_project.researchprojectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mtu.research_project.researchprojectapp.Screens.AddSubScreen
import mtu.research_project.researchprojectapp.Screens.CameraScreen
import mtu.research_project.researchprojectapp.Screens.CaptureScreen
import mtu.research_project.researchprojectapp.Screens.CheckPermissionScreen
import mtu.research_project.researchprojectapp.Screens.ImageEditorScreen
import mtu.research_project.researchprojectapp.Screens.ImagePreviewScreeContent
import mtu.research_project.researchprojectapp.Screens.ImagePreviewScreen
import mtu.research_project.researchprojectapp.Screens.Screens
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.ScopeID

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel
    private val cameraViewModel: CameraViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                DemoApp(cameraViewModel)
            }
        }
    }

    @Composable
    fun DemoApp(cameraViewModel: CameraViewModel) {
        val navController = rememberNavController()
        Scaffold { innerPadding ->
            NavHost(
                navController,
                modifier = Modifier.padding(innerPadding),
                cameraViewModel = cameraViewModel,
                appViewModel = appViewModel,
            )
        }
    }

    @Composable
    fun NavHost(
        navController: NavHostController,
        modifier: Modifier = Modifier,
        cameraViewModel: CameraViewModel,
        appViewModel: AppViewModel,
    ) {


        androidx.navigation.compose.NavHost(
            navController = navController,
            startDestination = Screens.CaptureScreen.route,
            modifier = modifier
        ) {
            composable(route = Screens.CaptureScreen.route) {
                CaptureScreen(navController, appViewModel, cameraViewModel)
            }
            composable(route = Screens.CameraScreen.route) {
                CameraScreen(cameraViewModel, appViewModel, cameraViewModel, navController)
            }
            composable(route = Screens.MainCameraScreen.route) {
                CheckPermissionScreen(cameraViewModel, appViewModel, navController)
            }
            composable(route = Screens.ImageEditorScreen.route) {
                ImageEditorScreen(appViewModel, navController, cameraViewModel)
            }
            composable(route = Screens.ImagePreviewScreen.route) {
                ImagePreviewScreen(appViewModel, cameraViewModel, navController)
            }
            composable(route = Screens.AddSubScreen.route) {
                AddSubScreen(appViewModel = appViewModel, navHostController = navController)
            }
        }
    }
}