package com.nicholas.rutherford.track.your.shot.koin

import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReaderImpl
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriterImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-20
 *
 * Koin module responsible for providing DataStore dependencies.
 *
 * This module provides implementations of the [DataStorePreferencesWriter] and [DataStorePreferencesReader]
 */
object DataStoreModule {

    /** Koin module containing DataStore dependencies. */
    val modules = module {
        single<DataStorePreferencesWriter> { DataStorePreferencesWriterImpl(application = androidApplication()) }
        single<DataStorePreferencesReader> { DataStorePreferencesReaderImpl(application = androidApplication()) }
    }
}
