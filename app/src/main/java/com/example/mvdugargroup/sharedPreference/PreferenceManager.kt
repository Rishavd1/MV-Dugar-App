package com.example.mvdugargroup.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mvdugargroup.Api.LoginDetailsResponse
import com.example.mvdugargroup.Constant.MyConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = MyConstant.SHARED_PREFERENCE_NAME)

class PreferenceManager(private val context: Context) {

    private val TAG = "PreferenceManager"
    companion object {
        val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_USER_NAME = stringPreferencesKey("name")
        val KEY_USER_USERNAME = stringPreferencesKey("user_name")

        val SELECTED_MODULE = stringPreferencesKey("selected_module")
        val FAVORITE_MODULES = stringSetPreferencesKey("favorite_modules")

    }

    val selectedModuleFlow: Flow<String?> = context.dataStore.data
        .map { it[SELECTED_MODULE] }

    val favoriteModulesFlow: Flow<Set<String>> = context.dataStore.data
        .map { it[FAVORITE_MODULES] ?: emptySet() }

    suspend fun saveSelectedModule(module: String?) {
        context.dataStore.edit { prefs ->
            if (module != null) prefs[SELECTED_MODULE] = module
            else prefs.remove(SELECTED_MODULE)
        }
    }

    suspend fun saveFavoriteModules(modules: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[FAVORITE_MODULES] = modules
        }
    }

    val rememberMeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[KEY_REMEMBER_ME] ?: false }

    val userIdFlow: Flow<String> = context.dataStore.data
        .map { it[KEY_USER_ID] ?: "" }

    val userNameFlow: Flow<String> = context.dataStore.data
        .map { it[KEY_USER_NAME] ?: "" }

    val userUsernameFlow: Flow<String> = context.dataStore.data
        .map { it[KEY_USER_USERNAME] ?: "" }


    fun getUserDetails(): Flow<LoginDetailsResponse?> = combine(
        userIdFlow,
        userNameFlow,
        userUsernameFlow
    ) { id, name, userName ->
        if (id.isNotBlank()) {
            Log.d(TAG, "getUserDetails: ${id} ${name} ${userName}")
            LoginDetailsResponse(id.toInt(), userName, name)
        } else null
    }
    suspend fun saveRememberMe(remember: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_REMEMBER_ME] = remember
        }
    }

    suspend fun saveUser(userDetails: LoginDetailsResponse) {
        Log.d(TAG, "saveUser: ${userDetails.id}, ${userDetails.name}, ${userDetails.userName}")
        context.dataStore.edit {
            it[KEY_USER_ID] = userDetails.id.toString()
            it[KEY_USER_NAME] = userDetails.name
            it[KEY_USER_USERNAME] = userDetails.userName
        }
    }
    suspend fun clearRememberMe() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}