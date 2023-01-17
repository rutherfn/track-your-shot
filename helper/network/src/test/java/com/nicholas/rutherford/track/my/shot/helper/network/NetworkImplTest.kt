package com.nicholas.rutherford.track.my.shot.helper.network

import android.app.Application
import io.mockk.every
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.net.InetAddress
import java.net.UnknownHostException

class NetworkImplTest {

    internal val application = Application()

    lateinit var networkImpl: NetworkImpl

    @BeforeEach
    fun beforeEach() {
        networkImpl = NetworkImpl(application = application)
    }

    @Nested
    inner class IsDeviceConnectedToInternet() {

        @Test
        @Throws(UnknownHostException::class)
        fun `should return back false if InetAddress throws back a unknown expection`() {
            Assertions.assertEquals(false, networkImpl.isDeviceConnectedToInternet())
        }

        // todo -> add more tests for InetAddresses in the future
    }
}
