package com.sree.imagelistapplication.repository

import com.sree.imagelistapplication.network.Photos
import com.sree.imagelistapplication.network.RestApisService
const val apiKey = "OPZQAtdSFauDsuizXp6CePWA7tIoQtc9jaNg6nNwnXJhNmRV8b5inCFE"
class SearchRepository(private val restApisService: RestApisService) {
   suspend fun getImages(category: String,count:Int) : Result<List<Photos>>
   {

      val response =restApisService.searchPhotos(apiKey,category,count)

       return try {

          if (response.isSuccessful) {
             response.body()?.photos?.let {
                Result.success(it)
             } ?: run { Result.failure(Exception(" Error occured ")) }
          } else {
             Result.failure(Exception(" Error occured "))
          }
       }catch (e:Exception)
       {
          Result.failure(e)
       }
   }


}