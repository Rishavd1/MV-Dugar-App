package com.example.mvdugargroup.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mvdugargroup.Constant.MyConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = MyConstant.SHARED_PREFERENCE_NAME)

class PreferenceManager(private val context: Context) {

    companion object {
        private val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_FAVORITE_MODULES = stringSetPreferencesKey("favorite_modules")
    }


    val rememberMeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEY_REMEMBER_ME] ?: false }

    val userIdFlow: Flow<String?> = context.dataStore.data
        .map { it[KEY_USER_ID] }

    val favoriteModulesFlow: Flow<Set<String>> = context.dataStore.data
        .map { it[KEY_FAVORITE_MODULES] ?: emptySet() }


    suspend fun saveRememberMe(remember: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_REMEMBER_ME] = remember
        }
    }

    suspend fun saveUserId(id: String) {
        context.dataStore.edit { it[KEY_USER_ID] = id }
    }

    suspend fun saveFavoriteModules(modules: Set<String>) {
        context.dataStore.edit { it[KEY_FAVORITE_MODULES] = modules }
    }

    suspend fun removeFavoriteModule(moduleId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_FAVORITE_MODULES] ?: emptySet()
            prefs[KEY_FAVORITE_MODULES] = current - moduleId
        }
    }

    suspend fun addFavoriteModule(moduleId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_FAVORITE_MODULES] ?: emptySet()
            prefs[KEY_FAVORITE_MODULES] = current + moduleId
        }
    }


    suspend fun clearRememberMe() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}