package com.nicholas.rutherford.track.your.shot.base.vm

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Nicholas Rutherford, last edited on 2025-09-29
 *
 * A base `ViewModel` class that implements `DefaultLifecycleObserver` to provide logging
 * for lifecycle events and lifecycle-aware Flow collection management.
 *
 * This class provides:
 * - Lifecycle event logging using Timber for debugging purposes
 * - Automatic Flow collection management that starts on resume and stops on pause
 * - Proper cleanup of all active Flow collections when ViewModel is destroyed
 *
 * Subclasses of this `BaseViewModel` will inherit both lifecycle logging and Flow management behavior.
 *
 * Usage: Extend this class in your own ViewModel classes to gain automatic lifecycle logging
 * and use the `collectFlow` method for lifecycle-aware Flow collection.
 */
abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {

    private val activeFlowCollections = mutableListOf<Job>()
    private var isResumed = false
    private var isStart = false

    /**
     * Optional method for subclasses to provide their own scope.
     * If not overridden, returns null and flow collection will be skipped.
     * This prevents breaking existing unit tests that don't mock BaseViewModel.
     */
    protected open fun getScope(): CoroutineScope? = null

    /**
     * Optional method for subclasses to specify when flow collection should start.
     * If not overridden, defaults to RESUME (traditional behavior).
     */
    protected open fun getFlowCollectionTrigger(): FlowCollectionTrigger = FlowCollectionTrigger.RESUME

    /**
     * Collects a Flow in a lifecycle-aware manner.
     * The collection will automatically start when the ViewModel is resumed and stop when paused.
     * All collections are properly cleaned up when the ViewModel is destroyed.
     *
     * @param flow The Flow to collect
     * @param onCollect The action to perform for each emitted value
     */
    protected fun <T> collectFlow(
        flow: Flow<T>,
        onCollect: suspend (T) -> Unit
    ) {
        getScope()?.let { scope ->
            val job = scope.launch {
                flow.collectLatest { value ->
                    if (shouldCollectFlow()) {
                        onCollect(value)
                    }
                }
            }
            activeFlowCollections.add(job)
        }
    }

    /**
     * Collects multiple Flows using combine in a lifecycle-aware manner.
     * The collection will automatically start when the ViewModel is resumed and stop when paused.
     *
     * @param flow1 The first Flow to combine
     * @param flow2 The second Flow to combine
     * @param onCollect The action to perform for each combined emission
     */
    protected fun <T1, T2> collectFlows(
        flow1: Flow<T1>,
        flow2: Flow<T2>,
        onCollect: suspend (T1, T2) -> Unit
    ) {
        getScope()?.let { scope ->
            val job = scope.launch {
                kotlinx.coroutines.flow.combine(flow1, flow2) { t1, t2 ->
                    if (shouldCollectFlow()) {
                        onCollect(t1, t2)
                    }
                }.collectLatest {}
            }
            activeFlowCollections.add(job)
        }
    }

    /**
     * Collects four Flows using combine in a lifecycle-aware manner.
     * The collection will automatically start when the ViewModel is resumed and stop when paused.
     *
     * @param flow1 The first Flow to combine
     * @param flow2 The second Flow to combine
     * @param flow3 The third Flow to combine
     * @param flow4 The fourth Flow to combine
     * @param onCollect The action to perform for each combined emission
     */
    protected fun <T1, T2, T3, T4> collectFlows(
        flow1: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        onCollect: suspend (T1, T2, T3, T4) -> Unit
    ) {
        getScope()?.let { scope ->
            val job = scope.launch {
                kotlinx.coroutines.flow.combine(flow1, flow2, flow3, flow4) { t1, t2, t3, t4 ->
                    if (shouldCollectFlow()) {
                        onCollect(t1, t2, t3, t4)
                    }
                }.collectLatest {}
            }
            activeFlowCollections.add(job)
        }
    }

    /**
     * Determines if flow collection should happen based on the current lifecycle state and trigger.
     */
    private fun shouldCollectFlow(): Boolean {
        return when (getFlowCollectionTrigger()) {
            FlowCollectionTrigger.INIT -> true
            FlowCollectionTrigger.START -> isStart
            FlowCollectionTrigger.RESUME -> isResumed
        }
    }

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
        isStart = true
        Timber.d("${this::class.simpleName} → onStart")
    }

    /**
     * Called when the ViewModel's lifecycle is resumed.
     * Logs the `onResume` event using Timber and enables Flow collection.
     *
     * @param owner The `LifecycleOwner` associated with the ViewModel.
     */
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        isResumed = true
        Timber.d("${this::class.simpleName} → onResume")
    }

    /**
     * Called when the ViewModel's lifecycle is paused.
     * Logs the `onPause` event using Timber and disables Flow collection.
     *
     * @param owner The `LifecycleOwner` associated with the ViewModel.
     */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        isResumed = false
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
        isStart = false
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

    /**
     * Called when the ViewModel is cleared.
     * Cancels all active Flow collections and cleans up resources.
     */
    override fun onCleared() {
        super.onCleared()
        activeFlowCollections.forEach { it.cancel() }
        activeFlowCollections.clear()
        Timber.d("${this::class.simpleName} → onCleared")
    }
}
