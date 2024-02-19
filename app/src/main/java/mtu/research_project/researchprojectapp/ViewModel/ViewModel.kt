package mtu.research_project.researchprojectapp.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import mtu.research_project.researchprojectapp.CameraX.CameraState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import mtu.research_project.researchprojectapp.AppModel.Categories
import mtu.research_project.researchprojectapp.AppModel.Category
import mtu.research_project.researchprojectapp.Utils.AddCategoryDialog
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

data class StudyUiState(
    val showAddCategoryDialog: Boolean = false,
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedCategory = MutableLiveData<Category?>()
    var selectedCategory: LiveData<Category?> = _selectedCategory

    private val _categories: MutableState<List<Category>> = mutableStateOf(emptyList())
    val categories: List<Category> get() = _categories.value

    var uiState by mutableStateOf(StudyUiState())
        private set

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
        }
    }

    // Function to retrieve photos from a specific category
    fun getPhotosFromCategory(categoryName: String, categories: Categories): List<Bitmap> {
        val category = categories.categories?.find { it.name == categoryName }
        return category?.photos ?: emptyList()
    }

    fun showAddCategoryDialog() {
        uiState = uiState.copy(showAddCategoryDialog  = true)
    }

    fun hideAddCategoryDialog() {
        uiState = uiState.copy(showAddCategoryDialog = false)
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
}