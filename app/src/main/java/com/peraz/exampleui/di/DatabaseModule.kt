package com.peraz.exampleui.di

import android.content.Context
import androidx.room.Room
import com.peraz.exampleui.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "AppDatabase"
        ).build()
    }//Crea el BUilder para la base de datos.


    @Provides
    fun providecollectionsDao(db: AppDatabase) = db.collectionsDao()
    //Con este se provee todo el pex al parecer


    @Provides
    fun provideproductsDao(db: AppDatabase) = db.productsDao()
    //Con este se provee todo el pex al parecer
}