package com.mufiid.pilwali2020.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mufiid.pilwali2020.data.entity.User
import com.mufiid.pilwali2020.data.repository.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepo: AuthRepository) : ViewModel() {
    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private val _userLoggedIn = MutableLiveData<User>()
    val userLoggedIn: LiveData<User> = _userLoggedIn

    fun logIn() {
        viewModelScope.launch {
            val user =
                userName.value?.let { userName ->
                    password.value?.let { password ->
                        authRepo.login(
                            userName = userName,
                            password = password
                        )
                    }
                }
        }

    }
}