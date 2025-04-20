package com.sree.imagelistapplication.di

import com.sree.imagelistapplication.network.RestApisService
import com.sree.imagelistapplication.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val baseUrl = "https://api.pexels.com/"
@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {



    @Provides
    @Singleton
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }

    @Provides
    fun provideSearchApiService(retrofit: Retrofit) : RestApisService {
        return retrofit.create(RestApisService::class.java)
    }

    @Provides
    fun provideSearchRepo(restApisService: RestApisService):SearchRepository
    {
        return SearchRepository(restApisService)
    }

}