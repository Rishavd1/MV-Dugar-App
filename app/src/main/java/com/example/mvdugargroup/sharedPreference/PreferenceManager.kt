package com.example.mvdugargroup.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.mvdugargroup.Constant.MyConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = MyConstant.SHARED_PREFERENCE_NAME)

class PreferenceManager(private val context: Context) {

    companion object {
        private val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
    }

    val rememberMeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEY_REMEMBER_ME] ?: false }

    suspend fun saveRememberMe(remember: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_REMEMBER_ME] = remember
        }
    }

    suspend fun clearRememberMe() {
        context.dataStore.edit { prefs ->
            prefs[KEY_REMEMBER_ME] = false
        }
    }
}