package mtu.research_project.researchprojectapp.Screens

sealed class Screens (val route: String){
    object CaptureScreen: Screens("capture_screen")
    object CameraScreen: Screens("camera_screen")
    object NoPermissionScreen: Screens("no_permission_screen")
    object MainCameraScreen: Screens("main_camera_screen")
    object CheckPermissionScreen: Screens("check_permission_screen")
    object ImageEditorScreen: Screens("image_editor_screen")
    object ImagePreviewScreen: Screens("image_preview_screen")
    object AddSubScreen: Screens("add_sub_screen")
}