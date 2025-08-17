package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * A reusable card component that contains a title and a switch.
 * The switch reflects a boolean state and allows toggling via user interaction.
 *
 * @param state The current boolean state of the switch.
 * @param title The title text displayed next to the switch.
 * @param onSwitchChanged Lambda callback invoked when the switch is toggled.
 * @param titleStyle Optional [TextStyle] to customize the appearance of the title. Defaults to smallBold style.
 */
@Composable
fun SwitchCard(
    state: Boolean,
    title: String,
    onSwitchChanged: ((to: Boolean) -> Unit),
    titleStyle: TextStyle = TextStyles.smallBold
) {
    Card(
        modifier = Modifier
            .background(AppColors.White)
            .fillMaxWidth()
            .clickable { }
            .padding(top = 8.dp, end = 4.dp, bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(Padding.eight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(start = 4.dp),
                style = titleStyle
            )

            Switch(
                checked = state,
                onCheckedChange = { onSwitchChanged(it) },
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(42.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = AppColors.Orange,
                    checkedTrackColor = AppColors.OrangeVariant.copy(alpha = 0.1f),
                    uncheckedThumbColor = AppColors.LightGray.copy(alpha = 0.3f),
                    uncheckedTrackColor = AppColors.LightGray.copy(alpha = 0.1f)
                )
            )
        }
    }
}

/**
 * Preview of [SwitchCard] composable.
 */
@Preview(showBackground = true)
@Composable
fun SwitchCardPreview() {
    val switchState = remember { mutableStateOf(true) }

    SwitchCard(
        state = switchState.value,
        title = "Enable Notifications",
        onSwitchChanged = { switchState.value = it }
    )
}
