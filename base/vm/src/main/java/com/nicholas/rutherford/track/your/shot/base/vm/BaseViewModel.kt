package com.nicholas.rutherford.track.your.shot.base.vm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import timber.log.Timber

/**
 * A base `ViewModel` class that implements `DefaultLifecycleObserver` to provide logging
 * for lifecycle events. This class logs each lifecycle event (onCreate, onStart, onResume, etc.)
 * using Timber for debugging purposes.
 *
 * Subclasses of this `BaseViewModel` will inherit the lifecycle logging behavior.
 * Timber logging provides an easy way to monitor ViewModel lifecycle behavior during development.
 *
 * Usage: Extend this class in your own ViewModel classes to gain automatic lifecycle logging.
 */
abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {
    /**
     * Called when the ViewModel's lifecycle is created.
     * Logs the `onCreate` event using Timber.
     *
     * @param owner The `LifecycleOwner` associated with the ViewModel.
     */
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Timber.d("${this::class.simpleName} → onCreate")
    }

    /**
     * Called when the ViewModel's lifecycle is started.
     * Logs the `onStart` event using Timber.
     *
     * @param owner The `LifecycleOwner` associated with the ViewModel.
     */
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("${this::class.simpleName} → onStart")
    }

    /**
     * Called when the ViewModel's lifecycle is resumed.
     * Logs the `onResume` event using Timber.
     *
     * @param owner The `LifecycleOwner` associated with the ViewModel.
     */
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Timber.d("${this::class.simpleName} → onResume")
    }

    /**
     * Called when the ViewModel's lifecycle is paused.
     * Logs the `onPause` event using Timber.
     *
     * @param owner The `LifecycleOwner` associated with the ViewModel.
     */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Timber.d("${this::class.simpleName} → onPause")
    }

    /**
     * Called when the ViewModel's lifecycle is stopped.
     * Logs the `onStop` event using Timber.
     *
     * @param owner The `LifecycleOwner` associated with the ViewModel.
     */
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.d("${this::class.simpleName} → onStop")
    }

    /**
     * Called when the ViewModel's lifecycle is destroyed.
     * Logs the `onDestroy` event using Timber.
     *
     * @param owner The `LifecycleOwner` associated with the ViewModel.
     */
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Timber.d("${this::class.simpleName} → onDestroy")
    }
}
