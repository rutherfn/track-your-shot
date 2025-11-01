package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository interface defining CRUD operations for Saved Voice Commands
 * Provides an abstraction layer over the underlying SavedVoiceCommandDao,
 * allowing use of Saved Voice Command domain models rather than database entities.
 */
interface SavedVoiceCommandRepository {
    /** Inserts a single [SavedVoiceCommand] into the database. */
    suspend fun createSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand)

    /** Inserts a list of [SavedVoiceCommand]s into the database. */
    suspend fun createAllSavedVoiceCommands(commands: List<SavedVoiceCommand>)

    /** Updates a single [SavedVoiceCommand] in the database. */
    suspend fun updateSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand)

    /** Deletes a single [SavedVoiceCommand] from the database. */
    suspend fun deleteSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand)

    /** Deletes all [SavedVoiceCommand]s from the database. */
    suspend fun deleteAllCommands()

    /** Retrieves a single [SavedVoiceCommand] by its name from the database. */
    suspend fun getVoiceCommandByName(name: String): SavedVoiceCommand?

    /** Retrieves all [SavedVoiceCommand]s from the database. */
    suspend fun getAllVoiceCommands(): List<SavedVoiceCommand>

    /** Retrieves the size of the [SavedVoiceCommand]s from the database. */
    suspend fun getVoiceCommandSize(): Int
}
