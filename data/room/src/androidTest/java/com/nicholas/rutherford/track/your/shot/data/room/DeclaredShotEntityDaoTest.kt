package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.DeclaredShotDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShotEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeclaredShotEntityDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var declaredShotDao: DeclaredShotDao

    private val declaredShotEntity = TestDeclaredShotEntity.build()

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        declaredShotDao = appDatabase.declaredShotDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun insert() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity))

        assertThat(listOf(declaredShotEntity), equalTo(declaredShotDao.getAllDeclaredShots()))
    }

    @Test
    fun update() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity))

        assertThat(listOf(declaredShotEntity), equalTo(declaredShotDao.getAllDeclaredShots()))

        val updatedEntity = declaredShotEntity.copy(shotCategory = "value1")

        declaredShotDao.update(declaredShotEntity = updatedEntity)

        assertThat(listOf(updatedEntity), equalTo(declaredShotDao.getAllDeclaredShots()))
    }

    @Test
    fun deleteAll() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity))

        assertThat(listOf(declaredShotEntity), equalTo(declaredShotDao.getAllDeclaredShots()))

        declaredShotDao.deleteAll()

        val declaredShotEmptyList: List<DeclaredShotEntity> = emptyList()

        assertThat(declaredShotEmptyList, equalTo(declaredShotDao.getAllDeclaredShots()))
    }

    @Test
    fun deleteDeclaredShotById() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity, declaredShotEntity.copy(id = 4)))

        assertThat(listOf(declaredShotEntity, declaredShotEntity.copy(id = 4)), equalTo(declaredShotDao.getAllDeclaredShots()))

        declaredShotDao.deleteDeclaredShotById(id = 4)

        assertThat(declaredShotDao.getAllDeclaredShots(), equalTo(arrayListOf(declaredShotEntity)))
    }

    @Test
    fun getAllDeclaredShots() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity))

        assertThat(listOf(declaredShotEntity), equalTo(declaredShotDao.getAllDeclaredShots()))
    }

    @Test
    fun getDeclaredShotFromId() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity))

        assertThat(declaredShotEntity, equalTo(declaredShotDao.getDeclaredShotFromId(id = declaredShotEntity.id)))
        assertThat(null, equalTo(declaredShotDao.getDeclaredShotFromId(id = 22)))
    }

    @Test
    fun getDeclaredShotByTitle() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity))

        assertThat(declaredShotEntity, equalTo(declaredShotDao.getDeclaredShotByTitle(title = declaredShotEntity.title)))
        assertThat(null, equalTo(declaredShotDao.getDeclaredShotByTitle(title = "Test Title")))
    }

    @Test
    fun getDeclaredShotsBySearchQuery() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity))

        val emptyDeclaredShotEntityList: List<DeclaredShotEntity> = emptyList()

        assertThat(listOf(declaredShotEntity), equalTo(declaredShotDao.getDeclaredShotsBySearchQuery("ook")))

        assertThat(emptyDeclaredShotEntityList, equalTo(declaredShotDao.getDeclaredShotsBySearchQuery("nick")))
    }

    @Test
    fun getMaxIdOrDefaultWithEntries() = runBlocking {
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity))
        declaredShotDao.insert(declaredShotEntities = listOf(declaredShotEntity.copy(id = 44)))

        assertThat(listOf(declaredShotEntity, declaredShotEntity.copy(id = 44)), equalTo(declaredShotDao.getAllDeclaredShots()))

        val result = declaredShotDao.getMaxIdOrDefault()

        assertThat(result, equalTo(44))
    }

    @Test
    fun getMaxIdOrDefaultWithNoEntries() = runBlocking {
        val declaredShotEmptyList: List<DeclaredShot> = emptyList()

        assertThat(declaredShotEmptyList, equalTo(declaredShotDao.getAllDeclaredShots()))

        val result = declaredShotDao.getMaxIdOrDefault()

        assertThat(result, equalTo(1))
    }
}
