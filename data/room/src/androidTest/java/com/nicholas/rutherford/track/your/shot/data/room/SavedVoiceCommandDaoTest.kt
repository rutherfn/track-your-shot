package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.SavedVoiceCommandDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.response.VoiceCommandTypes
import com.nicholas.rutherford.track.your.shot.data.test.room.TestSavedVoiceCommandEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SavedVoiceCommandDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var savedVoiceCommandDao: SavedVoiceCommandDao

    private val savedVoiceCommandEntity = TestSavedVoiceCommandEntity.build()

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        savedVoiceCommandDao = appDatabase.savedVoiceCommandsDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun insert() = runBlocking {
        savedVoiceCommandDao.insert(savedVoiceCommandEntity = savedVoiceCommandEntity)

        val allCommands = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(1, equalTo(allCommands.size))
        assertThat(savedVoiceCommandEntity.name, equalTo(allCommands[0].name))
        assertThat(savedVoiceCommandEntity.type, equalTo(allCommands[0].type))
    }

    @Test
    fun insertAll() = runBlocking {
        val secondEntity = savedVoiceCommandEntity.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val thirdEntity = savedVoiceCommandEntity.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val entities = listOf(savedVoiceCommandEntity, secondEntity, thirdEntity)

        savedVoiceCommandDao.insertAll(savedVoiceCommands = entities)

        val allCommands = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(3, equalTo(allCommands.size))
        assertThat(entities.map { it.name }, equalTo(allCommands.map { it.name }))
        assertThat(entities.map { it.type }, equalTo(allCommands.map { it.type }))
    }

    @Test
    fun update() = runBlocking {
        savedVoiceCommandDao.insert(savedVoiceCommandEntity = savedVoiceCommandEntity)

        val allCommandsBefore = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(1, equalTo(allCommandsBefore.size))
        assertThat(savedVoiceCommandEntity.name, equalTo(allCommandsBefore[0].name))

        val updatedEntity = savedVoiceCommandEntity.copy(id = allCommandsBefore[0].id, name = "Updated Start Command", type = VoiceCommandTypes.Stop)

        savedVoiceCommandDao.update(savedVoiceCommandEntity = updatedEntity)

        val allCommandsAfter = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(1, equalTo(allCommandsAfter.size))
        assertThat(updatedEntity.name, equalTo(allCommandsAfter[0].name))
        assertThat(updatedEntity.type, equalTo(allCommandsAfter[0].type))
    }

    @Test
    fun deleteSavedVoiceCommand() = runBlocking {
        savedVoiceCommandDao.insert(savedVoiceCommandEntity = savedVoiceCommandEntity)

        val allCommandsBefore = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(1, equalTo(allCommandsBefore.size))

        savedVoiceCommandDao.deleteSavedVoiceCommand(voiceCommandEntity = allCommandsBefore[0])

        assertThat(emptyList(), equalTo(savedVoiceCommandDao.getAllSavedVoiceCommands()))
    }

    @Test
    fun deleteAllSavedVoiceCommands() = runBlocking {
        val secondEntity = savedVoiceCommandEntity.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val thirdEntity = savedVoiceCommandEntity.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val entities = listOf(savedVoiceCommandEntity, secondEntity, thirdEntity)

        savedVoiceCommandDao.insertAll(savedVoiceCommands = entities)

        val allCommandsBefore = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(3, equalTo(allCommandsBefore.size))
        assertThat(entities.map { it.name }, equalTo(allCommandsBefore.map { it.name }))
        assertThat(entities.map { it.type }, equalTo(allCommandsBefore.map { it.type }))

        savedVoiceCommandDao.deleteAllSavedVoiceCommands()

        assertThat(emptyList(), equalTo(savedVoiceCommandDao.getAllSavedVoiceCommands()))
    }

    @Test
    fun getSavedVoiceCommandByName() = runBlocking {
        val secondEntity = savedVoiceCommandEntity.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val thirdEntity = savedVoiceCommandEntity.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val entities = listOf(savedVoiceCommandEntity, secondEntity, thirdEntity)

        savedVoiceCommandDao.insertAll(savedVoiceCommands = entities)

        val foundEntity = savedVoiceCommandDao.getSavedVoiceCommandByName(name = "Stop Command")

        assertThat("Stop Command", equalTo(foundEntity?.name))
        assertThat(VoiceCommandTypes.Stop, equalTo(foundEntity?.type))
    }

    @Test
    fun getSavedVoiceCommandByName_whenNameDoesNotExist_shouldReturnNull() = runBlocking {
        savedVoiceCommandDao.insert(savedVoiceCommandEntity = savedVoiceCommandEntity)

        val foundEntity = savedVoiceCommandDao.getSavedVoiceCommandByName(name = "Non-existent Command")

        assertThat(foundEntity, nullValue())
    }

    @Test
    fun getAllSavedVoiceCommands() = runBlocking {
        val secondEntity = savedVoiceCommandEntity.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val thirdEntity = savedVoiceCommandEntity.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val fourthEntity = savedVoiceCommandEntity.copy(id = 4, name = "Miss Command", type = VoiceCommandTypes.Miss)
        val entities = listOf(savedVoiceCommandEntity, secondEntity, thirdEntity, fourthEntity)

        savedVoiceCommandDao.insertAll(savedVoiceCommands = entities)

        val allCommands = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(4, equalTo(allCommands.size))
        assertThat(entities.map { it.name }, equalTo(allCommands.map { it.name }))
        assertThat(entities.map { it.type }, equalTo(allCommands.map { it.type }))
    }

    @Test
    fun getAllSavedVoiceCommands_whenEmpty_shouldReturnEmptyList() = runBlocking {
        val allCommands = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(emptyList(), equalTo(allCommands))
    }

    @Test
    fun insertAndRetrieveMultipleVoiceCommandTypes() = runBlocking {
        val startEntity = savedVoiceCommandEntity.copy(id = 1, name = "Start Command", type = VoiceCommandTypes.Start)
        val stopEntity = savedVoiceCommandEntity.copy(id = 2, name = "Stop Command", type = VoiceCommandTypes.Stop)
        val makeEntity = savedVoiceCommandEntity.copy(id = 3, name = "Make Command", type = VoiceCommandTypes.Make)
        val missEntity = savedVoiceCommandEntity.copy(id = 4, name = "Miss Command", type = VoiceCommandTypes.Miss)
        val noneEntity = savedVoiceCommandEntity.copy(id = 5, name = "None Command", type = VoiceCommandTypes.None)

        val entities = listOf(startEntity, stopEntity, makeEntity, missEntity, noneEntity)

        savedVoiceCommandDao.insertAll(savedVoiceCommands = entities)

        val allCommands = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(5, equalTo(allCommands.size))
        assertThat(entities.map { it.name }, equalTo(allCommands.map { it.name }))
        assertThat(entities.map { it.type }, equalTo(allCommands.map { it.type }))
    }

    @Test
    fun updateSpecificVoiceCommand() = runBlocking {
        val originalEntity = savedVoiceCommandEntity.copy(id = 1, name = "Original Command", type = VoiceCommandTypes.Start)
        val secondEntity = savedVoiceCommandEntity.copy(id = 2, name = "Second Command", type = VoiceCommandTypes.Stop)

        savedVoiceCommandDao.insertAll(savedVoiceCommands = listOf(originalEntity, secondEntity))

        val allCommandsBefore = savedVoiceCommandDao.getAllSavedVoiceCommands()
        val updatedEntity = originalEntity.copy(id = allCommandsBefore[0].id, name = "Updated Command", type = VoiceCommandTypes.Make)

        savedVoiceCommandDao.update(savedVoiceCommandEntity = updatedEntity)

        val allCommandsAfter = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(2, equalTo(allCommandsAfter.size))
        assertThat("Updated Command", equalTo(allCommandsAfter[0].name))
        assertThat(VoiceCommandTypes.Make, equalTo(allCommandsAfter[0].type))
        assertThat("Second Command", equalTo(allCommandsAfter[1].name))
    }

    @Test
    fun deleteSpecificVoiceCommand() = runBlocking {
        val firstEntity = savedVoiceCommandEntity.copy(id = 1, name = "First Command", type = VoiceCommandTypes.Start)
        val secondEntity = savedVoiceCommandEntity.copy(id = 2, name = "Second Command", type = VoiceCommandTypes.Stop)
        val thirdEntity = savedVoiceCommandEntity.copy(id = 3, name = "Third Command", type = VoiceCommandTypes.Make)

        savedVoiceCommandDao.insertAll(savedVoiceCommands = listOf(firstEntity, secondEntity, thirdEntity))

        val allCommandsBefore = savedVoiceCommandDao.getAllSavedVoiceCommands()

        savedVoiceCommandDao.deleteSavedVoiceCommand(voiceCommandEntity = allCommandsBefore[1])

        val remainingCommands = savedVoiceCommandDao.getAllSavedVoiceCommands()

        assertThat(2, equalTo(remainingCommands.size))
        assertThat(listOf("First Command", "Third Command"), equalTo(remainingCommands.map { it.name }))
    }
}
