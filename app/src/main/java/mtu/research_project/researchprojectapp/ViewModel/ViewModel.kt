package mtu.research_project.researchprojectapp.ViewModel

import android.app.Application
import android.graphics.Bitmap
import androidx.camera.core.CameraState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import mtu.research_project.researchprojectapp.AppModel.Categories
import mtu.research_project.researchprojectapp.AppModel.Category
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CameraViewModel : ViewModel() {

   /* private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun updateCapturedPhotoState(updatedPhoto: Bitmap?) {
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatedPhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }*/
}

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val _selectedCategory = MutableLiveData<Category?>()
    var selectedCategory: LiveData<Category?> = _selectedCategory


    //var categories by mutableStateOf<List<Category>>(emptyList())

    val categories = Categories(categories = ArrayList())

    fun setSelectedCategory(category: Category?) {
        _selectedCategory.value = category
    }

    fun createCategory(category: Category, categories: Categories) {
        categories.categories?.add(category)
    }

    // Function to add a photo to a specific category
    fun addPhotoToCategory(categoryName: String, bitmap: Bitmap) {
        val category = categories.categories?.find { it.name == categoryName }
        category?.photos?.add(bitmap)
    }

    // Function to retrieve photos from a specific category
    fun getPhotosFromCategory(categoryName: String): List<Bitmap> {
        val category = categories.categories?.find { it.name == categoryName }
        return category?.photos ?: emptyList()
    }

}