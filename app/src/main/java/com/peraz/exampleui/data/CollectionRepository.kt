package com.peraz.exampleui.data

import android.content.Context
import android.util.Log
import com.peraz.exampleui.data.local.CollectionsDao
import com.peraz.exampleui.data.local.CollectionsEntity
import com.peraz.exampleui.data.remote.RetrofitInterface
import com.peraz.exampleui.data.remote.CollectionsModel
import com.peraz.exampleui.data.remote.toCollectionsEntityList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class CollectionRepository @Inject constructor(
    private val collectionsDao: CollectionsDao,
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val retrofitInterface: RetrofitInterface
    ) {
    suspend fun refreshCollection(): List<CollectionsEntity>?{
        try{
            var auxlistofcollections= mutableListOf<CollectionsModel>()
            val apiCollections = retrofitInterface.getCollections().body()?.resultado

            //Obtiene la lista de colecciones desde internet
            if (!apiCollections.isNullOrEmpty()){
                for(imageCollection in apiCollections){ //por cada coleccion va a recolectar las imagenes
                    val imageUrl = imageCollection.image
                    val imageId = imageCollection.id
                    var localImagePath: String? = null

                    if (imageUrl.isNotBlank() && imageId !=null){
                        localImagePath=downloadAndSaveImage(
                            context= context,
                            imageUrl= imageUrl,
                            fileName= "collection_image_$imageId.jpg"
                        )
                    }
                    val newCollection=imageCollection.copy(
                        localImagePath = localImagePath
                    )

                    auxlistofcollections.add(newCollection)
                }
            }//Aqui se van a recolectar las imagenes y almacenarlas no se donde
            val collectionsEntities=auxlistofcollections.toCollectionsEntityList()
            //Convierte las colecciones obtenidas de internet a un EntityLIst(DB)
            collectionsDao.insertCollections(collectionsEntities!!)
            //Actualiza la base de datos si es que hay una.

            return collectionsEntities
        }catch(e: Exception)
        {
            Log.d("CollectionRepository", "${e.message}")
        }
        return null
    }

    private suspend fun downloadAndSaveImage(
        context: Context,
        imageUrl: String,
        fileName: String
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
                    val storageDir = File(context.filesDir, "image_collections")
                    //Accede al directorio en la carpeta files, ahi se van a guardar en files/image_collections
                    if (!storageDir.exists()) {
                        storageDir.mkdirs()
                    }//Si el directorio no existe, lo crea

                    val imageFile = File(storageDir, fileName)

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

    fun getCollections(): List<CollectionsEntity>{
        return collectionsDao.getAllCollections()
    }


}