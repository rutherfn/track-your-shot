package com.nicholas.rutherford.track.my.shot.helper.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NetworkImplTest {

    lateinit var networkImpl: NetworkImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        networkImpl = NetworkImpl()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class consts {

        @Test
        fun `host name`() {
            Assertions.assertEquals("8.8.8.8", HOST_NAME)
        }

        @Test
        fun `port`() {
            Assertions.assertEquals(53, PORT)
        }

        @Test
        fun `timeout ms`() {
            Assertions.assertEquals(1500, TIMEOUT_MS)
        }
    }

    // todo add tests
    @Nested
    inner class IsDeviceConnectedToInternet() {

//        @OptIn(ExperimentalCoroutinesApi::class)
//        @Test
//        @Throws(IOException::class)
//        fun `should return back false if InetAddress throws back a unknown expection`() = runTest {
//            Dispatchers.setMain(dispatcher)
//
//            Assertions.assertEquals(false, networkImpl.isDeviceConnectedToInternet())
//        }
    }
}
