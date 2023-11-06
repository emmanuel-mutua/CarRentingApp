package com.johnstanley.attachmentapp.presentation.auth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.johnstanley.attachmentapp.data.Response
import com.johnstanley.attachmentapp.presentation.components.MyOutlinedTextField
import com.johnstanley.attachmentapp.presentation.components.MyProgressIndicator
import com.johnstanley.attachmentapp.presentation.components.PassWordField
import com.johnstanley.attachmentapp.ui.theme.AttachmentAppTheme
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    userData: UserData,
    navigateToLogin: () -> Unit,
) {
    val passwordVisible by rememberSaveable { mutableStateOf(false) }
    val messageBarState = rememberMessageBarState()
    var isLoading by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf("") }
    var registrationNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val role by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isPassWordError by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(role) }
    val roles = listOf("Staff", "Student")
    val context = LocalContext.current
    when (val signInResponse = viewModel.signUpResponse) {
        Response.Loading -> {
            isLoading = true
        }

        is Response.Success -> {
            val isSignedUp = signInResponse.data
            if (isSignedUp) {
                messageBarState.addSuccess("Success Verify Email")
                isLoading = false
                viewModel.sendEmailVerification()
                navigateToLogin()
            }
        }

        is Response.Failure -> signInResponse.apply {
            LaunchedEffect(message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        else -> {}
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        ContentWithMessageBar(messageBarState = messageBarState) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Welcome!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Register an account",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, start = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Select:")
                    roles.forEach { roleOption ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = roleOption == selectedRole,
                                onClick = {
                                    selectedRole = roleOption
                                    viewModel.setRole(roleOption)
                                },
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(roleOption)
                        }
                    }
                }

                MyOutlinedTextField(
                    value = fullName,
                    placeHolder = "FullName",
                    onValueChange = { fullName = it },
                    isError = false,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
                if (selectedRole == "Student") {
                    MyOutlinedTextField(
                        value = registrationNumber,
                        placeHolder = "Registration Number",
                        onValueChange = { registrationNumber = it },
                        isError = false,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                        ),
                    )
                }

                MyOutlinedTextField(
                    value = phoneNumber,
                    placeHolder = "Phone Number",
                    onValueChange = { phoneNumber = it },
                    isError = false,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                    ),
                )

                MyOutlinedTextField(
                    value = email,
                    placeHolder = "Email",
                    onValueChange = { email = it },
                    isError = false,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                    ),
                )
                Spacer(modifier = Modifier.height(10.dp))

                PassWordField(
                    isPasswordVisible = passwordVisible,
                    passwordValue = password,
                    label = "Password",
                    isError = isPassWordError,
                    onValueChange = { password = it },
                )
                PassWordField(
                    isPasswordVisible = passwordVisible,
                    passwordValue = confirmPassword,
                    label = "ConfirmPassword",
                    isError = isPassWordError,
                    onValueChange = { confirmPassword = it },
                )

//            if (passwordState.error != "") {
//                Text(
//                    text = passwordState.error ?: "",
//                    style = MaterialTheme.typography.body2,
//                    color = MaterialTheme.colors.error,
//                    textAlign = TextAlgn.End,
//                  modifier = Modifier.fillMaxWidth(),
//                )
//            }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (role.isEmpty() && fullName.isEmpty() && phoneNumber.isEmpty() && email.isEmpty() && password.isEmpty()) {
                            Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        }

                        viewModel.signUpUser(email, password)
                    },
                    shape = RoundedCornerShape(16),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    if (!isLoading) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            text = "Register",
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                        )
                    } else {
                        MyProgressIndicator()
                    }
                }
                TextButton(
                    onClick = navigateToLogin,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(),
                            ) {
                                append("Already have an account?")
                            }
                            append(" ")
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                ),
                            ) {
                                append("Login")
                            }
                        },
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPrev() {
    AttachmentAppTheme {
    }
}
