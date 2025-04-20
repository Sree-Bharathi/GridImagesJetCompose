package com.sree.imagelistapplication.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PhotosEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase :RoomDatabase()  {
   /* companion object{
        fun getinstance(context: Context) =
            Room.databaseBuilder(context,AppDataBase::class.java,"Imagelist_db")
                .fallbackToDestructiveMigration()
                .build()*/
   abstract val dao: PhotosDao



   // abstract fun getPhotoDao():PhotosDao
}