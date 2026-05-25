package com.example.budgetmanager.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore(name = "settings")

object UserPreferences {

    private object PreferenceKeys {
        val USER_ID = longPreferencesKey("user_id")
        val PROFILE_IMAGE_PATH_KEY = stringPreferencesKey("profile_image_path")
    }

    private fun getEncryptedPrefs(context: Context): EncryptedSharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "secure_user_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    suspend fun getToken(context: Context): String? = withContext(Dispatchers.IO) {
        val prefs = getEncryptedPrefs(context)
        prefs.getString("auth_token", null)
    }

    suspend fun saveToken(context: Context, token: String) = withContext(Dispatchers.IO) {
        getEncryptedPrefs(context).edit().putString("auth_token", token).apply()
    }

    suspend fun clearToken(context: Context) = withContext(Dispatchers.IO) {
        getEncryptedPrefs(context).edit().remove("auth_token").apply()
    }

    fun userIdFlow(context: Context): Flow<Long?> =
        context.dataStore.data.map { preferences ->
            val id = preferences[PreferenceKeys.USER_ID]
            if (id == -1L) null else id
        }

    suspend fun saveUserId(context: Context, userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_ID] = userId
        }
    }

    suspend fun clearUserId(context: Context) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.USER_ID] = -1L
        }
    }

    fun profileImagePathFlow(context: Context): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.PROFILE_IMAGE_PATH_KEY]
        }
    }

    suspend fun saveProfileImagePath(context: Context, path: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.PROFILE_IMAGE_PATH_KEY] = path
        }
    }

    suspend fun clearProfileImagePath(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(PreferenceKeys.PROFILE_IMAGE_PATH_KEY)
        }
    }
}