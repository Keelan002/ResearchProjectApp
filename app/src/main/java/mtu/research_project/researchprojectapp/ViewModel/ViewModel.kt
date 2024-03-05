package mtu.research_project.researchprojectapp.ViewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import mtu.research_project.researchprojectapp.CameraX.CameraState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.CropState
import com.mr0xf00.easycrop.CropperLoading
import com.mr0xf00.easycrop.ImageCropper
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.rememberImagePicker
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mtu.research_project.researchprojectapp.AppModel.Category
import mtu.research_project.researchprojectapp.Utils.AddCategoryDialog
import mtu.research_project.researchprojectapp.Utils.AddSubCategoryDialog
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CameraViewModel : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }

    @Composable
    fun SimpleDemo( selectedImage: ImageBitmap? ) {

        var error by remember { mutableStateOf<CropError?>(null) }
        val imageCropper = rememberImageCropper()
        val scope = rememberCoroutineScope()


        if (selectedImage != null){
            scope.launch {
                when (val result = imageCropper.crop(maxResultSize = null, bmp = selectedImage)) {
                    is CropResult.Cancelled -> {
                        imageCropper.cropState?.done(true)
                        Log.d("TEST CANCEL EDIT", "SUCCESS")
                    }
                    is CropError -> error = result
                    is CropResult.Success -> {
                        updateCapturedPhotoState(result.bitmap.asAndroidBitmap())
                        imageCropper.cropState?.done(true)
                        hideEditImageDialog()
                        Log.d("TEST CROP SUCCESS", "SUCCESS")
                    }
                }
            }
        }

        DemoContent(cropState = imageCropper.cropState)

        error?.let { CropErrorDialog(it, onDismiss = { error = null }) }
    }

    @Composable
    fun DemoContent(
        cropState: CropState?,
    ) {
        if (cropState != null && uiState.showEditImageDialog) {
                ImageCropperDialog(state = cropState)
            }
    }

    @Composable
    fun CropErrorDialog(error: CropError, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = { Button(onClick = onDismiss) { Text("Ok") } },
            text = { Text(text = "ERROR") }
        )
    }

    fun showEditImageDialog(){
        uiState = uiState.copy(showEditImageDialog = true)
    }

    fun hideEditImageDialog(){
        uiState = uiState.copy(showEditImageDialog = false)
    }
}


data class UiState(
    val showAddCategoryDialog: Boolean = false,
    val showAddSubCategoryDialog: Boolean = false,
    val showEditImageDialog: Boolean = false
)

var uiState by mutableStateOf(UiState())
    private set

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedCategory = MutableLiveData<Category?>()
    var selectedCategory: LiveData<Category?> = _selectedCategory

    private val _categories: MutableState<List<Category>> = mutableStateOf(emptyList())
    val categories: List<Category> get() = _categories.value

    var imageUri by mutableStateOf<Uri?>(null)

    val bitmap = mutableStateOf<Bitmap?>(null)


    fun setSelectedCategory(category: Category?) {
        _selectedCategory.value = category
    }


    private fun createCategory(category: Category) {
        _categories.value = _categories.value + category
    }

    fun addPhotoToCategory(bitmap: Bitmap) {
        val category = selectedCategory.value
        if (category != null) {
            category.photos?.add(bitmap)
            Log.d("PHOTOS IN CATEGORY", "${category.photos}")
        }
    }

    fun addPhotoFromGallery(context: Context){
        if (imageUri != null) {
            bitmap.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val src = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                ImageDecoder.decodeBitmap(src)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
            bitmap.value?.let { bitmap ->
                addPhotoToCategory(bitmap)
            }
        }
    }

    private fun addSubCategoryToCategory(subCategory: Category){
        val category = selectedCategory.value
        if (category != null){
            category.subCategories?.add(subCategory)
        }
    }

    fun showAddCategoryDialog() {
        uiState = uiState.copy(showAddCategoryDialog  = true)
    }

    fun hideAddCategoryDialog() {
        uiState = uiState.copy(showAddCategoryDialog = false)
    }

    fun showAddSubCategoryDialog(){
        uiState = uiState.copy(showAddSubCategoryDialog = true)
    }

    fun hideAddSubCategoryDialog(){
        uiState = uiState.copy(showAddSubCategoryDialog = false)
    }

    @Composable
    fun RunAddCategoryDialog(){
        if (uiState.showAddCategoryDialog){
            AddCategoryDialog(
                onDismiss = { hideAddCategoryDialog() },
                onAddCategory = { category ->
                    createCategory(category)
                    hideAddCategoryDialog()
                    Log.d("LISTED CATEGORIES", "$categories")
                }
            )
        }
    }

    @Composable
    fun RunAddSubCategoryDialog(appViewModel: AppViewModel) {
        if (uiState.showAddSubCategoryDialog) {
            AddSubCategoryDialog(
                onDismiss = { hideAddSubCategoryDialog() },
                onAddCategory = { category ->
                    //createCategory(category)
                    addSubCategoryToCategory(category)
                    hideAddSubCategoryDialog()
                    Log.d("SELECTED CATEGORY", "${appViewModel.selectedCategory.value}")
                    Log.d("SELECTED CATEGORY SUBS", "${appViewModel.selectedCategory.value!!.subCategories}")
                },
            )
        }
    }


}

