package com.nicholas.rutherford.track.your.shot.feature.create.account.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.your.shot.TrackYourShotTheme
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.Shared
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import com.nicholas.rutherford.track.yourshot.compose.content.test.rule.drawableId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val EMAIL_IMAGE_HEIGHT_WIDTH = 144

@Composable
fun AuthenticationScreen(viewModel: AuthenticationViewModel) {
    val coroutineScope = rememberCoroutineScope()

    TrackYourShotTheme {
        AuthenticationScreenContent(viewModel = viewModel, coroutineScope = coroutineScope)
    }
}

@Composable
fun AuthenticationScreenContent(
    viewModel: AuthenticationViewModel,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Padding.twenty),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = DrawablesIds.email),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(EMAIL_IMAGE_HEIGHT_WIDTH.dp)
                .width(EMAIL_IMAGE_HEIGHT_WIDTH.dp)
                .padding(bottom = Padding.twenty)
                .semantics { drawableId = DrawablesIds.email }
                .testTag(tag = AuthenticationTags.EMAIL_IMAGE)
        )

        Text(
            text = stringResource(id = StringsIds.emailHasBeenSentToVerifyAccountPleaseOpenEmailSentEmailToVerifyAccount),
            modifier = Modifier
                .padding(Padding.four)
                .testTag(tag = AuthenticationTags.EMAIL_HAS_BEEN_SENT_TEXT),
            style = TextStyles.small,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Padding.four))

        ClickableText(
            text = AnnotatedString(stringResource(id = StringsIds.checkIfAccountHaBeenVerified)),
            onClick = {
                coroutineScope.launch { viewModel.onCheckIfAccountHaBeenVerifiedClicked() }
            },
            style = TextStyles.hyperLink.copy(fontSize = 16.sp, color = Color.Blue),
            modifier = Modifier.testTag(tag = AuthenticationTags.CHECK_IF_ACCOUNT_HAS_BEEN_VERIFIED_TEXT)
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        Button(
            onClick = { viewModel.onOpenEmailClicked() },
            shape = RoundedCornerShape(size = Shared.buttonDefaultShapeSize),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.eight)
                .testTag(tag = AuthenticationTags.OPEN_EMAIL_BUTTON),
            colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.openEmail),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.onResendEmailClicked()
                }
            },
            shape = RoundedCornerShape(size = Shared.buttonDefaultShapeSize),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.eight)
                .testTag(tag = AuthenticationTags.RESEND_EMAIL_BUTTON),
            colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.resendEmail),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.onDeletePendingAccountClicked()
                }
            },
            shape = RoundedCornerShape(size = Shared.buttonDefaultShapeSize),
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.eight),
            colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.deletePendingAccount),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )
    }
}
