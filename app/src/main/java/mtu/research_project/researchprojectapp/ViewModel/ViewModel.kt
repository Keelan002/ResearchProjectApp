package mtu.research_project.researchprojectapp.ViewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.mr0xf00.easycrop.CropState
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import mtu.research_project.researchprojectapp.AppModel.Category
import mtu.research_project.researchprojectapp.AppModel.CategoryImage
import mtu.research_project.researchprojectapp.CameraX.CameraState
import mtu.research_project.researchprojectapp.Screens.Screens
import mtu.research_project.researchprojectapp.Utils.Dialogs.AddCategoryDialog
import mtu.research_project.researchprojectapp.Utils.Dialogs.AddSubCategoryDialog
import org.koin.android.annotation.KoinViewModel
import java.io.InputStream

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

    private val _listOfTitles: MutableState<List<String>> = mutableStateOf(emptyList())
    val listOfTitles: List<String> get() = _listOfTitles.value

    private val _isEditingExistingPhoto = mutableStateOf(false)

    val  isEditingExistingPhoto: MutableState<Boolean> = _isEditingExistingPhoto

    private val _isViewingSub = mutableStateOf(false)
    val isViewingSub: MutableState<Boolean> = _isViewingSub

    var imageUri by mutableStateOf<Uri?>(null)

    private val bitmap = mutableStateOf<Bitmap?>(null)

    var selectedImage by mutableStateOf<CategoryImage?>(null)

    fun setCategoryImageName(image: CategoryImage, newTitle: String): CategoryImage {
        return image.copy(imageTitle = newTitle)
    }

    fun updateIsViewingSubBool(newValue: Boolean){
        _isViewingSub.value = newValue
    }

    fun updateIsEditingExistingPhotoBool(newValue: Boolean) {
        _isEditingExistingPhoto.value = newValue
    }

    fun addTitleToList(title: String){
        _listOfTitles.value = _listOfTitles.value + title
    }


    fun setSelectedCategory(category: Category?) {
        _selectedCategory.value = category
    }


    private fun createCategory(category: Category) {
        _categories.value = _categories.value + category
    }

    fun addPhotoToCategory(bitmap: Bitmap, title: String) {

        val imageToAdd = CategoryImage(bitmap, title)
        val category = selectedCategory.value
        if (category != null) {
            category.photos?.add(imageToAdd)
        }
    }

    private fun addSubCategoryToCategory(subCategory: Category){
        val category = selectedCategory.value

        if (category != null){
            category.subCategories?.add(subCategory)
        }
    }

    fun replacePhotoInCategory(oldImage: CategoryImage, newImage: CategoryImage) {
        val category = selectedCategory.value
        if (category != null) {
            val photos = category.photos
            if (photos != null) {
                val index = photos.indexOf(oldImage)
                if (index != -1) {
                    photos[index] = newImage
                }
            }
        }
    }

    fun removePhotoAndTitle(selectedPhoto: CategoryImage) {
        val category = selectedCategory.value
        if (category != null) {
            val index = category.photos?.indexOf(selectedPhoto)
            if (index != null && index != -1) {
                val updatedPhotos = category.photos.toMutableList()
                updatedPhotos.removeAt(index)
                _selectedCategory.value = category.copy(photos = updatedPhotos)

                val updatedTitles = _listOfTitles.value.toMutableList()
                updatedTitles.removeAt(index)
                _listOfTitles.value = updatedTitles
            }
        }
    }

    fun loadImageFromUriAsBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.use {
                BitmapFactory.decodeStream(it)
            }
        } catch (e: Exception) {
            null
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
                }
            )
        }
    }

    @Composable
    fun RunAddSubCategoryDialog(navHostController: NavHostController) {
        AddSubCategoryDialog(
            onDismiss = { hideAddSubCategoryDialog() },
            onAddCategory = { category ->
                addSubCategoryToCategory(category)
                hideAddSubCategoryDialog()
                navHostController.navigate(Screens.CaptureScreen.route)
            },
            appViewModel = this
        )
    }

    @Composable
    fun RunImageCropperDialog(
        cropState: CropState?,
    ) {
        if (cropState != null) {
            ImageCropperDialog(state = cropState)
        }
    }


}

