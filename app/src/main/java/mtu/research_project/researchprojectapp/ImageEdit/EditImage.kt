package mtu.research_project.researchprojectapp.ImageEdit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.navigation.NavController
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import mtu.research_project.researchprojectapp.Screens.Screens
import mtu.research_project.researchprojectapp.ViewModel.AppViewModel

@Composable
fun EditExistingImage(
    selectedImage: ImageBitmap?,
    appViewModel: AppViewModel,
    navController: NavController
) {
    val imageCropper = rememberImageCropper()
    var error by remember { mutableStateOf<CropError?>(null) }

    LaunchedEffect(selectedImage) {
        if (selectedImage != null) {
            when (val result = imageCropper.crop(maxResultSize = null, bmp = selectedImage)) {
                CropResult.Cancelled -> {}
                is CropError -> error = result
                is CropResult.Success -> {

                    appViewModel.replacePhotoInCategory(
                        oldImage = selectedImage.asAndroidBitmap(),
                        newImage = result.bitmap.asAndroidBitmap()
                    )

                    appViewModel.selectedImage = result.bitmap


                    navController.navigate(Screens.ImagePreviewScreen.route)
                }
                else -> {}
            }
        }
    }

    appViewModel.RunImageCropperDialog(
        cropState = imageCropper.cropState,
    )
}
