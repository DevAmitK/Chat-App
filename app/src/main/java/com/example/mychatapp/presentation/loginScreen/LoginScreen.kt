package com.example.mychatapp.presentation.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mychatapp.R
import com.example.mychatapp.presentation.navigation.Routes
import com.example.mychatapp.ui.comp.CustomDialog

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    viewModel: LoginViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDialogErrorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .fillMaxWidth(0.5f),
                tint = Color.White,
                painter = painterResource(id = R.drawable.baseline_textsms_24),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                text = "Welcome to My Chat"
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.3f))
            SignInWithGoogleButton(
                modifier = Modifier,
                onSuccess = {
                    if (it.email != null){
                        viewModel.checkIfUserExistsAndLogin(it.email!!,navHostController)
                    }else{
                        showDialog = true
                        showDialogErrorMessage =  "Email Not Found"
                    }
                },
                onError = {
                    showDialog = true
                    showDialogErrorMessage = it?.localizedMessage ?: "Unknown Error"
                })
        }
    }
    if (showDialog) {
        CustomDialog(
            showDialog = true,
            title = "User Not Found",
            message = "$showDialogErrorMessage , click Go To Login Page ",
            confirmButtonText = "Go To Login Page",
            onConfirm = {
                navHostController.navigate(Routes.LoginScreen)
            }
        )
    }
}


