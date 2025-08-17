package com.nicholas.rutherford.track.yourshot.compose.content.test.rule

import androidx.annotation.DrawableRes
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility functions and properties for testing Compose UI elements with
 * semantics, drawables, test tags, and text.
 */

/** Semantics property key to store a drawable resource ID for testing */
val DrawableId = SemanticsPropertyKey<Int>("DrawableResId")

/** Extension property to attach a drawable resource ID to a semantics receiver */
var SemanticsPropertyReceiver.drawableId by DrawableId

/**
 * Returns a [SemanticsMatcher] that checks if a node has the given drawable resource ID.
 *
 * @param id The drawable resource ID to match.
 * @return A [SemanticsMatcher] for use in Compose tests.
 */
fun hasDrawable(@DrawableRes id: Int): SemanticsMatcher = SemanticsMatcher.expectValue(DrawableId, id)

/**
 * Verifies that a node with the given [testTag] is displayed.
 *
 * @param testTag The test tag of the node to check.
 */
fun ComposeContentTestRule.verifyTagIsDisplayed(testTag: String) {
    this.onNodeWithTag(testTag = testTag).assertIsDisplayed()
}

/**
 * Verifies that a node with the given [testTag] and drawable resource ID [id] is displayed.
 *
 * @param id The drawable resource ID expected on the node.
 * @param testTag The test tag of the node to check.
 */
fun ComposeContentTestRule.verifyTagWithImageResIsDisplayed(id: Int, testTag: String) {
    this.onNode(hasTestTag(testTag = testTag) and hasDrawable(id = id)).assertIsDisplayed()
}

/**
 * Verifies that a node with the given [testTag] does not exist in the hierarchy.
 *
 * @param testTag The test tag of the node to check.
 */
fun ComposeContentTestRule.verifyTagIsNotDisplayed(testTag: String) {
    this.onNodeWithTag(testTag = testTag).assertDoesNotExist()
}

/**
 * Verifies that a node with the given [testTag] contains the specified [text] and is displayed.
 *
 * @param text The text to match in the node.
 * @param testTag The test tag of the node to check.
 */
fun ComposeContentTestRule.verifyTagWithTextIsDisplayed(text: String, testTag: String) {
    this.onNode(hasTestTag(testTag = testTag) and hasText(text = text))
        .assertIsDisplayed()
}
