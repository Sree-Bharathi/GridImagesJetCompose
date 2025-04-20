package com.sree.imagelistapplication.repository

import com.sree.imagelistapplication.local.PhotosDao
import com.sree.imagelistapplication.local.PhotosEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface Repository {

    suspend fun insert(photosEntity: PhotosEntity)

    suspend fun delete(photosEntity: PhotosEntity)

    suspend fun update(photosEntity: PhotosEntity)

    suspend fun getAllPhotos(): Flow<List<PhotosEntity>>

    suspend fun getPhotosOnCategory(category:String):Flow<List<PhotosEntity>>

}

class RepositoryImpl @Inject constructor(
      private val dao: PhotosDao) :Repository {

    override suspend fun insert(photosEntity: PhotosEntity) {
        withContext(IO) {
            dao.insertPhoto(photosEntity)
        }
    }

    override suspend fun delete(photosEntity: PhotosEntity) {
        withContext(IO) {
            dao.deletePhoto(photosEntity)
        }
    }

    override suspend fun update(photosEntity: PhotosEntity) {

        withContext(IO) {
            dao.updatePhoto(photosEntity)
        }
    }

    override suspend fun getAllPhotos(): Flow<List<PhotosEntity>> {

       return withContext(IO) {
            dao.getAllPhotos()
        }
    }

    override suspend fun getPhotosOnCategory(category: String): Flow<List<PhotosEntity>> {
        return withContext(IO) {
            dao.getCategoryPhotos(category)
        }
    }

}




