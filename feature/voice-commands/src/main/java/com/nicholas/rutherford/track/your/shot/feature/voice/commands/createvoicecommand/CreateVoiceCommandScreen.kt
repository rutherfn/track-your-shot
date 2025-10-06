package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createvoicecommand

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toDisplayLabel
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun CreateVoiceCommandScreen(params: CreateVoiceCommandParams) {
    BackHandler(enabled = true) { params.onToolbarMenuClicked.invoke() }
    val state = params.state
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        // Large mic icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Colors.secondaryColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Microphone",
                modifier = Modifier.size(60.dp),
                tint = Colors.secondaryColor
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Instructions
        Text(
            text = "Tap to record your voice command phrase",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Example
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AppColors.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Example phrases:",
                    style = TextStyles.smallBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = getExamplePhrase(state.type),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Record button
        Button(
            onClick = { 
                // Handle record action
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Record",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Start Recording",
                style = TextStyles.smallBold,
                color = Color.White
            )
        }
    }
}

private fun getExamplePhrase(type: VoiceCommandTypes?): String {
    return when (type) {
        VoiceCommandTypes.Start -> "\"start\", \"begin\", \"go\""
        VoiceCommandTypes.Stop -> "\"stop\", \"end\", \"done\""
        VoiceCommandTypes.Make -> "\"swish\", \"money\", \"bucket\""
        VoiceCommandTypes.Miss -> "\"brick\", \"airball\", \"clank\""
        else -> "\"swish\", \"brick\", \"start\", \"stop\""
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun CreateVoiceCommandScreenPreview() {
    CreateVoiceCommandScreen(
        params = CreateVoiceCommandParams(
            state = CreateVoiceCommandState(
                type = VoiceCommandTypes.Make,
                recordedPhrase = null
            ),
            onRecordClicked = { },
            onSaveClicked = { },
            onToolbarMenuClicked = { }
        )
    )
}