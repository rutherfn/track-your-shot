package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toCreateCommandLabel
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding

@Composable
fun CreateEditVoiceCommandScreen(params: CreateEditVoiceCommandParams) {
    BackHandler(enabled = true) { params.onToolbarMenuClicked.invoke() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Padding.twentyFour)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
                .padding(top = Padding.forty),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = params.state.type.toCreateCommandLabel(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (params.state.isRecording) {
                    "Listening..."
                } else {
                    "Record the phrase you'd like to use for this command"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (params.state.isRecording) {
                    Colors.secondaryColor
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                textAlign = TextAlign.Center,
                fontWeight = if (params.state.isRecording) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AppColors.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Example phrases",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = getExamplePhrase(params.state.type),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Colors.secondaryColor,
                        fontWeight = FontWeight.Medium,
                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            RecordedPhraseCard(
                isRecording = params.state.isRecording,
                recordedPhrase = params.state.recordedPhrase
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when {
                params.state.isRecording -> {
                    Button(
                        onClick = params.onRecordClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Stop",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Stop Recording",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
                params.state.recordedPhrase != null -> {
                    // Re-record button
                    OutlinedButton(
                        onClick = params.onRecordClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Record Again",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Colors.secondaryColor
                        )
                    }

                    Button(
                        onClick = params.onSaveClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Save Voice Command",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
                else -> {
                    Button(
                        onClick = params.onRecordClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Record Phrase",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordedPhraseCard(
    isRecording: Boolean,
    recordedPhrase: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isRecording -> Colors.secondaryColor.copy(alpha = 0.08f)
                recordedPhrase != null -> Colors.secondaryColor.copy(alpha = 0.12f)
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (recordedPhrase != null) 0.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isRecording -> {
                    // Recording animation
                    RecordingIndicator()
                }
                recordedPhrase != null -> {
                    // Recorded phrase display
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Your recorded phrase",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "\"$recordedPhrase\"",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Colors.secondaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                else -> {
                    // Empty state
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Recorded phrase",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "No phrase recorded yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "recording")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Pulsing circle animation
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .alpha(alpha)
                .background(Colors.secondaryColor.copy(alpha = 0.3f), CircleShape)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated dots
            repeat(3) { index ->
                val dotAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, delayMillis = index * 200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dot$index"
                )
                
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .alpha(dotAlpha)
                        .background(Colors.secondaryColor, CircleShape)
                )
                
                if (index < 2) {
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Recording in progress",
            style = MaterialTheme.typography.bodyMedium,
            color = Colors.secondaryColor,
            fontWeight = FontWeight.Medium
        )
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

// Preview - Initial state
@Preview(name = "Initial State", showBackground = true)
@Composable
fun CreateEditVoiceCommandScreenPreview() {
    CreateEditVoiceCommandScreen(
        params = CreateEditVoiceCommandParams(
            state = CreateEditVoiceCommandState(
                type = VoiceCommandTypes.Make,
                recordedPhrase = null,
                isRecording = false
            ),
            onRecordClicked = { },
            onSaveClicked = { },
            onToolbarMenuClicked = { }
        )
    )
}

// Preview - Recording state
@Preview(name = "Recording State", showBackground = true)
@Composable
fun CreateEditVoiceCommandScreenRecordingPreview() {
    CreateEditVoiceCommandScreen(
        params = CreateEditVoiceCommandParams(
            state = CreateEditVoiceCommandState(
                type = VoiceCommandTypes.Make,
                recordedPhrase = null,
                isRecording = true
            ),
            onRecordClicked = { },
            onSaveClicked = { },
            onToolbarMenuClicked = { }
        )
    )
}

// Preview - Recorded state
@Preview(name = "Recorded State", showBackground = true)
@Composable
fun CreateEditVoiceCommandScreenRecordedPreview() {
    CreateEditVoiceCommandScreen(
        params = CreateEditVoiceCommandParams(
            state = CreateEditVoiceCommandState(
                type = VoiceCommandTypes.Start,
                recordedPhrase = "Let's go",
                isRecording = false
            ),
            onRecordClicked = { },
            onSaveClicked = { },
            onToolbarMenuClicked = { }
        )
    )
}
