package com.nicholas.rutherford.track.your.shot.data.store

import android.app.Application
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriterImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DataStorePreferencesWriterImplTest {

    private val application = mockk<Application>(relaxed = true)

    private val dispatcher = StandardTestDispatcher()

    private lateinit var writer: DataStorePreferencesWriterImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        writer = DataStorePreferencesWriterImpl(application = application)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save app has been launched should update preferences value`() = runTest {
        every { application.dataStore } return 
    }
}