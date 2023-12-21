package com.aiang.data.repository

import com.aiang.data.preferences.UserModel
import com.aiang.data.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val userPreferences: UserPreferences,
){
    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreferences: UserPreferences
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreferences)
            }.also { instance = it }
    }

    suspend fun saveSession(userModel: UserModel) {
        userPreferences.saveSession(userModel)
    }

    suspend fun markFormFilled() {
        userPreferences.markFormFilled()
    }

    suspend fun resetFormFilled() {
        userPreferences.resetFormFilled()
    }

    suspend fun addFinishedTaskId(taskId: String) {
        userPreferences.addFinishedTask(taskId)
    }

    suspend fun deleteFinishedTaskId(taskId: String) {
        userPreferences.deleteFinishedTask(taskId)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }
}