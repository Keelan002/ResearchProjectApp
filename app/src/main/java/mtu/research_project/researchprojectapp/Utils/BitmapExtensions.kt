package mtu.research_project.researchprojectapp.Utils

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * used to rotate the image
 *
 * @param rotationDegrees the degree of rotation
 *
 * @return the new bitmap
 */
fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}