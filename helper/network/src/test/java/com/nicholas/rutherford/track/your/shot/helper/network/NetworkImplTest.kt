package com.nicholas.rutherford.track.your.shot.helper.network

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkImplTest {

    lateinit var networkImpl: NetworkImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    val socket = mockk<Socket>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        networkImpl = NetworkImpl(scope = scope)
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
        fun `port should result in 53`() {
            Assertions.assertEquals(53, PORT)
        }

        @Test
        fun `timeout ms`() {
            Assertions.assertEquals(1500, TIMEOUT_MS)
        }
    }

    @Nested
    inner class IsDeviceConnectedToInternet {

        @Test
        fun `with a successful connection should return true`() = runTest{
            every { socket.connect(any<InetSocketAddress>(), any()) } just Runs
            every { socket.close() } just Runs

            mockkConstructor(Socket::class)
            every { anyConstructed<Socket>().connect(any<InetSocketAddress>(), any()) } just Runs
            every { anyConstructed<Socket>().close() } just Runs

            val result = networkImpl.isDeviceConnectedToInternet()

            Assertions.assertEquals(true, result)
        }

        @Test
        fun `with a successful connect with delay should return true`() = runTest {
            every { socket.connect(any<InetSocketAddress>(), any()) } just Runs
            every { socket.close() } just Runs

            mockkConstructor(Socket::class)
            every { anyConstructed<Socket>().connect(any<InetSocketAddress>(), any()) } just Runs
            every { anyConstructed<Socket>().close() } just Runs

            val result = networkImpl.isDeviceConnectedToInternet()

            Assertions.assertEquals(true, result)
        }

        @Test
        fun `with a failed connection due to IOException should return false`() = runTest {
            every { socket.connect(any<InetSocketAddress>(), any()) } throws IOException()
            every { socket.close() } just Runs

            mockkConstructor(Socket::class)
            every { anyConstructed<Socket>().connect(any<InetSocketAddress>(), any()) } throws IOException()
            every { anyConstructed<Socket>().close() } just Runs

            val result = networkImpl.isDeviceConnectedToInternet()

            Assertions.assertEquals(false, result)
        }
    }
}
