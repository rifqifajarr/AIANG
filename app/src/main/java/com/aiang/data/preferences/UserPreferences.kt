package com.aiang.data.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aiang.data.api.response.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val USERID_KEY = stringPreferencesKey("userId")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val IS_FORM_FILLED_KEY = booleanPreferencesKey("isFormFilled")
        private val FINISHED_TASK_ID_KEY = stringSetPreferencesKey("finishedTaskId")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[USERID_KEY] = user.userId
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[IS_FORM_FILLED_KEY] = false
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
                preferences[USERID_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[IS_FORM_FILLED_KEY] ?: false,
                getFinishedTasks(preferences)
            )
        }
    }

    suspend fun addFinishedTask(taskId: String) {
        dataStore.edit { preferences ->
            val currentFinishedTasks = preferences[FINISHED_TASK_ID_KEY] ?: emptySet()
            val updatedFinishedTasks = currentFinishedTasks.toMutableSet().apply {
                add(taskId)
            }
            preferences[FINISHED_TASK_ID_KEY] = updatedFinishedTasks
        }
    }

    suspend fun deleteFinishedTask(taskId: String) {
        dataStore.edit { preferences ->
            val currentFinishedTasks = preferences[FINISHED_TASK_ID_KEY] ?: emptySet()
            val updatedFinishedTasks = currentFinishedTasks.toMutableSet().apply {
                remove(taskId)
            }
            preferences[FINISHED_TASK_ID_KEY] = updatedFinishedTasks
        }
    }

    private fun getFinishedTasks(preferences: Preferences): List<String> {
        val finishedTaskId = preferences[FINISHED_TASK_ID_KEY] ?: emptySet()
        return finishedTaskId.map { it }
    }

    suspend fun markFormFilled() {
        dataStore.edit { preferences ->
            preferences[IS_FORM_FILLED_KEY] = true
        }
    }

    suspend fun resetFormFilled() {
        dataStore.edit { preferences ->
            preferences[IS_FORM_FILLED_KEY] = false
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}