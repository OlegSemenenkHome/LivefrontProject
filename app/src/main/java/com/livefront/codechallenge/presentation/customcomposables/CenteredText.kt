package com.livefront.codechallenge.presentation.customcomposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun CenteredText(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            fontSize = 20.sp,
            text = text,
            modifier = modifier
                .padding(horizontal = 20.dp)
        )
    }
}

@Composable
internal fun CenteredTextWithButton(
    buttonText: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                fontSize = 20.sp,
                text = text,
                modifier = modifier
                    .padding(horizontal = 20.dp)
            )

            Button(
                onClick = onClick,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    fontSize = 20.sp,
                    text = buttonText,
                )
            }
        }
    }
}
