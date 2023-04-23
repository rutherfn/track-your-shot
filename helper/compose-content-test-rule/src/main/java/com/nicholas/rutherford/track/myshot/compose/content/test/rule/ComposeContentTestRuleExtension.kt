package com.nicholas.rutherford.track.myshot.compose.content.test.rule

import androidx.annotation.DrawableRes
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule

val DrawableId = SemanticsPropertyKey<Int>("DrawableResId")
var SemanticsPropertyReceiver.drawableId by DrawableId

fun hasDrawable(@DrawableRes id: Int): SemanticsMatcher = SemanticsMatcher.expectValue(DrawableId, id)

fun ComposeContentTestRule.verifyTagIsDisplayed(testTag: String) {
    this.onNodeWithTag(testTag = testTag).assertIsDisplayed()
}

fun ComposeContentTestRule.verifyTagWithImageResIsDisplayed(id: Int, testTag: String) {
    this.onNode(hasTestTag(testTag = testTag) and hasDrawable(id = id)).assertIsDisplayed()
}

fun ComposeContentTestRule.verifyTagIsNotDisplayed(testTag: String) {
    this.onNodeWithTag(testTag = testTag).assertIsNotDisplayed()
}

fun ComposeContentTestRule.verifyTagWithTextIsDisplayed(text: String, testTag: String) {
    this.onNode(hasTestTag(testTag = testTag) and hasText(text = text))
        .assertIsDisplayed()
}
