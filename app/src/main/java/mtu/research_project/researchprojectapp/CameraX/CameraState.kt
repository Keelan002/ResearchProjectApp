package mtu.research_project.researchprojectapp.CameraX

import android.graphics.Bitmap

/**
 * used to track the camera state
 *
 * @param capturedImage the most recently captured image
 */
data class CameraState(
    val capturedImage: Bitmap? = null,
)