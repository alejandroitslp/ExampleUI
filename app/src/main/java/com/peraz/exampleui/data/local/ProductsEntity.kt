package com.peraz.exampleui.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peraz.exampleui.data.remote.ColProductModel
import com.peraz.exampleui.data.remote.CollectionsModel

@Entity(tableName = "products")
data class ProductsEntity(
    @PrimaryKey(autoGenerate = false) val  id:Int?,
    val name: String,
    val desc: String?,
    val image: String,
    val localimagepath: String?,
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
        image = this.image,
        localimagepath  = this.localimagepath,
        stock = this.stock,
        price = this.price,
        idCollection = this.idCollection,
        nameCollection = this.nameCollection,
    )
}