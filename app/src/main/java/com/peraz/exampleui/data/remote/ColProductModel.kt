package com.peraz.exampleui.data.remote

import com.peraz.exampleui.data.local.CollectionsEntity
import com.peraz.exampleui.data.local.ProductsEntity
import kotlin.String

data class ColProductModel(
    val id: Int,
    val name: String,
    val desc: String? = null,
    val images: List<String?>,
    val localimagepath: List<String?>,
    val stock: Int,
    val price: String,
    val idCollection: Int,
    val nameCollection: String,
)

fun ColProductModel.toProductsEntity(): ProductsEntity{
    return ProductsEntity(
        id = this.id,
        name = this.name,
        desc=this.desc,
        images= this.images,
        localimagepath=this.localimagepath,
        stock= this.stock,
        price= this.price,
        idCollection= this.idCollection,
        nameCollection= this.nameCollection,
    )
}

fun List<CollectionsModel>.toCollectionsEntityList():List<CollectionsEntity>{
    return this.map { it.toCollectionsEntity() }
}