package com.peraz.exampleui.data.remote

import com.peraz.exampleui.data.local.CollectionsEntity
import com.peraz.exampleui.data.local.ProductsEntity

data class CollectionsModel(
    val id: Int,
    val image: String,
    val nombre: String,
    val localImagePath: String?
)

fun CollectionsModel.toCollectionsEntity(): CollectionsEntity{
    return CollectionsEntity(
        id = this.id,
        nombre = this.nombre,
        image = this.image,
        localImagePath = this.localImagePath
    )
}

fun List<ColProductModel>.toProductsEntityList():List<ProductsEntity>{
    return this.map { it.toProductsEntity() }
}