package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.SavedVoiceCommandDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toSavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.SavedVoiceCommand
import com.nicholas.rutherford.track.your.shot.data.room.response.toSavedVoiceCommandEntity

class SavedVoiceCommandRepositoryImpl(private val savedVoiceCommandDao: SavedVoiceCommandDao) : SavedVoiceCommandRepository {

    override suspend fun createSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand) = savedVoiceCommandDao.insert(savedVoiceCommandEntity = savedVoiceCommand.toSavedVoiceCommandEntity())
    override suspend fun createAllSavedVoiceCommands(commands: List<SavedVoiceCommand>) = savedVoiceCommandDao.insertAll(savedVoiceCommands = commands.map { command -> command.toSavedVoiceCommandEntity() })
    override suspend fun updateSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand) = savedVoiceCommandDao.update(savedVoiceCommandEntity = savedVoiceCommand.toSavedVoiceCommandEntity())
    override suspend fun deleteSavedVoiceCommand(savedVoiceCommand: SavedVoiceCommand) = savedVoiceCommandDao.deleteSavedVoiceCommand(voiceCommandEntity = savedVoiceCommand.toSavedVoiceCommandEntity())
    override suspend fun deleteAllCommands() = savedVoiceCommandDao.deleteAllSavedVoiceCommands()
    override suspend fun getVoiceCommandByName(name: String): SavedVoiceCommand? = savedVoiceCommandDao.getSavedVoiceCommandByName(name = name)?.toSavedVoiceCommand()
    override suspend fun getAllVoiceCommands(): List<SavedVoiceCommand> = savedVoiceCommandDao.getAllSavedVoiceCommands().map { command -> command.toSavedVoiceCommand() }
}
