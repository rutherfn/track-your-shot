package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.SavedVoiceCommandDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.test.room.TestSavedVoiceCommand
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SavedVoiceCommandRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var savedVoiceCommandDao: SavedVoiceCommandDao

    private val savedVoiceCommand = TestSavedVoiceCommand.build()

    private lateinit var savedVoiceCommandRepositoryImpl: SavedVoiceCommandRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        savedVoiceCommandDao = appDatabase.savedVoiceCommandsDao()

        savedVoiceCommandRepositoryImpl = SavedVoiceCommandRepositoryImpl(savedVoiceCommandDao = savedVoiceCommandDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createSavedVoiceCommand() = runBlocking {
        savedVoiceCommandRepositoryImpl.createSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand)

        val allCommands = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        assertThat(1, equalTo(allCommands.size))
        assertThat(savedVoiceCommand.name, equalTo(allCommands[0].name))
        assertThat(savedVoiceCommand.type, equalTo(allCommands[0].type))
    }

    @Test
    fun createAllSavedVoiceCommands() = runBlocking {
        val secondCommand = savedVoiceCommand.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val thirdCommand = savedVoiceCommand.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val commands = listOf(savedVoiceCommand, secondCommand, thirdCommand)

        savedVoiceCommandRepositoryImpl.createAllSavedVoiceCommands(commands = commands)

        val allCommands = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        assertThat(3, equalTo(allCommands.size))
        assertThat(commands.map { it.name }, equalTo(allCommands.map { it.name }))
        assertThat(commands.map { it.type }, equalTo(allCommands.map { it.type }))
    }

    @Test
    fun updateSavedVoiceCommand() = runBlocking {
        savedVoiceCommandRepositoryImpl.createSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand)

        val allCommandsBefore = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        assertThat(1, equalTo(allCommandsBefore.size))
        assertThat(savedVoiceCommand.name, equalTo(allCommandsBefore[0].name))

        val updatedCommand = savedVoiceCommand.copy(id = allCommandsBefore[0].id, name = "Updated Start Command", type = VoiceCommandTypes.Stop)
        savedVoiceCommandRepositoryImpl.updateSavedVoiceCommand(savedVoiceCommand = updatedCommand)

        val allCommandsAfter = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        assertThat(1, equalTo(allCommandsAfter.size))
        assertThat(updatedCommand.name, equalTo(allCommandsAfter[0].name))
        assertThat(updatedCommand.type, equalTo(allCommandsAfter[0].type))
    }

    @Test
    fun deleteSavedVoiceCommand() = runBlocking {
        savedVoiceCommandRepositoryImpl.createSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand)

        val allCommandsBefore = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        assertThat(1, equalTo(allCommandsBefore.size))

        savedVoiceCommandRepositoryImpl.deleteSavedVoiceCommand(savedVoiceCommand = allCommandsBefore[0])

        assertThat(emptyList(), equalTo(savedVoiceCommandRepositoryImpl.getAllVoiceCommands()))
    }

    @Test
    fun deleteAllCommands() = runBlocking {
        val secondCommand = savedVoiceCommand.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val thirdCommand = savedVoiceCommand.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val commands = listOf(savedVoiceCommand, secondCommand, thirdCommand)

        savedVoiceCommandRepositoryImpl.createAllSavedVoiceCommands(commands = commands)

        val allCommandsBefore = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        assertThat(3, equalTo(allCommandsBefore.size))
        assertThat(commands.map { it.name }, equalTo(allCommandsBefore.map { it.name }))
        assertThat(commands.map { it.type }, equalTo(allCommandsBefore.map { it.type }))

        savedVoiceCommandRepositoryImpl.deleteAllCommands()

        assertThat(emptyList(), equalTo(savedVoiceCommandRepositoryImpl.getAllVoiceCommands()))
    }

    @Test
    fun getVoiceCommandByName() = runBlocking {
        val secondCommand = savedVoiceCommand.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val thirdCommand = savedVoiceCommand.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val commands = listOf(savedVoiceCommand, secondCommand, thirdCommand)

        savedVoiceCommandRepositoryImpl.createAllSavedVoiceCommands(commands = commands)

        val foundCommand = savedVoiceCommandRepositoryImpl.getVoiceCommandByName(name = "Stop Command")

        assertThat("Stop Command", equalTo(foundCommand?.name))
        assertThat(VoiceCommandTypes.Stop, equalTo(foundCommand?.type))
    }

    @Test
    fun getVoiceCommandByName_whenNameDoesNotExist_shouldReturnNull() = runBlocking {
        savedVoiceCommandRepositoryImpl.createSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand)

        val foundCommand = savedVoiceCommandRepositoryImpl.getVoiceCommandByName(name = "Non-existent Command")

        assertThat(foundCommand, nullValue())
    }

    @Test
    fun getAllVoiceCommands() = runBlocking {
        val secondCommand = savedVoiceCommand.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val thirdCommand = savedVoiceCommand.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val fourthCommand = savedVoiceCommand.copy(id = 4, name = "Miss Command", type = VoiceCommandTypes.Miss)
        val commands = listOf(savedVoiceCommand, secondCommand, thirdCommand, fourthCommand)

        savedVoiceCommandRepositoryImpl.createAllSavedVoiceCommands(commands = commands)

        val allCommands = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()

        assertThat(4, equalTo(allCommands.size))
        assertThat(commands.map { it.name }, equalTo(allCommands.map { it.name }))
        assertThat(commands.map { it.type }, equalTo(allCommands.map { it.type }))
    }

    @Test
    fun getAllVoiceCommands_whenEmpty_shouldReturnEmptyList() = runBlocking {
        val allCommands = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()

        assertThat(emptyList(), equalTo(allCommands))
    }

    @Test
    fun createAndRetrieveMultipleVoiceCommandTypes() = runBlocking {
        val startCommand = savedVoiceCommand.copy(id = 1, name = "Start Command", type = VoiceCommandTypes.Start)
        val stopCommand = savedVoiceCommand.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val makeCommand = savedVoiceCommand.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val missCommand = savedVoiceCommand.copy(id = 4, name = "Miss Command", type = VoiceCommandTypes.Miss)
        val noneCommand = savedVoiceCommand.copy(id = 5, name = "None Command", type = VoiceCommandTypes.None)

        val commands = listOf(startCommand, stopCommand, makeCommand, missCommand, noneCommand)

        savedVoiceCommandRepositoryImpl.createAllSavedVoiceCommands(commands = commands)

        val allCommands = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()

        assertThat(5, equalTo(allCommands.size))
        assertThat(commands.map { it.name }, equalTo(allCommands.map { it.name }))
        assertThat(commands.map { it.type }, equalTo(allCommands.map { it.type }))
    }

    @Test
    fun updateSpecificVoiceCommand() = runBlocking {
        val originalCommand = savedVoiceCommand.copy(id = 1, name = "Original Command", type = VoiceCommandTypes.Start)
        val secondCommand = savedVoiceCommand.copy(id = 2, name = "Second Command", type = VoiceCommandTypes.Stop)

        savedVoiceCommandRepositoryImpl.createAllSavedVoiceCommands(commands = listOf(originalCommand, secondCommand))

        val allCommandsBefore = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        val updatedCommand = originalCommand.copy(id = allCommandsBefore[0].id, name = "Updated Command", type = VoiceCommandTypes.Make)
        savedVoiceCommandRepositoryImpl.updateSavedVoiceCommand(savedVoiceCommand = updatedCommand)

        val allCommandsAfter = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        assertThat(2, equalTo(allCommandsAfter.size))
        assertThat("Updated Command", equalTo(allCommandsAfter[0].name))
        assertThat(VoiceCommandTypes.Make, equalTo(allCommandsAfter[0].type))
        assertThat("Second Command", equalTo(allCommandsAfter[1].name))
    }

    @Test
    fun deleteSpecificVoiceCommand() = runBlocking {
        val firstCommand = savedVoiceCommand.copy(id = 1, name = "First Command", type = VoiceCommandTypes.Start)
        val secondCommand = savedVoiceCommand.copy(id = 2, name = "Second Command", type = VoiceCommandTypes.Stop)
        val thirdCommand = savedVoiceCommand.copy(id = 3, name = "Third Command", type = VoiceCommandTypes.Make)

        savedVoiceCommandRepositoryImpl.createAllSavedVoiceCommands(commands = listOf(firstCommand, secondCommand, thirdCommand))

        val allCommandsBefore = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        savedVoiceCommandRepositoryImpl.deleteSavedVoiceCommand(savedVoiceCommand = allCommandsBefore[1]) // Delete the second command

        val remainingCommands = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        assertThat(2, equalTo(remainingCommands.size))
        assertThat(listOf("First Command", "Third Command"), equalTo(remainingCommands.map { it.name }))
    }

    @Test
    fun createSingleCommandAndVerifyRetrieval() = runBlocking {
        savedVoiceCommandRepositoryImpl.createSavedVoiceCommand(savedVoiceCommand = savedVoiceCommand)

        val retrievedCommand = savedVoiceCommandRepositoryImpl.getVoiceCommandByName(name = savedVoiceCommand.name)

        assertThat(savedVoiceCommand.name, equalTo(retrievedCommand?.name))
        assertThat(savedVoiceCommand.type, equalTo(retrievedCommand?.type))
    }

    @Test
    fun createMultipleCommandsAndVerifyAllRetrieval() = runBlocking {
        val commands = listOf(
            savedVoiceCommand.copy(id = 1, name = "Start Command", type = VoiceCommandTypes.Start),
            savedVoiceCommand.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop),
            savedVoiceCommand.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        )

        savedVoiceCommandRepositoryImpl.createAllSavedVoiceCommands(commands = commands)

        val allCommands = savedVoiceCommandRepositoryImpl.getAllVoiceCommands()
        val retrievedStart = savedVoiceCommandRepositoryImpl.getVoiceCommandByName(name = "Start Command")
        val retrievedStop = savedVoiceCommandRepositoryImpl.getVoiceCommandByName(name = "Stop Command")
        val retrievedMake = savedVoiceCommandRepositoryImpl.getVoiceCommandByName(name = "Make Command")

        assertThat(3, equalTo(allCommands.size))
        assertThat("Start Command", equalTo(retrievedStart?.name))
        assertThat(VoiceCommandTypes.Start, equalTo(retrievedStart?.type))
        assertThat("Stop Command", equalTo(retrievedStop?.name))
        assertThat(VoiceCommandTypes.Stop, equalTo(retrievedStop?.type))
        assertThat("Make Command", equalTo(retrievedMake?.name))
        assertThat(VoiceCommandTypes.Make, equalTo(retrievedMake?.type))
    }
}
