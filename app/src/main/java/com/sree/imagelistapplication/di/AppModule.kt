package com.sree.imagelistapplication.di

import android.content.Context
import androidx.room.Room
import com.sree.imagelistapplication.local.AppDataBase
import com.sree.imagelistapplication.local.PhotosDao
import com.sree.imagelistapplication.repository.Repository
import com.sree.imagelistapplication.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context) :AppDataBase
    {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "Imagelist_db"
        )
//            .addMigrations() later add migrations if u change the table
            .build()
    }



    @Provides
    fun provideMyRepository(mydb : AppDataBase) :Repository
    {
         return RepositoryImpl(mydb.dao)
    }
}