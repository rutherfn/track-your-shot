package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.RecordingIndicator
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.examplePhrases
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes.Companion.toCreateCommandLabel
import com.nicholas.rutherford.track.your.shot.feature.voice.commands.VoiceCommandState
import com.nicholas.rutherford.track.your.shot.helper.extensions.hasRecordAudioPermissionEnabled
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding

@Composable
fun CreateEditVoiceCommandScreen(params: CreateEditVoiceCommandParams) {
    val context = LocalContext.current
    var hasRecordAudioPermission by remember { mutableStateOf(value = hasRecordAudioPermissionEnabled(context = context)) }

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
            CommandScreenHeaderInfo(
                name = params.state.recordedPhrase ?: stringResource(id = StringsIds.empty),
                type = params.state.type,
                isRecording = params.state.isRecording,
                voiceCommandState = params.state.voiceCommandState
            )

            ExamplePhraseCard(type = params.state.type)

            Spacer(modifier = Modifier.height(16.dp))

            RecordedPhraseCard(
                isRecording = params.state.isRecording,
                recordedPhrase = params.state.recordedPhrase,
                voiceCommandState = params.state.voiceCommandState,
                voiceCapturedErrorDescription = params.state.voiceCapturedErrorDescription
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when {
                params.state.isRecording -> RecordingPhraseButtonContent(
                    hasRecordAudioPermission = hasRecordAudioPermission,
                    onRecordPhraseClicked = params.onRecordPhraseClicked
                )
                params.state.voiceCapturedErrorDescription != null && params.state.voiceCommandState == VoiceCommandState.RECORDING_ERROR -> ErrorPhraseCaptureButtonContent(
                    hasRecordAudioPermission = hasRecordAudioPermission,
                    onTryAgainClicked = params.onTryAgainClicked,
                    onDismissErrorClicked = params.onDismissErrorClicked
                )
                !params.state.recordedPhrase.isNullOrEmpty() -> PhraseCaptureButtonContent(
                    state = params.state,
                    hasRecordAudioPermission = hasRecordAudioPermission,
                    isCommandAlreadyCreated = params.state.isCommandAlreadyCreated,
                    hasOverriddenExistingCommand = params.state.hasOverriddenExistingCommand,
                    onRecordAgainClicked = params.onRecordAgainClicked,
                    onEditVoiceCommandClicked = params.onEditVoiceCommandClicked,
                    onDeleteVoiceCommandClicked = params.onDeleteVoiceCommandClicked,
                    onSaveNewVoiceCommandClicked = params.onSaveNewVoiceCommandClicked
                )
                else -> NoPhraseCaptureButtonContent(
                    hasRecordAudioPermission = hasRecordAudioPermission,
                    onRecordPhraseClicked = params.onRecordPhraseClicked
                )
            }
        }
    }
}

@Composable
private fun CommandScreenHeaderInfo(
    name: String,
    type: VoiceCommandTypes,
    isRecording: Boolean,
    voiceCommandState: VoiceCommandState
) {
    val headerInfo = if (isRecording) {
        Triple(
            first = stringResource(id = StringsIds.listeningDots),
            second = Colors.secondaryColor,
            third = FontWeight.SemiBold
        )
    } else if (voiceCommandState == VoiceCommandState.EDITING_EXISTING && name.isNotEmpty()) {
        Triple(
            first = stringResource(id = StringsIds.editPhraseXToUseThisCommand, name),
            second = MaterialTheme.colorScheme.onSurfaceVariant,
            third = FontWeight.Normal
        )
    } else {
        Triple(
            first = stringResource(id = StringsIds.recordThePhraseYouWouldLikeText),
            second = MaterialTheme.colorScheme.onSurfaceVariant,
            third = FontWeight.Normal
        )
    }

    Text(
        text = type.toCreateCommandLabel(),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = headerInfo.first,
        style = MaterialTheme.typography.bodyMedium,
        color = headerInfo.second,
        textAlign = TextAlign.Center,
        fontWeight = headerInfo.third
    )

    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun ExamplePhraseCard(type: VoiceCommandTypes) {
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
                text = stringResource(id = StringsIds.examplePhrases),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = type.examplePhrases(),
                style = MaterialTheme.typography.bodyLarge,
                color = Colors.secondaryColor,
                fontWeight = FontWeight.Medium,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )
        }
    }
}

@Composable
private fun RecordedPhraseCard(
    isRecording: Boolean,
    recordedPhrase: String?,
    voiceCommandState: VoiceCommandState,
    voiceCapturedErrorDescription: String?
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
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isRecording -> RecordingIndicator()
                voiceCommandState == VoiceCommandState.RECORDING_ERROR && !voiceCapturedErrorDescription.isNullOrEmpty() -> ErrorPhraseContent(voiceCapturedErrorDescription = voiceCapturedErrorDescription)
                !recordedPhrase.isNullOrEmpty() -> RecordedPhraseContent(recordedPhrase = recordedPhrase)
                else -> NoPhraseRecordedContent()
            }
        }
    }
}

@Composable
private fun ErrorPhraseContent(voiceCapturedErrorDescription: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = StringsIds.errorOccurred),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = voiceCapturedErrorDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
        )
    }
}

@Composable
private fun RecordedPhraseContent(recordedPhrase: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = StringsIds.yourRecordedPhrase),
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

@Composable
private fun NoPhraseRecordedContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = StringsIds.recordedPhrase),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(id = StringsIds.noPhraseRecordedYet),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun RecordingPhraseButtonContent(
    hasRecordAudioPermission: Boolean,
    onRecordPhraseClicked: () -> Unit
) {
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onRecordPhraseClicked.invoke()
        }
    }
    Button(
        onClick = {
            if (hasRecordAudioPermission) {
                onRecordPhraseClicked.invoke()
            } else {
                recordAudioPermissionLauncher.launch(input = Manifest.permission.RECORD_AUDIO)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Stop",
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(id = StringsIds.noPhraseRecordedYet),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
private fun PhraseCaptureButtonContent(
    state: CreateEditVoiceCommandState,
    hasRecordAudioPermission: Boolean,
    isCommandAlreadyCreated: Boolean,
    hasOverriddenExistingCommand: Boolean,
    onRecordAgainClicked: () -> Unit,
    onEditVoiceCommandClicked: (String, VoiceCommandTypes) -> Unit,
    onDeleteVoiceCommandClicked: () -> Unit,
    onSaveNewVoiceCommandClicked: () -> Unit
) {
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onRecordAgainClicked.invoke()
        }
    }

    OutlinedButton(
        onClick = {
            if (hasRecordAudioPermission) {
                onRecordAgainClicked.invoke()
            } else {
                recordAudioPermissionLauncher.launch(input = Manifest.permission.RECORD_AUDIO)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = stringResource(id = StringsIds.recordAgain),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Colors.secondaryColor
        )
    }

    Button(
        onClick = {
            if (isCommandAlreadyCreated) {
                onEditVoiceCommandClicked(state.recordedPhrase ?: "", state.type)
            } else {
                onSaveNewVoiceCommandClicked()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Save",
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = if (isCommandAlreadyCreated) {
                stringResource(id = StringsIds.editVoiceCommand)
            } else {
                stringResource(id = StringsIds.saveVoiceCommand)
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }

    if (isCommandAlreadyCreated && !hasOverriddenExistingCommand) {
        Button(
            onClick = onDeleteVoiceCommandClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(id = StringsIds.deleteCommand),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun NoPhraseCaptureButtonContent(
    hasRecordAudioPermission: Boolean,
    onRecordPhraseClicked: () -> Unit
) {
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onRecordPhraseClicked.invoke()
        }
    }

    Button(
        onClick = {
            if (hasRecordAudioPermission) {
                onRecordPhraseClicked.invoke()
            } else {
                recordAudioPermissionLauncher.launch(input = Manifest.permission.RECORD_AUDIO)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Text(
            text = stringResource(id = StringsIds.recordPhrase),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Composable
private fun ErrorPhraseCaptureButtonContent(
    hasRecordAudioPermission: Boolean,
    onDismissErrorClicked: () -> Unit,
    onTryAgainClicked: () -> Unit
) {
    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onTryAgainClicked.invoke()
        }
    }
    OutlinedButton(
        onClick = {
            if (hasRecordAudioPermission) {
                onTryAgainClicked.invoke()
            } else {
                recordAudioPermissionLauncher.launch(input = Manifest.permission.RECORD_AUDIO)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = stringResource(id = StringsIds.tryAgain),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Colors.secondaryColor
        )
    }

    Button(
        onClick = onDismissErrorClicked,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Dismiss",
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = stringResource(id = StringsIds.dismiss),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

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
            onRecordPhraseClicked = { },
            onSaveNewVoiceCommandClicked = { },
            onEditVoiceCommandClicked = { phrase, type -> },
            onDeleteVoiceCommandClicked = { },
            onToolbarMenuClicked = { },
            onDismissErrorClicked = { },
            onTryAgainClicked = { },
            onRecordAgainClicked = { }
        )
    )
}

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
            onRecordPhraseClicked = { },
            onSaveNewVoiceCommandClicked = { },
            onEditVoiceCommandClicked = { phrase, type -> },
            onDeleteVoiceCommandClicked = { },
            onToolbarMenuClicked = { },
            onDismissErrorClicked = { },
            onTryAgainClicked = { },
            onRecordAgainClicked = { }
        )
    )
}

@Preview(name = "Editing from Existing State", showBackground = true)
@Composable
fun EditVoiceCommandScreenRecordingPreview() {
    CreateEditVoiceCommandScreen(
        params = CreateEditVoiceCommandParams(
            state = CreateEditVoiceCommandState(
                type = VoiceCommandTypes.Make,
                recordedPhrase = "Phrase",
                isRecording = false,
                voiceCommandState = VoiceCommandState.EDITING_EXISTING
            ),
            onRecordPhraseClicked = { },
            onSaveNewVoiceCommandClicked = { },
            onEditVoiceCommandClicked = { phrase, type -> },
            onDeleteVoiceCommandClicked = { },
            onToolbarMenuClicked = { },
            onDismissErrorClicked = { },
            onTryAgainClicked = { },
            onRecordAgainClicked = { }
        )
    )
}

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
            onRecordPhraseClicked = { },
            onSaveNewVoiceCommandClicked = { },
            onEditVoiceCommandClicked = { phrase, type -> },
            onDeleteVoiceCommandClicked = { },
            onToolbarMenuClicked = { },
            onDismissErrorClicked = { },
            onTryAgainClicked = { },
            onRecordAgainClicked = { }
        )
    )
}

@Preview(name = "Error State", showBackground = true)
@Composable
fun CreateEditVoiceCommandScreenErrorPreview() {
    CreateEditVoiceCommandScreen(
        params = CreateEditVoiceCommandParams(
            state = CreateEditVoiceCommandState(
                type = VoiceCommandTypes.Make,
                recordedPhrase = null,
                isRecording = false,
                voiceCommandState = VoiceCommandState.RECORDING_ERROR,
                voiceCapturedErrorDescription = "Unable to process audio. Please try speaking more clearly and ensure you're in a quiet environment."
            ),
            onRecordPhraseClicked = { },
            onSaveNewVoiceCommandClicked = { },
            onEditVoiceCommandClicked = { phrase, type -> },
            onDeleteVoiceCommandClicked = { },
            onToolbarMenuClicked = { },
            onDismissErrorClicked = { },
            onTryAgainClicked = { },
            onRecordAgainClicked = { }
        )
    )
}
