package com.peraz.exampleui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class) // Provide OkHttpClient as a singleton application-wide
object NetworkModule {

    @Provides
    @Singleton // Ensures only one instance of HttpLoggingInterceptor is created
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // Set your desired log level. For debugging, BODY is useful.
            // For production, you might want Level.NONE or Level.BASIC.
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton // Ensures only one instance of OkHttpClient is created
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add the logging interceptor
            // .addInterceptor(YourAuthInterceptor()) // Example: Add other custom interceptors
            .connectTimeout(30, TimeUnit.SECONDS) // Optional: Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Optional: Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Optional: Set write timeout
            .build()
    }
}