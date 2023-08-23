package com.nicholas.rutherford.track.my.shot.data.room.converters

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.room.response.PlayerPositions
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerPositionsConverterTest {

    private lateinit var database: AppDatabase
    private lateinit var playerDao: PlayerDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        playerDao = database.playerDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun playerPositionsConversion() {
        val positionsToTest = listOf(
            PlayerPositions.PointGuard,
            PlayerPositions.ShootingGuard,
            PlayerPositions.SmallForward,
            PlayerPositions.PowerForward,
            PlayerPositions.Center
        )

        val converter = PlayerPositionsConverter()

        for (position in positionsToTest) {
            val convertedValue = converter.toValue(position)
            val expectedValue = position.value
            assertEquals(expectedValue, convertedValue)

            val convertedPosition = converter.fromValue(expectedValue)
            assertEquals(position, convertedPosition)
        }
    }
}
