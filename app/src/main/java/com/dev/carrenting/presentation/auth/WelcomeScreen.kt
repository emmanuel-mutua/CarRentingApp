package com.dev.carrenting.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.carrenting.R
import com.dev.carrenting.ui.theme.AttachmentAppTheme
import com.dev.carrenting.ui.theme.bodyMediumLight
import com.dev.carrenting.ui.theme.bodySmall
import com.dev.carrenting.ui.theme.titleLarge

@Composable
fun WelcomeScreen(
    onGetStartedClick : () -> Unit,
    onLoginButtonClick : () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Image Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Image(
               painter = painterResource(id = R.drawable.mancleaning),
                contentDescription = "Welcome Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
            )
        }

        // Text Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = "Find your perfect career here",
                style = titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Discover your ideal career and find the right opportunities that match your skills and passion.",
                style = bodyMediumLight,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Get Started Button
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Get Started", style = bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign In Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already have an account?")

                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = onLoginButtonClick) {
                    Text(
                        text = "SignIn",
                        style = bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPrev() {
    AttachmentAppTheme {
        WelcomeScreen({},{})
    }
}
