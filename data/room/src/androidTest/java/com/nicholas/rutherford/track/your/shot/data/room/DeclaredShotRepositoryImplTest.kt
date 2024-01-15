package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.DeclaredShotDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.feature.splash.declaredshotsjson.FakeDeclaredShotsJson
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

    private lateinit var declaredShotRepositoryImpl: DeclaredShotRepositoryImpl

    private val declaredShotJson = FakeDeclaredShotsJson()

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        declaredShotDao = appDatabase.declaredShotDao()
        declaredShotRepositoryImpl = DeclaredShotRepositoryImpl(
            declaredShotDao = declaredShotDao,
            declaredShotsJson = declaredShotJson
        )
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createDeclaredShotsShouldCallInsertAndReturnShotsIfDeclaredShotsIsEmpty() = runBlocking {
        val newDeclaredShot = DeclaredShot(
            id = 1,
            shotCategory = "inside",
            title = "Layup",
            description = "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
        )
        declaredShotRepositoryImpl.createDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(newDeclaredShot)))
    }

    @Test
    fun createDeclaredShotsShouldCallInsertAndReturnShotsIfDeclaredShotsIsEmptyShouldNotUpdateAgainWhenCalledAgain() = runBlocking {
        val newDeclaredShot = DeclaredShot(
            id = 1,
            shotCategory = "inside",
            title = "Layup",
            description = "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
        )
        declaredShotRepositoryImpl.createDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(newDeclaredShot)))

        declaredShotRepositoryImpl.createDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(newDeclaredShot)))
    }

    @Test
    fun updateDeclaredShot() = runBlocking {
        val newDeclaredShot = DeclaredShot(
            id = 1,
            shotCategory = "inside",
            title = "Layup",
            description = "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
        )
        val newDeclaredShot2 = newDeclaredShot.copy(shotCategory = "other")
        declaredShotRepositoryImpl.createDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(newDeclaredShot)))

        declaredShotRepositoryImpl.updateDeclaredShot(declaredShot = newDeclaredShot2)

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(newDeclaredShot2)))
    }

    @Test
    fun fetchAllDeclaredShots() = runBlocking {
        val newDeclaredShot = DeclaredShot(
            id = 1,
            shotCategory = "inside",
            title = "Layup",
            description = "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
        )
        declaredShotRepositoryImpl.createDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(newDeclaredShot)))
    }

    @Test
    fun deleteAllDeclaredShots() = runBlocking {
        val newDeclaredShot = DeclaredShot(
            id = 1,
            shotCategory = "inside",
            title = "Layup",
            description = "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
        )
        val emptyDeclaredShotList: List<DeclaredShot> = emptyList()

        declaredShotRepositoryImpl.createDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(listOf(newDeclaredShot)))

        declaredShotRepositoryImpl.deleteAllDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchAllDeclaredShots(), equalTo(emptyDeclaredShotList))
    }

    @Test
    fun fetchDeclaredShotFromId() = runBlocking {
        val newDeclaredShot = DeclaredShot(
            id = 1,
            shotCategory = "inside",
            title = "Layup",
            description = "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
        )

        declaredShotRepositoryImpl.createDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchDeclaredShotFromId(id = 1), equalTo(newDeclaredShot))
        assertThat(declaredShotRepositoryImpl.fetchDeclaredShotFromId(id = 2), equalTo(null))
    }

    @Test
    fun fetchDeclaredShotsBySearchQuery() = runBlocking {
        val newDeclaredShot = DeclaredShot(
            id = 1,
            shotCategory = "inside",
            title = "Layup",
            description = "A layup is a fundamental and common inside shot where a player drives towards the basket and releases the ball near the hoop with one hand."
        )
        val emptyDeclaredShotList: List<DeclaredShot> = emptyList()

        declaredShotRepositoryImpl.createDeclaredShots()

        assertThat(declaredShotRepositoryImpl.fetchDeclaredShotsBySearchQuery("Lay"), equalTo(listOf(newDeclaredShot)))
        assertThat(declaredShotRepositoryImpl.fetchDeclaredShotsBySearchQuery("Test"), equalTo(emptyDeclaredShotList))
    }
}
