package com.example.gweather.ui.auth


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gweather.data.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthState{
    object Auth: AuthState
    data class Authenticated(val uid: String): AuthState
    data class Error(val message: String): AuthState
}


@HiltViewModel
class AuthViewModel @Inject  constructor(
    private val repo: FirebaseAuthRepository
) : ViewModel() {
    var stateAuth by mutableStateOf<AuthState>(AuthState.Auth)
        private set

    fun register(email: String, password: String){
        viewModelScope.launch {
            stateAuth = try {
                val user = repo.register(email, password).getOrThrow()
                if(user != null){
                    AuthState.Authenticated(user.uid)
                }
                else{
                    AuthState.Error("No User Found")
                }
            } catch (e: Exception){
                AuthState.Error(e.message ?: "Unknown error")
            }
                }
            }

    fun  login(email: String, password: String){
        viewModelScope.launch {
            stateAuth = try {
                val user = repo.login(email, password).getOrThrow()
                if(user != null){
                    AuthState.Authenticated(user.uid)
                }
                else{
                    AuthState.Error("No User Found")
                }
            }
            catch (e: Exception){
                AuthState.Error(e.message ?: "Unknown error")}
        }
    }

    fun checkSession(){
        repo.currentUser()?.let { users ->
            stateAuth = AuthState.Authenticated(users.uid)
        }
    }


    fun logout(){
        repo.logout()
        stateAuth = AuthState.Auth
    }
}