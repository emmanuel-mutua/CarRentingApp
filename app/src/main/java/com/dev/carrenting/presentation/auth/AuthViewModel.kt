package com.dev.carrenting.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.dev.carrenting.data.Response
import com.dev.carrenting.data.repository.AuthRepository
import com.dev.carrenting.data.repository.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
    private var _registerState = MutableStateFlow(AuthStateData())
    val registerState = _registerState.asStateFlow()
    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val isEmailVerified = currentUser?.isEmailVerified ?: false
    private var _userData = MutableStateFlow(UserData())


    private val _signInResponse = MutableStateFlow<Response<Boolean>>(Response.Idle)
    val signInResponse = _signInResponse.asStateFlow()

    private val _signUpResponse = MutableStateFlow<Response<Boolean>>(Response.Idle)
    val signUpResponse = _signUpResponse.asStateFlow()

    fun signInEmailAndPassword(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _signInResponse.value = Response.Loading
            val response = authRepo.signInEmailAndPassword(email, password)
            when (response) {
                Response.Loading -> {
                    _registerState.update {
                        it.copy(
                            isLoading = true,
                        )
                    }
                }

                is Response.Failure -> {
                    _registerState.update {
                        it.copy(
                            isLoading = false,
                            message = response.message,
                        )
                    }
                }

                is Response.Success -> {
                    _registerState.update {
                        if (isEmailVerified){
                            it.copy(
                                isLoading = false,
                                isSignedIn = true,
                                message = "Success"
                            )
                        }else{
                            it.copy(
                                isLoading = false,
                                isSignedIn = false,
                                message = "Please verify email"
                            )
                        }

                    }
                }

                Response.Idle -> TODO()
            }
            _signInResponse.value = response
        }
        onSuccess()
    }

    fun signUpEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _signUpResponse.value = Response.Loading
            val response = authRepo.signUpEmailAndPassword(email, password)
            delay(3000)
//            _signUpResponse.value = response
        }
    }

    fun sendEmailVerification() {
        viewModelScope.launch {
            authRepo.sendEmailVerification()
        }
    }

    fun signOut() {
        authRepo.signOut()
    }

    fun signUpUser(email: String, password: String) {
        signUpEmailAndPassword(email, password)
    }

    fun saveUserDetails(
        firstname: String,
        sirname: String,
        email: String,
    ) {
        _userData.update {
            it.copy(
                firstName = firstname,
                sirName = sirname,
                email = email
            )
        }
    }

    fun saveUserToDataBase() {
        viewModelScope.launch {
            delay(2000)
            _userData.update {
                it.copy(
                    uid = FirebaseAuth.getInstance()?.currentUser?.uid ?: "",
                )
            }
            val response = storageService.addUser(_userData.value)
            if (response) {
            }
        }
    }

    fun signUpMyUser(email: String, password: String, firstName: String, surname : String) {
        _signInResponse.value = Response.Loading
        viewModelScope.launch {
            when(val request =  authRepo.signUpEmailAndPassword(email, password)){
                is Response.Failure -> {
                    _registerState.update {
                        it.copy(
                            message = request.message
                        )
                    }
                }
                Response.Idle -> Unit
                Response.Loading -> Unit
                is Response.Success -> {
                    val userData = UserData(
                        uid = request.data,
                        firstName = firstName,
                        sirName = surname,
                        email = email
                    )
                    storageService.addUser(userData)
                    _registerState.update {
                        it.copy(
                            isLoading = false,
                            isSignedIn = true
                        )
                    }
                }
            }
        }
    }
}

data class AuthStateData(
    val isLoading: Boolean = false,
    val message: String = "",
    val isSignedIn: Boolean = false,
)

data class UserData(
    val uid: String = "",
    val firstName: String = "",
    val sirName: String = "",
    val email: String = "",
)

