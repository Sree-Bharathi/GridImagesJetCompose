package com.sree.imagelistapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sree.imagelistapplication.network.Photos
import com.sree.imagelistapplication.repository.SearchRepository
import com.sree.imagelistapplication.utills.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val searchRepository: SearchRepository ) : ViewModel(){

        private val _uiState = MutableStateFlow(ImageList.UiState())
    val uiState: StateFlow<ImageList.UiState> get() =_uiState.asStateFlow()

    fun getImages( category :String){
            viewModelScope.launch {
               val response = searchRepository.getImages(category,10)
                ImageList.UiState(isloading = true)
                if(response.isSuccess)
                {
                    _uiState.update { ImageList.UiState(data = response.getOrThrow()) }
                }
                else{
                    _uiState.update { ImageList.UiState(error = UiText.RemoteString(response.exceptionOrNull()?.localizedMessage.toString())) }
                }


            }
    }


     fun onEvent(event: ImageList.Event)
     {
         when(event)
         {
             is ImageList.Event.getphotoEvent -> {
                 getImages(event.category)
             }
         }
     }
}

object ImageList{
     data class UiState(
         val isloading :Boolean = false,
         val error : UiText = UiText.Idle,
         val data : List<Photos> ?=null
     )

    sealed interface  Navigation{
        data class gotoPhotoDetails(val path:String) : Navigation
    }
    sealed interface Event{

        data class getphotoEvent(val category :String):Event
    }
}