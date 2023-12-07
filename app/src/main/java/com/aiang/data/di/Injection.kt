package com.aiang.data.di

import android.content.Context
import com.aiang.data.preferences.UserPreferences
import com.aiang.data.preferences.dataStore
import com.aiang.data.repository.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        return Repository.getInstance(preferences)
    }
}