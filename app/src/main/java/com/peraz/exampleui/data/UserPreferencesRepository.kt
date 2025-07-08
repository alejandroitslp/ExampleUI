package com.peraz.exampleui.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.peraz.exampleui.core.UserPreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
    ){
    val userNameFlow: Flow<String?> = dataStore.data.catch {
        exception->
        if (exception is IOException){
            emit(emptyPreferences())
        }else{
            throw exception
        }
    }.map {
        preferences ->
        preferences[UserPreferencesKeys.USER_NAME]
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.USER_TOKEN]
    }

    val roleFlow: Flow<Int?> =dataStore.data.map {
        preferences ->
        preferences[UserPreferencesKeys.USER_ROLE]
    }

    //Asi se obtienen los valores de las variables, sin alterar nada.

    suspend fun updateUserName(name: String){
        dataStore.edit { preferences->
            preferences[UserPreferencesKeys.USER_NAME] = name
        }
    }

    suspend fun updateToken(token: String){
        dataStore.edit { preferences->
            preferences[UserPreferencesKeys.USER_TOKEN] = token
        }
    }

    suspend fun updateRole(role: Int){
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.USER_ROLE]= role
        }
    }

    //Estos son los metodos para actualizar los datos.



}