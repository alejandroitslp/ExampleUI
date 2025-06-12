package com.peraz.exampleui.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    //Obtiene las colecciones
    @GET("/api/collections")
    suspend fun getCollections(): Response<ResponseCollectionsModel>


    //Obtienen los productos de determinadas colecciones
    @GET("/api/products")
    suspend fun getProducts(): Response<ResponseColProductModel>
}