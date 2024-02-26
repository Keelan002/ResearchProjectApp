package mtu.research_project.researchprojectapp.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import mtu.research_project.researchprojectapp.CameraX.CameraState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import mtu.research_project.researchprojectapp.AppModel.Categories
import mtu.research_project.researchprojectapp.AppModel.Category
import mtu.research_project.researchprojectapp.AppModel.SubCategory
import mtu.research_project.researchprojectapp.Utils.AddCategoryDialog
import mtu.research_project.researchprojectapp.Utils.AddSubCategoryDialog
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CameraViewModel : ViewModel() {

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }
}

data class UiState(
    val showAddCategoryDialog: Boolean = false,
    val showAddSubCategoryDialog: Boolean = false
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    var isExpanded by mutableStateOf(false)
        private set

    fun setIsExpanded(value: Boolean) {
        isExpanded = value
    }

    private val _selectedCategory = MutableLiveData<Category?>()
    var selectedCategory: LiveData<Category?> = _selectedCategory

    private val _selectedSubCategory = MutableLiveData<SubCategory?>()
    var selectedSubCategory: LiveData<SubCategory?> = _selectedSubCategory

    private val _categories: MutableState<List<Category>> = mutableStateOf(emptyList())
    val categories: List<Category> get() = _categories.value

    private val _subCategories: MutableState<List<SubCategory>> = mutableStateOf(emptyList())
    val subCategories: List<SubCategory> get() = _subCategories.value

    var uiState by mutableStateOf(UiState())
        private set

    fun setSelectedCategory(category: Category?) {
        _selectedCategory.value = category
    }

    fun setSelectedSubCategory(subCategory: SubCategory){
        _selectedSubCategory.value = subCategory
    }

    private fun createSubCategory(subCategory: SubCategory){
        _subCategories.value = _subCategories.value + subCategory
        val category = selectedCategory.value
        if (category != null){
            category.subCategories?.add(subCategory)
        }
    }

    private fun createCategory(category: Category) {
        _categories.value = _categories.value + category
    }

    fun addPhotoToCategory(bitmap: ImageBitmap) {
        val category = selectedCategory.value
        if (category != null) {
            category.photos?.add(bitmap)
        }
    }

    // Function to retrieve photos from a specific category
    fun getPhotosFromCategory(categoryName: String, categories: Categories): List<ImageBitmap> {
        val category = categories.categories?.find { it.name == categoryName }
        return category?.photos ?: emptyList()
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
                onAddCategory = { subCategory ->
                    createSubCategory(subCategory)
                    hideAddSubCategoryDialog()
                    Log.d("SELECTED CATEGORY", "${appViewModel.selectedCategory.value}")
                    Log.d("SELECTED CATEGORY SUBS", "${appViewModel.selectedCategory.value!!.subCategories}")
                },
            )
        }
    }
}