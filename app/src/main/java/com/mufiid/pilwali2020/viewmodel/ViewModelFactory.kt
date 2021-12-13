package com.mufiid.pilwali2020.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mufiid.pilwali2020.data.repository.AuthRepository
import com.mufiid.pilwali2020.ui.login.AuthViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val authRepo: AuthRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepo) as T
            }
            else -> throw Throwable("Unknown ViewModel Class: " + modelClass.name)
        }
    }
}