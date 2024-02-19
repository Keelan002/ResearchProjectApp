package mtu.research_project.researchprojectapp.AppModel

import android.graphics.Bitmap

data class Categories(
    val categories: ArrayList<Category>?
)

data class Category(
    val name: String,
    val photos: MutableList<Bitmap>? = mutableListOf()
)