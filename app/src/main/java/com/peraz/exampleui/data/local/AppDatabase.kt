package com.peraz.exampleui.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CollectionsEntity::class, ProductsEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun collectionsDao(): CollectionsDao
    abstract fun productsDao(): ProductsDao
}