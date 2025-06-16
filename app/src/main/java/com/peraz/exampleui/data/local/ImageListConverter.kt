package com.peraz.exampleui.data.local

import androidx.compose.ui.input.key.type
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImageListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): List<String?> {
        if (value == null) {
            return listOf()
        }
        val listType = object : TypeToken<List<String?>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>): String? {
        if (list == null) {
            return null
        }
        return gson.toJson(list)
    }
}