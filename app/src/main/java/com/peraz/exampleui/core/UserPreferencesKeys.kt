package com.peraz.exampleui.core

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val USER_NAME= stringPreferencesKey("user_name")
    val USER_ROLE= intPreferencesKey("user_role")
    val USER_TOKEN=stringPreferencesKey("user_token")
}