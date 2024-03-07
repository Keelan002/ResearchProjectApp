package mtu.research_project.researchprojectapp.ImageEdit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.mr0xf00.easycrop.CropState
import com.mr0xf00.easycrop.CropperLoading
import com.mr0xf00.easycrop.ui.ImageCropperDialog
//import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding

@Composable
fun DemoContent(
    cropState: CropState?,
    loadingStatus: CropperLoading?,
    selectedImage: ImageBitmap?,
    modifier: Modifier = Modifier,
) {
    if (cropState != null) {
        ImageCropperDialog(state = cropState)
    }
    if (cropState == null && loadingStatus != null) {
        LoadingDialog(status = loadingStatus)
    }
    Column (
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (selectedImage != null) Image(
            bitmap = selectedImage, contentDescription = null,
            modifier = Modifier.weight(1f)
        )
        else Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)){
            Text("No image selected !")
        }
    }
}