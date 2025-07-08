package com.peraz.exampleui.data

import android.content.Context
import android.util.Log
import com.peraz.exampleui.data.local.ProductsDao
import com.peraz.exampleui.data.local.ProductsEntity
import com.peraz.exampleui.data.local.toModel
import com.peraz.exampleui.data.remote.ApiInterface
import com.peraz.exampleui.data.remote.ColProductModel
import com.peraz.exampleui.data.remote.toProductsEntityList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productsDao: ProductsDao,
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val apiInterface: ApiInterface
){
    var _counterProgress= MutableSharedFlow<Float>()
    val counterProgress = _counterProgress.asSharedFlow()
    var _listOfProductsModel = mutableListOf<ColProductModel>()
    val listOfProductsModel = _listOfProductsModel


    suspend fun refreshProducts(): List<ProductsEntity>?{
        try{
            var auxlistofproducts= mutableListOf<ColProductModel>()//Lista vacia que sirve para recolectar los productos y despues volverlo lista de entities


            val apiProducts = apiInterface.getProducts().body()?.products// apiProducts es la lista de resultado e items

            //Obtiene la lista de productos desde internet
            if (!apiProducts.isNullOrEmpty()){
                var sumaFloats= 0f //permite la suma de floats para la progressBar de HomeScreen
                var calculoFloats=100f/apiProducts.size.toFloat()

                for(product in apiProducts){ //por cada producto va a recolectar las imagenes

                    sumaFloats=(sumaFloats+calculoFloats)
                    val imageId = product.id
                    var localImagePath= mutableListOf<String?>() //sirve como auxiliar para recolectar el path y guardarlo en Room
                    var counter=1// el contador cuenta el producto para llevar un orden de folder con id de cada producto
                    for (image in product.images){

                        if (image!!.isNotBlank() && imageId !=null){
                            localImagePath.add( downloadAndSaveImage(
                                foldernumber=imageId,
                                context= context,
                                imageUrl= image.toString(),
                                fileName= "product_image_$counter.jpg"
                            ))
                            counter++
                        }
                    }
                    _counterProgress.emit(sumaFloats/100f) //Emite los floats para la progressBar de HomeScreen

                    val newProduct=product.copy(
                        localimagepath = localImagePath
                    )//en cada nuevo valor pasado a entities, va a agregar el localpath almacenado arriba
                    auxlistofproducts.add(newProduct)//se agrega a la lista auxiliar que despues pasara a lista de entities


                }
            }//Aqui se van a recolectar las imagenes

            val productsEntities=auxlistofproducts.toProductsEntityList()
            //Convierte los productos obtenidos de internet a un EntityLIst(DB)
            productsDao.insertProduct(productsEntities!!)
            //Actualiza la base de datos si es que hay una.

            return productsEntities

        }catch(e: Exception)
        {
            throw e
        }
        return null
    }

    private suspend fun downloadAndSaveImage(
        context: Context,
        imageUrl: String,
        fileName: String,
        foldernumber: Int
    ): String? {
        return withContext(Dispatchers.IO) {
            try{
                val request = Request.Builder().url(imageUrl).build()
                val response = okHttpClient.newCall(request).execute()

                if (!response.isSuccessful) {
                    Log.e("ImageDownloader", "Failed to download image, Code: ${response.code}")
                    return@withContext null
                }//Si la respuesta del servidor no es satisfactoria, se sale con error

                response.body?.let { responseBody ->
                    val storageDir = File(context.filesDir, "image_products_$foldernumber")
                    //Accede al directorio en la carpeta files, ahi se van a guardar en files/image_products
                    if (!storageDir.exists()) {
                        storageDir.mkdirs()
                    }//Si el directorio no existe, lo crea

                    val imageFile = File(storageDir, fileName) //crea el archivo en la ruta con el nombre dado

                    FileOutputStream(imageFile).use { outputStream ->
                        responseBody.byteStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    Log.d("ImageDownloader", "Image saved to: ${imageFile.absolutePath}")
                    return@withContext imageFile.absolutePath
                } ?: return@withContext null
            }catch (e: IOException){
                Log.e("ImageDownloader", "IOException during image download or save: ${e.message}", e)
                throw e
            }catch (e: Exception){
                Log.e("ImageDownloader", "Generic Exception: $e", e)
                throw e
            }
        }
    }


    fun getProducts(): List<ProductsEntity>{
        _listOfProductsModel.clear()
        productsDao.getAllProducts().map {
            it.toModel()
            _listOfProductsModel.add(it.toModel())
        }
        return productsDao.getAllProducts()
    }//Con este se obtienen los datos de la base de datos.

    fun getSpecificCollectionProducts(id: Int): List<ProductsEntity>{
        _listOfProductsModel.clear()
        productsDao.getCollectionProducts(id).map {
            it.toModel()
            _listOfProductsModel.add(it.toModel())
        }
        return productsDao.getCollectionProducts(id)
    }//Con este se obtienen los productos especificos de alguna coleccion.

    fun getProductById(id: Int): ProductsEntity{
        return productsDao.getProductById(id)
    }

}

