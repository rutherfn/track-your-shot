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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

// todo -> Component is a working concept when finish make sure to document it
@Composable
fun SwitchRow(
    enabled: Boolean,
    title: String,
    onSwitchChanged: ((to: Boolean) -> Unit),
    titleStyle: TextStyle = TextStyles.smallBold
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

        Switch(
            checked = enabled,
            onCheckedChange = { onSwitchChanged.invoke(it) },
            modifier = Modifier.padding(start = 4.dp).size(42.dp)
        )
    }
}
