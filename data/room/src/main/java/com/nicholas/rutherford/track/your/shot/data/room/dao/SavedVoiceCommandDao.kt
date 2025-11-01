package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.SavedVoiceCommandEntity

/**
 * Created by Nicholas Rutherford, last edited on 2025-10-02
 *
 * Data Access Object (DAO) for performing CRUD operations on the [SavedVoiceCommandEntity] table.
 * Handles insertion, updates, deletions, and queries for saved voice commands in the database.
 */
@Dao
interface SavedVoiceCommandDao {

    /**
     * Inserts a single saved voice command into the database.
     *
     * @params [savedVoiceCommandEntity] The saved voice command to insert.
     */
    @Insert
    suspend fun insert(savedVoiceCommandEntity: SavedVoiceCommandEntity)

    /**
     * Inserts a list of saved voice command entities into the database.
     *
     * @param savedVoiceCommands The list of saved voice commands to insert.
     */
    @Insert
    suspend fun insertAll(savedVoiceCommands: List<SavedVoiceCommandEntity>)

    /**
     * Updates an existing saved voice command entity in the database.
     *
     * @params [savedVoiceCommandEntity] The saved voice command to update.
     */
    @Update
    suspend fun update(savedVoiceCommandEntity: SavedVoiceCommandEntity)

    /**
     * Deletes a saved voice command entity from the database by its entity
     *
     * @params [voiceCommandEntity] The voice command entity of the saved voice command to delete.
     */
    @Delete
    suspend fun deleteSavedVoiceCommand(voiceCommandEntity: SavedVoiceCommandEntity)

    /**
     * Deletes all saved voice commands from the database.
     */
    @Query("DELETE FROM savedVoiceCommands")
    suspend fun deleteAllSavedVoiceCommands()

    /**
     * Retrieves a saved voice command by their name
     *
     * @params [name] The saved voice command name
     * @return The saved voice command with the specified name.
     */
    @Query("SELECT * FROM savedVoiceCommands WHERE name = :name")
    suspend fun getSavedVoiceCommandByName(name: String): SavedVoiceCommandEntity?

    /**
     * Retrieves all saved voice commands
     *t
     * @return Saved voice commands
     */
    @Query("SELECT * FROM savedVoiceCommands")
    suspend fun getAllSavedVoiceCommands(): List<SavedVoiceCommandEntity>

    /**
     * Retrieves the size of the saved voice commands
     *t
     * @return Saved voice commands size
     */
    @Query("SELECT COUNT(*) FROM savedVoiceCommands")
    suspend fun getSavedVoiceCommandsSize(): Int
}
