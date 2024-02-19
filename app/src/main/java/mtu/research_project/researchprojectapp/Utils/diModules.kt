package mtu.research_project.researchprojectapp.Utils

import androidx.lifecycle.ViewModel
import mtu.research_project.researchprojectapp.ViewModel.CameraViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CameraViewModel() }
}
