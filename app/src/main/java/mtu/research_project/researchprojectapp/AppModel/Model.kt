package mtu.research_project.researchprojectapp.AppModel

import android.graphics.Bitmap

data class Category(
    val name: String,
    val photos: MutableList<CategoryImage>? = mutableListOf(),
    val subCategories: MutableList<Category>? = mutableListOf(),
)

data class CategoryImage(
    val image: Bitmap,
    var imageTitle: String,
    var labelData: LabelData?
)

data class LabelData(
    val labelDataName: String,
    val keys: MutableList<String>? = mutableListOf(),
    val values: MutableList<String>? = mutableListOf()
)


