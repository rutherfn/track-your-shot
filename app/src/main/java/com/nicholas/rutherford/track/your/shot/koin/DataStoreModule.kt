package com.nicholas.rutherford.track.your.shot.koin

import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReaderImpl
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriterImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object DataStoreModule {

    val modules = module {
        single<DataStorePreferencesWriter> { DataStorePreferencesWriterImpl(application = androidApplication()) }
        single<DataStorePreferencesReader> { DataStorePreferencesReaderImpl(application = androidApplication()) }
    }
}