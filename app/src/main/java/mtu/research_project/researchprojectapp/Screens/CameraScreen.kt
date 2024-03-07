package mtu.research_project.researchprojectapp.Screens

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mtu.research_project.researchprojectapp.Utils.rotateBitmap
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.Executor
import mtu.research_project.researchprojectapp.CameraX.CameraState

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = koinViewModel(),
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel,
    navController: NavHostController,
) {
    val cameraState: CameraState by viewModel.state.collectAsStateWithLifecycle()

    CameraContent(
        lastCapturedPhoto = cameraState.capturedImage,
        appViewModel = appViewModel,
        navController = navController,
        cameraViewModel = cameraViewModel
    )
}

@Composable
private fun CameraContent(
    lastCapturedPhoto: Bitmap? = null,
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel,
    navController: NavHostController
) {

    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Take photo") },
                onClick = {
                    capturePhoto(
                        context = context,
                        cameraController = cameraController,
                        appViewModel = appViewModel,
                        cameraViewModel = cameraViewModel
                    )
                },
                icon = { Icon(imageVector = Icons.Default.Face, contentDescription = "Camera capture icon") }
            )
            ExtendedFloatingActionButton(
                text = { Text(text = "Back") },
                onClick = {
                    navController.navigate(Screens.CaptureScreen.route)
                },
                icon = { Icon(imageVector = Icons.Default.Face, contentDescription = "Camera capture icon") }
            )
        }
    ) { paddingValues: PaddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        setBackgroundColor(0xFF00B6CB.toInt())
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
        }
    }
}


private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    appViewModel: AppViewModel,
    cameraViewModel: CameraViewModel,
) {

    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val correctedBitmap: Bitmap = image
                .toBitmap()
                .rotateBitmap(image.imageInfo.rotationDegrees)

            cameraViewModel.updateCapturedPhotoState(correctedBitmap)
            cameraViewModel.state.value.capturedImage?.let { appViewModel.addPhotoToCategory(it) }
            image.close()

        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

