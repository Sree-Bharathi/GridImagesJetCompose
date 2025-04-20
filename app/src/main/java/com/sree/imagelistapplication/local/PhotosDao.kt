package com.sree.imagelistapplication.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PhotosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photos:PhotosEntity)

    @Delete
    suspend fun deletePhoto(photos: PhotosEntity)

    @Query("SELECT * from PhotosEntity")
    fun getAllPhotos(): Flow<List<PhotosEntity>>

    @Query("SELECT * from PhotosEntity WHERE  category=:categories ")
    fun getCategoryPhotos(categories : String): Flow<List<PhotosEntity>>

    @Update
    suspend fun updatePhoto(photos: PhotosEntity)
}