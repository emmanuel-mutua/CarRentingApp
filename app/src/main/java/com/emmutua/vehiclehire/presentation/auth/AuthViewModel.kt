package com.emmutua.vehiclehire.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emmutua.vehiclehire.data.Response
import com.emmutua.vehiclehire.data.repository.AuthRepository
import com.emmutua.vehiclehire.data.repository.StorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val storageService: StorageService,
) : ViewModel() {

    private var _registerState = MutableStateFlow(AuthState())
    val registerState = _registerState.asStateFlow()

    private val _signUpResponse = MutableStateFlow<Response<Any>>(Response.Idle)
    val signUpResponse = _signUpResponse.asStateFlow()

    var currentUser : FirebaseUser ? = FirebaseAuth.getInstance().currentUser

    val isEmailVerified = currentUser?.isEmailVerified ?: false
    private var _userData = MutableStateFlow(UserData())

    fun signInEmailAndPassword(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _registerState.update {
                it.copy(
                    authenticated = true
                )
            }
            val response = authRepo.signInEmailAndPassword(email, password)
            when (response) {
                is Response.Failure -> {
                    _registerState.update {
                        it.copy(
                            errorMessage = response.errorMessage,
                        )
                    }
                    onSuccess()
                }

                is Response.Success -> {
                    _registerState.update {
                        if (isEmailVerified) {
                            it.copy(
                                authenticated = true,
                                errorMessage = "Success"
                            )
                        } else {
                            it.copy(
                                authenticated = false,
                                errorMessage = "Please verify email"
                            )
                        }

                    }
                    onSuccess()
                }

                Response.Idle -> Unit
            }
        }
    }

    fun signUpMyUser(
        email: String,
        password: String,
        firstname: String,
        surname: String,
    ) {
        _userData.update {
            it.copy(
                firstname = firstname,
                surname = surname,
                email = email
            )
        }
        viewModelScope.launch {
            val signUpResponse = authRepo.signUpUser(email, password)
            when (signUpResponse) {
                is Response.Failure -> {
                    _registerState.update {
                        it.copy(
                            errorMessage = signUpResponse.errorMessage
                        )
                    }
                }

                Response.Idle -> Unit
                is Response.Success -> {
                    val userData = UserData(
                        uid = signUpResponse.data,
                        firstname = firstname,
                        surname = surname,
                        email = email
                    )
                    storageService.addUser(userData)
                        _registerState.update {
                            it.copy(
                                authenticated = true
                            )
                        }
                }
            }
            _signUpResponse.value = signUpResponse
        }
    }

    fun signOut() {
        authRepo.signOut()
    }
}

data class AuthState(
    val errorMessage: String = "",
    val authenticated: Boolean = false,
)
data class UserData(
    val uid: String = "",
    val firstname: String = "",
    val surname: String = "",
    val email: String = "",
)

