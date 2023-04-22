package com.nicholas.rutherford.track.myshot.compose.content.test.rule

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag

fun ComposeContentTestRule.verifyTagIsDisplayed(testTag: String) {
    this.onNodeWithTag(testTag).assertIsDisplayed()
}
