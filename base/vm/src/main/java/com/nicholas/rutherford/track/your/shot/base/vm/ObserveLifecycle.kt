package com.nicholas.rutherford.track.your.shot.base.vm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * A Composable function that observes the lifecycle of a [BaseViewModel] and ensures that
 * the lifecycle is correctly managed during the lifecycle of the given [lifecycleOwner].
 *
 * This function attaches the [BaseViewModel] as a lifecycle observer to the provided [lifecycleOwner].
 * It also ensures that the observer is removed when the lifecycle owner is disposed of, avoiding
 * potential memory leaks or lifecycle-related issues.
 *
 * @param viewModel The [BaseViewModel] instance that needs to be observed during the lifecycle.
 * @param lifecycleOwner The [LifecycleOwner] whose lifecycle events will be observed. By default,
 *                       it uses the current [LocalLifecycleOwner].
 */
@Composable
fun ObserveLifecycle(viewModel: BaseViewModel, lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    // Use DisposableEffect to ensure proper lifecycle management
    DisposableEffect(lifecycleOwner) {
        // Add the viewModel as a lifecycle observer
        lifecycleOwner.lifecycle.addObserver(viewModel)

        // Remove the viewModel as a lifecycle observer on disposal
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel)
        }
    }
}
