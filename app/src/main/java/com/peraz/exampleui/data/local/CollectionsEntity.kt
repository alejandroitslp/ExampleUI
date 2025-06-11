package com.peraz.exampleui.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peraz.exampleui.data.remote.CollectionsModel

@Entity(tableName = "collections")
data class CollectionsEntity(
    @PrimaryKey(autoGenerate = false) val  id:Int?,
    val image: String,
    val localImagePath: String?,
    val nombre: String
)

fun CollectionsEntity.toModel(): CollectionsModel{
    return CollectionsModel(
        id = this.id ?: 0,
        image = this.image,
        localImagePath= this.localImagePath,
        nombre = this.nombre
    )
}