package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun SwitchRow(
    title: String,
    onSwitchChanged: ((to: Boolean) -> Unit),
    titleStyle: TextStyle = TextStyles.smallBold,
) {

    Row(
        modifier = Modifier.fillMaxSize().fillMaxWidth().padding(Padding.eight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(start = 4.dp),
            style = titleStyle
        )

        // logic for switch row
        val test = remember { mutableStateOf(value = false ) }
        Switch(
            checked = test.value,
            onCheckedChange = {
                              test.value = it
            },
            modifier = Modifier.padding(start = 4.dp).size(42.dp)
        )
    }
}