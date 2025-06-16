package com.peraz.exampleui.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.peraz.exampleui.data.remote.ColProductModel
import com.peraz.exampleui.data.remote.CollectionsModel

@Entity(tableName = "products")
@TypeConverters(ImageListConverter::class)
data class ProductsEntity(
    @PrimaryKey(autoGenerate = false) val  id:Int?,
    val name: String,
    val desc: String?,
    val images: List<String?>,
    val localimagepath: List<String?>,
    val stock: Int,
    val price: String,
    val idCollection: Int,
    val nameCollection: String,
)

fun ProductsEntity.toModel(): ColProductModel{

    return ColProductModel(
        id = this.id ?: 0,
        name = this.name,
        desc = this.desc,
        images = this.images,
        localimagepath  = this.localimagepath,
        stock = this.stock,
        price = this.price,
        idCollection = this.idCollection,
        nameCollection = this.nameCollection,
    )
}