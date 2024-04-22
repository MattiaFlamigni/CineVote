package com.example.cinevote.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun SimpleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: ButtonColors = ButtonDefaults.buttonColors(),
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White),
        shape= RoundedCornerShape(35)

    ) {
        Box(

            contentAlignment = Alignment.Center
        ) {
            // Here you can customize further if needed
            Text(text = text,
                style = TextStyle(fontSize= fontSize)
            )
        }
    }
}


@Composable
fun FancyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize
) {
    Surface(
        modifier = modifier.padding(8.dp),
        shape = RoundedCornerShape(8.dp),

    ) {
        Box(
            modifier = Modifier
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = text,
                style = TextStyle(fontSize= fontSize)
            )
        }
    }
}