package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.DeclaredShotDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.entities.toDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShotEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeclaredShotRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var declaredShotDao: DeclaredShotDao

    private val declaredShotEntity = TestDeclaredShotEntity.build()

    private lateinit var declaredShotRepositoryImpl: DeclaredShotRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        declaredShotDao = appDatabase.declaredShotDao()

        declaredShotRepositoryImpl = DeclaredShotRepositoryImpl(declaredShotDao = declaredShotDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createDeclaredShotsWithEmptyDeclaredShots() = runBlocking {
        declaredShotRepositoryImpl.createDeclaredShots(shotIdsToFilterOut = emptyList())

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(declaredShotRepositoryImpl.getDeclaredShotsFromJson().sortedBy { it.title }))
    }

    @Test
    fun updateDeclaredShot() = runBlocking {
        declaredShotRepositoryImpl.createNewDeclaredShot(declaredShot = declaredShotEntity.toDeclaredShot())

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(declaredShotEntity.toDeclaredShot())))

        declaredShotRepositoryImpl.updateDeclaredShot(declaredShot = declaredShotEntity.toDeclaredShot().copy(title = "Test1"))

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(declaredShotEntity.toDeclaredShot().copy(title = "Test1"))))
    }

    @Test
    fun fetchAllDeclaredShots() = runBlocking {
        declaredShotRepositoryImpl.createDeclaredShots(shotIdsToFilterOut = emptyList())

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(declaredShotRepositoryImpl.getDeclaredShotsFromJson().sortedBy { it.title }))
    }

    @Test
    fun deleteAllDeclaredShots() = runBlocking {
        declaredShotRepositoryImpl.createDeclaredShots(shotIdsToFilterOut = emptyList())

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(declaredShotRepositoryImpl.getDeclaredShotsFromJson().sortedBy { it.title }))

        declaredShotRepositoryImpl.deleteAllDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(emptyList()))
    }

    @Test
    fun deleteShotById() = runBlocking {
        declaredShotRepositoryImpl.createNewDeclaredShot(declaredShot = declaredShotEntity.toDeclaredShot())

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(declaredShotEntity.toDeclaredShot())))

        declaredShotRepositoryImpl.deleteShotById(id = declaredShotEntity.id)

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(emptyList()))
    }

    @Test
    fun fetchDeclaredShotFromId() = runBlocking {
        declaredShotRepositoryImpl.createNewDeclaredShot(declaredShot = declaredShotEntity.toDeclaredShot())

        assertThat(declaredShotRepositoryImpl.fetchDeclaredShotFromId(id = declaredShotEntity.id), equalTo(declaredShotEntity.toDeclaredShot()))
    }

    @Test
    fun fetchDeclaredShotsBySearchQuery() = runBlocking {
        declaredShotRepositoryImpl.createNewDeclaredShot(declaredShot = declaredShotEntity.toDeclaredShot())

        assertThat(declaredShotRepositoryImpl.fetchDeclaredShotsBySearchQuery(searchQuery = "test"), equalTo(emptyList()))
        assertThat(declaredShotRepositoryImpl.fetchDeclaredShotsBySearchQuery(searchQuery = declaredShotEntity.title), equalTo(listOf(declaredShotEntity.toDeclaredShot())))
    }

    @Test
    fun fetchMaxIdNoEntries() = runBlocking {
        val result = declaredShotRepositoryImpl.fetchMaxId()

        assertThat(1, equalTo(result))
    }

    @Test
    fun fetchMaxIdWithEntries() = runBlocking {
        declaredShotRepositoryImpl.createNewDeclaredShot(declaredShot = declaredShotEntity.toDeclaredShot())
        declaredShotRepositoryImpl.createNewDeclaredShot(declaredShot = declaredShotEntity.toDeclaredShot().copy(id = 444))

        assertThat(listOf(declaredShotEntity.toDeclaredShot(), declaredShotEntity.toDeclaredShot().copy(id = 444)), equalTo(declaredShotRepositoryImpl.fetchAllDeclaredShots()))

        assertThat(444, equalTo(declaredShotRepositoryImpl.fetchMaxId()))
    }
}
