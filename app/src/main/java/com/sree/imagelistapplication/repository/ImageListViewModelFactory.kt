package com.sree.imagelistapplication.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sree.imagelistapplication.viewModel.ImageListViewModel

class ImageListViewModelFactory
    (private val repository: SearchRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageListViewModel::class.java)) {
            return ImageListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}