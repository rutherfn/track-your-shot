package com.nicholas.rutherford.track.my.shot

import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.assertNotNull

class MyApplicationTest : KoinTest {

    private val appCenter: AppCenter by inject()
    private val buildTypeImpl: BuildTypeImpl by inject()
    private val viewModel: MainActivityViewModel by inject()

    private val myApplication = MyApplication()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        androidContext(myApplication)
        modules(AppModule().modules)
    }

    @Test
    fun `start koin on create should inject modules with instances as not null`() {
        myApplication.startKoinOnCreate()

        assertNotNull(appCenter)
        assertNotNull(buildTypeImpl)
        assertNotNull(viewModel)
    }
}
