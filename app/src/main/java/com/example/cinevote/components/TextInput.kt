package com.example.cinevote.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.cinevote.R
import kotlin.reflect.KFunction1


enum class KeyBoard(){
    MAIL, TEXT, NUMBER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(role:String, type:KeyBoard=KeyBoard.TEXT, error:Boolean=false, onChangeAction:(title:String)->Unit):String {

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
        onValueChange = { text = it ; onChangeAction(text) },
        label = { Text(role) },
        colors= OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.myGreen),
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Blue
        ),
        isError = error,
        keyboardOptions = key,


    )

    return text
}

@Composable
fun PasswordInput(error:Boolean=false, onChangeAction:(password:String)->Unit):String {
    var password by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it ; onChangeAction(password) },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors= OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.myGreen),
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.Blue
        ),
        isError = error
    )

    return password
}

