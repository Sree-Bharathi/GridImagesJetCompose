package com.sree.imagelistapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sree.imagelistapplication.categories
import com.sree.imagelistapplication.network.Photos
import com.sree.imagelistapplication.network.RestApisService
import com.sree.imagelistapplication.di.RetrofitModule
import com.sree.imagelistapplication.local.PhotosEntity
import com.sree.imagelistapplication.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {
    private val apiKey = "OPZQAtdSFauDsuizXp6CePWA7tIoQtc9jaNg6nNwnXJhNmRV8b5inCFE"
    val pexelsApi = RetrofitModule.getInstance().create(RestApisService::class.java)
    private val _photos= MutableStateFlow<List<Photos>> (emptyList())
    private val _photosEntity= MutableStateFlow<List<PhotosEntity>> (emptyList())
    val photos:StateFlow<List<Photos>> = _photos
    val photosEntity:StateFlow<List<PhotosEntity>> = _photosEntity
    private val is_loading_m = MutableStateFlow(false)
    val is_loading:StateFlow<Boolean> = is_loading_m

    fun fetchPhotos() {
        viewModelScope.launch {
            is_loading_m.value=true
            try {
                val response = pexelsApi.searchPhotos(apiKey, categories, 10)
                if (response.isSuccessful) {
                    val photos = response.body()?.photos ?: emptyList()
                    photos.forEach {
                        println("Photo: ${it.url}, Photographer: ${it.photographer}")
                        _photos.value = response.body()?.photos ?: emptyList()

                        repository.insert(tophotoEntitiy(it, categories) )
                    }
                } else {
                    println("API Error: ${response.errorBody()?.string()}")
                }
            } catch (e: IOException) {
                println("Network Error: ${e.message}")
            } catch (e: HttpException) {
                println("HTTP Error: ${e.message}")
            }
            finally {
                is_loading_m.value=false
            }
        }
    }

    fun tophotoEntitiy(photos: Photos,category:String):PhotosEntity{
        return PhotosEntity(photos.id,photos.width,photos.height,photos.src?.original,photos.photographer,photos.photographerUrl,photos.photographerId,photos.avgColor,photos.src.toString(),photos.liked,photos.alt,category)
    }
    fun getPhotos()
    {
     viewModelScope.launch (IO){
       repository.getAllPhotos().collectLatest {_photosEntity.tryEmit(it)  }
     }
    }
    fun getCategoryPhotos(category: String)
    {
     viewModelScope.launch (IO){
       repository.getPhotosOnCategory(category).collectLatest {_photosEntity.tryEmit(it)  }
     }
    }
}