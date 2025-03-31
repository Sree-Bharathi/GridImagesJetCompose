package com.sree.imagelistapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sree.imagelistapplication.categories
import com.sree.imagelistapplication.network.Photos
import com.sree.imagelistapplication.network.RestApis
import com.sree.imagelistapplication.network.RetrofitHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PhotosViewModel : ViewModel() {
    private val apiKey = "OPZQAtdSFauDsuizXp6CePWA7tIoQtc9jaNg6nNwnXJhNmRV8b5inCFE"
    val pexelsApi = RetrofitHelper.getInstance().create(RestApis::class.java)
    private val photos_m= MutableStateFlow<List<Photos>> (emptyList())
    val photos:StateFlow<List<Photos>> = photos_m
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
                        photos_m.value = response.body()?.photos ?: emptyList()
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
}