package com.example.cinevote.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation


enum class KeyBoard(){
    MAIL, TEXT, NUMBER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(role:String, type:KeyBoard=KeyBoard.TEXT, error:Boolean=false):String {

     var text by remember { mutableStateOf("") }
    var key = KeyboardOptions(keyboardType = KeyboardType.Text)
    if(type==KeyBoard.NUMBER){
        key = KeyboardOptions(keyboardType = KeyboardType.Number)
    }
    if(type==KeyBoard.MAIL){
        key = KeyboardOptions(keyboardType = KeyboardType.Email)
    }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(role) },
        colors= OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Blue
        ),
        isError = error,
        keyboardOptions = key

    )

    return text
}

@Composable
fun PasswordInput(error:Boolean=false):String {
    var password by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors= OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Blue,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Blue
        ),
        isError = error
    )

    return password
}

