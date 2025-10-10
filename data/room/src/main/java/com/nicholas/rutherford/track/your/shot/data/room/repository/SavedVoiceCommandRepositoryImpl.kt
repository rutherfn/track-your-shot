package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.SavedVoiceCommandDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toSavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.toSavedVoiceCommandEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Repository implementation for managing saved voice commands in the database.
 * Provides methods for creating, updating, deleting, and retrieving saved voice commands.
 */
class SavedVoiceCommandRepositoryImpl(private val savedVoiceCommandDao: SavedVoiceCommandDao) : SavedVoiceCommandRepository {

    /**
     * Creates a new saved voice command in the database.
     *
     * @param savedVoiceCommand The saved voice command to be created.
     */
    override suspend fun createSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand) = savedVoiceCommandDao.insert(savedVoiceCommandEntity = savedVoiceCommand.toSavedVoiceCommandEntity())

    /**
     * Creates all the saved voice commands in the database.
     *
     * @param commands The list of saved voice commands to be created.
     */
    override suspend fun createAllSavedVoiceCommands(commands: List<SavedVoiceCommand>) = savedVoiceCommandDao.insertAll(savedVoiceCommands = commands.map { command -> command.toSavedVoiceCommandEntity() })

    /**
     * Updates an existing saved voice command in the database.
     *
     * @param savedVoiceCommand The updated saved voice command.
     */
    override suspend fun updateSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand) = savedVoiceCommandDao.update(savedVoiceCommandEntity = savedVoiceCommand.toSavedVoiceCommandEntity())

    /**
     * Deletes a saved voice command from the database.
     *
     * @param savedVoiceCommand The saved voice command to be deleted.
     */
    override suspend fun deleteSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand) = savedVoiceCommandDao.deleteSavedVoiceCommand(voiceCommandEntity = savedVoiceCommand.toSavedVoiceCommandEntity())

    /**
     * Deletes all saved voice commands from the database.
     */
    override suspend fun deleteAllCommands() = savedVoiceCommandDao.deleteAllSavedVoiceCommands()

    /**
     * Fetches a saved voice command by its name from the database.
     *
     * @param name The name of the saved voice command to be fetched.
     * @return The saved voice command with the specified name, or null if not found.
     */
    override suspend fun getVoiceCommandByName(name: String): SavedVoiceCommand? = savedVoiceCommandDao.getSavedVoiceCommandByName(name = name)?.toSavedVoiceCommand()

    /**
     * Fetches all saved voice commands from the database.
     *
     * @return A list of all saved voice commands.
     */
    override suspend fun getAllVoiceCommands(): List<SavedVoiceCommand> = savedVoiceCommandDao.getAllSavedVoiceCommands().map { command -> command.toSavedVoiceCommand() }
}
