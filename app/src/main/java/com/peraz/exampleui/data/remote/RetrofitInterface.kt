package com.peraz.exampleui.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitInterface {

    //Obtiene las colecciones
    @GET("/api/collections")
    suspend fun getCollections(): Response<ResponseCollectionsModel>

    //Obtienen los productos de determinadas colecciones
    @GET("/api/products")
    suspend fun getProducts(): Response<ResponseColProductModel>

    //Se realiza el login
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseUserModel>
}