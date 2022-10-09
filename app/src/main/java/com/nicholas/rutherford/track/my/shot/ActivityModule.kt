package com.nicholas.rutherford.track.my.shot

import org.koin.dsl.module

val mainActivityModule = module {
    scope<MainActivity> {
        scoped { MainActivityViewModel(appCenter = get()) }
    }
}
