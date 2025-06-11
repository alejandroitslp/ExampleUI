package com.peraz.exampleui.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionsDao {

    @Query("Select * From collections")
    fun getAllCollections(): List<CollectionsEntity>
    //Se observan cambios con Flow

    @Query("Delete From collections")
    suspend fun deleteAllCollections()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollections(collections: List<CollectionsEntity>)
    //Inserta una lista de colecciones al parecer.

}