package mtu.research_project.researchprojectapp.AppModel

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap

data class Category(
    val name: String,
    val photos: MutableList<Bitmap>? = mutableListOf(),
    val subCategories: MutableList<Category>? = mutableListOf()
)


