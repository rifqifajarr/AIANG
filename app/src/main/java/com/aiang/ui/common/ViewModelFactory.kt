package com.aiang.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aiang.MainViewModel
import com.aiang.data.repository.Repository
import com.aiang.ui.screen.addtask.AddTaskViewModel
import com.aiang.ui.screen.calendar.task.TaskViewModel
import com.aiang.ui.screen.home.HomeViewModel
import com.aiang.ui.screen.login.LoginViewModel
import com.aiang.ui.screen.profile.ProfileViewModel
import com.aiang.ui.screen.routineform.RoutineFormViewModel

class ViewModelFactory(private val repository: Repository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            return AddTaskViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RoutineFormViewModel::class.java)) {
            return RoutineFormViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}