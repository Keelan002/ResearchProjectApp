package mtu.research_project.researchprojectapp.ViewModel

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageProcessor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.navigation.NavHostController
import com.mr0xf00.easycrop.CropState
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import mtu.research_project.researchprojectapp.AppModel.Category
import mtu.research_project.researchprojectapp.AppModel.CategoryImage
import mtu.research_project.researchprojectapp.CameraX.CameraState
import mtu.research_project.researchprojectapp.R
import mtu.research_project.researchprojectapp.Screens.Screens
import mtu.research_project.researchprojectapp.Utils.Dialogs.AddCategoryDialog
import mtu.research_project.researchprojectapp.Utils.Dialogs.AddSubCategoryDialog
import org.koin.android.annotation.KoinViewModel
import java.io.File
import java.io.InputStream
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

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

    private val _currentSelectedCategory = MutableLiveData<Category?>()
    var currentSelectedCategory: LiveData<Category?> = _currentSelectedCategory

    private val _allCategories: MutableState<List<Category>> = mutableStateOf(emptyList())
    private val allCategories: List<Category> get() = _allCategories.value

    private val _topLvlCategories: MutableState<List<Category>> = mutableStateOf(emptyList())
    val topLvlCategories: List<Category> get() = _topLvlCategories.value

    private val _categoryNavigationStack: MutableState<List<Category>> = mutableStateOf(emptyList())
    val categoryNavigationStack: List<Category> get() = _categoryNavigationStack.value

    private val _isEditingExistingPhoto = mutableStateOf(false)

    val  isEditingExistingPhoto: MutableState<Boolean> = _isEditingExistingPhoto

    private val _isViewingSub = mutableStateOf(false)
    val isViewingSub: MutableState<Boolean> = _isViewingSub

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    var selectedImage by mutableStateOf<CategoryImage?>(null)

    private var imagePath: String? = null

    fun setImagePath(path: String?) {
        imagePath = path
    }

    private val _filteredCategories = MutableLiveData<List<Category>>(emptyList())
    val filteredCategories: LiveData<List<Category>> get() = _filteredCategories


    fun setSearchQuery(query: String) {
        Log.d("SEARCH QUERY", query)
        Log.d("FILTERED CATE", "${_filteredCategories.value}")
        _searchQuery.value = query
        updateFilteredCategories()
        removeNonMatchingCategories(query)
    }

    fun addCatgeoryToNavStack(category: Category) {
        _categoryNavigationStack.value += category
    }

    fun removeLastCategory() {
        if (_categoryNavigationStack.value.isNotEmpty()) {
            _categoryNavigationStack.value = _categoryNavigationStack.value.dropLast(1)
        }
    }

    fun getLastCategory(): Category? {
        return categoryNavigationStack.lastOrNull()
    }

    fun setCategoryImageName(image: CategoryImage, newTitle: String): CategoryImage {
        return image.copy(imageTitle = newTitle)
    }

    fun updateIsViewingSubBool(newValue: Boolean){
        _isViewingSub.value = newValue
    }

    fun updateIsEditingExistingPhotoBool(newValue: Boolean) {
        _isEditingExistingPhoto.value = newValue
    }

    fun setSelectedCategory(category: Category?) {
        _currentSelectedCategory.value = category
    }


    private fun createCategory(category: Category) {
        _topLvlCategories.value = _topLvlCategories.value + category
    }

    fun addPhotoToCategory(bitmap: Bitmap, title: String) {

        val imageToAdd = CategoryImage(bitmap, title)
        val category = currentSelectedCategory.value
        if (category != null) {
            category.photos?.add(imageToAdd)
        }
    }

    private fun addSubCategoryToCategory(subCategory: Category){
        val category = currentSelectedCategory.value

        if (category != null){
            category.subCategories?.add(subCategory)
        }
    }

    fun replacePhotoInCategory(oldImage: CategoryImage, newImage: CategoryImage) {
        val category = currentSelectedCategory.value
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
        val category = currentSelectedCategory.value
        if (category != null) {
            val index = category.photos?.indexOf(selectedPhoto)
            if (index != null && index != -1) {
                val updatedPhotos = category.photos.toMutableList()
                updatedPhotos.removeAt(index)
                _currentSelectedCategory.value = category.copy(photos = updatedPhotos)
                Log.d("Category", "$category")
            }
        }
    }

    fun deletePhotoByTitle(imageTitle: String) {
        val selectedCategory = currentSelectedCategory.value ?: return

        val photoIndex = selectedCategory.photos?.indexOfFirst { it.imageTitle == imageTitle }

        if (photoIndex != -1) {
            if (photoIndex != null) {
                selectedCategory.photos.removeAt(photoIndex)
            }
        }
    }


    private fun updateFilteredCategories() {
        val lowercaseQuery = _searchQuery.value?.lowercase()?.trim()
        Log.d("LOWERCSSE", "$lowercaseQuery")
        if (lowercaseQuery != null) {
            _filteredCategories.value = if (lowercaseQuery.isEmpty()) {
                emptyList()
            } else {
                allCategories.filter { category ->
                    category.name.lowercase().startsWith(lowercaseQuery)
                }
            }
        }
    }

    private fun removeNonMatchingCategories(query: String) {
        val lowercaseQuery = query.lowercase().trim()

        if (lowercaseQuery.isEmpty()){
            _filteredCategories.value = emptyList()
        }else{
            _filteredCategories.value = _filteredCategories.value?.filter { category ->
                category.name.lowercase().contains(lowercaseQuery)
            }
        }
    }

    private fun addAllCategories(categories: List<Category>) {
        val allCategoriesList = mutableListOf<Category>()
        categories.forEach { category ->
            allCategoriesList.add(category)
            if (category.subCategories?.isNotEmpty() == true) {
                category.subCategories.let { allCategoriesList.addAll(it) }
            }
        }
        _allCategories.value = allCategoriesList
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
                    addAllCategories(topLvlCategories)
                }
            )
        }
    }

    @Composable
    fun RunAddSubCategoryDialog(navHostController: NavHostController) {
        AddSubCategoryDialog(
            onDismiss = { navHostController.navigate(Screens.CaptureScreen.route) },
            onAddCategory = { category ->
                addSubCategoryToCategory(category)
                hideAddSubCategoryDialog()
                addAllCategories(topLvlCategories)
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


    fun countSubCategoriesAndImages(category: Category): Int {
        var count = 0

        count += category.subCategories?.size!!

        count += category.photos?.size!!

        for (subCategory in category.subCategories) {
            count += countSubCategoriesAndImages(subCategory)
        }

        return count
    }


    private fun uploadImage(imageFile: File): Response {
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                name = "file",
                filename = imageFile.name,
                body = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            )
            .build()

        val request = Request.Builder()
            .url("http://nutriscan.pythonanywhere.com/extract")
            .post(requestBody)
            .build()

        return client.newCall(request).execute()
    }

    private fun parseResponse(responseBody: String): Map<String, Double> {
        val jsonObject = JSONObject(responseBody)
        val dataObject = jsonObject.getJSONObject("data")
        val resultMap = mutableMapOf<String, Double>()

        val keys = dataObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = dataObject.getDouble(key)
            resultMap[key] = value
        }

        return resultMap
    }

    suspend fun hitApi(){
        val imageFile = imagePath?.let { File(it) }
        val response = imageFile?.let { uploadImage(it) }

        Log.d("IMAGE PATH", "$response")

        if (response != null) {
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("RESPONE BODY" , "$responseBody")
                val parsedData = responseBody?.let { parseResponse(it) }
                println("Extracted Data: $parsedData")
            } else {
                println("Error: ${response.code} - ${response.message}")
            }
        }else{
            println("RESPONE IS NULL")
        }
    }

    fun test(){
        R.drawable.o_donnels_crisps
    }

}

