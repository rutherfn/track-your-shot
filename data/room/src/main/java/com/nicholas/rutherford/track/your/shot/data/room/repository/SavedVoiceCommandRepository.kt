package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand

interface SavedVoiceCommandRepository {
    suspend fun createSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand)
    suspend fun createAllSavedVoiceCommands(commands: List<SavedVoiceCommand>)
    suspend fun updateSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand)
    suspend fun deleteSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand)
    suspend fun deleteAllCommands()
    suspend fun getVoiceCommandByName(name: String): SavedVoiceCommand?
    suspend fun getAllVoiceCommands(): List<SavedVoiceCommand>
}
