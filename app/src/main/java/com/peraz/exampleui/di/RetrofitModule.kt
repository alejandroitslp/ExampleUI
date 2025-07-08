package com.peraz.exampleui.di

import com.peraz.exampleui.data.remote.ApiInterface
import com.peraz.exampleui.data.remote.RetrofitInstance.getUnsafeOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object RetrofitModule {

    @Provides
    fun providesBaseUrl(): String="http://192.168.100.106:8000/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getUnsafeOkHttpClient()!!)
        .build()

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)

}