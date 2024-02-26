package mtu.research_project.researchprojectapp.AppModel

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap

data class Categories(
    val categories: ArrayList<Category>?
)

data class Category(
    val name: String,
    val photos: MutableList<Bitmap>? = mutableListOf(),
    val subCategories: MutableList<SubCategory>? = mutableListOf()
)

data class SubCategory(
    val name: String,
    val photos: MutableList<ImageBitmap>? = mutableListOf()
)