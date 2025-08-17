package com.nicholas.rutherford.track.your.shot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Extension function that converts a [Flow] into a [State] in a Composable function,
 * observing it in a lifecycle-aware manner.
 *
 * @param lifecycleOwner The [LifecycleOwner] whose lifecycle should be respected.
 * @param initialState The initial value used before the first emission from the flow.
 *
 * @return A Compose [State] that will be updated as new items are emitted from the flow,
 * only while the lifecycle is at least in the STARTED state.
 */
@Composable
fun <T> Flow<T>.asLifecycleAwareState(
    lifecycleOwner: LifecycleOwner,
    initialState: T
): State<T> =
    lifecycleAwareState(lifecycleOwner, this, initialState)

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Internal utility that sets up a lifecycle-aware collection of a [Flow] and exposes it as [State].
 * Used internally by [asLifecycleAwareState] to avoid collecting when the [LifecycleOwner]
 * is not in a STARTED state or higher.
 *
 * @param lifecycleOwner The [LifecycleOwner] controlling the active state of the flow collection.
 * @param flow The [Flow] to collect.
 * @param initialState The default value returned before any emissions.
 *
 * @return A Compose [State] representing the current value emitted by the flow.
 */
@Composable
internal fun <T> lifecycleAwareState(
    lifecycleOwner: LifecycleOwner,
    flow: Flow<T>,
    initialState: T
): State<T> {
    val lifecycleAwareStateFlow = remember(flow, lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    return lifecycleAwareStateFlow.collectAsState(initialState)
}
