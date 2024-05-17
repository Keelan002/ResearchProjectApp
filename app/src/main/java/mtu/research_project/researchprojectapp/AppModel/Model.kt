package mtu.research_project.researchprojectapp.AppModel

import android.graphics.Bitmap

/**
 * a category data object
 *
 * @param name the name of the category
 * @param photos the list of photos a category has
 * @param subCategories the list of subcategories a category has
 */
data class Category(
    val name: String,
    val photos: MutableList<CategoryImage>? = mutableListOf(),
    val subCategories: MutableList<Category>? = mutableListOf(),
)

/**
 * a category image object
 *
 * @param image the image bitmap
 * @param imageTitle the title of the image
 * @param labelData the extracted data associated with the image
 */
data class CategoryImage(
    val image: Bitmap,
    var imageTitle: String,
    var labelData: LabelData?
)

/**
 * the extracted text as label data object
 *
 * @param labelDataName the name gievn
 *
 * @param keys the key from each data point
 * @param values the vlaues associated with each key
 */
data class LabelData(
    val labelDataName: String,
    val keys: MutableList<String>? = mutableListOf(),
    val values: MutableList<String>? = mutableListOf()
)


