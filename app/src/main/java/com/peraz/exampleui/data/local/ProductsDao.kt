package com.peraz.exampleui.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {
    @Query("Select * From products")
    fun getAllProducts(): List<ProductsEntity>
    //Se observan cambios con Flow

    @Query("Delete From products Where id = :productId")
    suspend fun deleteProductById(productId: Int)

    @Query("Delete From products")
    suspend fun deleteAllProducts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(collections: List<ProductsEntity>)
    //Inserta una lista de productos.

    @Query("Select * from products where idCollection = :id")
    fun getCollectionProducts(id: Int): List<ProductsEntity>
}