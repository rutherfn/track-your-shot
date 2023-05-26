package com.nicholas.rutherford.track.my.shot.feature.create.account.authentication

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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
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
import androidx.lifecycle.Lifecycle
import com.nicholas.rutherford.track.my.shot.compose.components.Content
import com.nicholas.rutherford.track.my.shot.compose.components.DialogWithTextField
import com.nicholas.rutherford.track.my.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.my.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.my.shot.feature.splash.Colors
import com.nicholas.rutherford.track.my.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.my.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.my.shot.helper.ui.Padding
import com.nicholas.rutherford.track.my.shot.helper.ui.Shared
import com.nicholas.rutherford.track.my.shot.helper.ui.TextStyles
import com.nicholas.rutherford.track.my.shot.navigation.OnLifecycleEvent
import com.nicholas.rutherford.track.myshot.compose.content.test.rule.drawableId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val EMAIL_IMAGE_HEIGHT_WIDTH = 144

@Composable
fun AuthenticationScreen(viewModel: AuthenticationViewModel, usernameArgument: String?, emailArgument: String?) {
    val coroutineScope = rememberCoroutineScope()

    val shouldShowDialogWithTextField = viewModel.shouldShowDialogWithTextFieldFlow.collectAsState().value
    var textFieldValue by remember { mutableStateOf("") }

    if (shouldShowDialogWithTextField) {
        DialogWithTextField(
            onDismissClicked = {},
            title = stringResource(id = StringsIds.usernameInUse),
            description = stringResource(id = StringsIds.yourUsernameIsCurrentlyAlreadyBeingUsedForTrackMyShotPleaseCreateANewUsernameAndPressConfirmToContinueVerifyingAccount),
            textFieldValue = textFieldValue,
            textFieldLabelValue = stringResource(id = StringsIds.userName),
            onValueChange = {
                textFieldValue = it
            },
            confirmButton = AlertConfirmAndDismissButton(
                onButtonClicked = { viewModel.onConfirmNewUsernameClicked(newUsername = textFieldValue) },
                buttonText = stringResource(id = StringsIds.confirm)
            )
        )
    }

    viewModel.updateUsernameAndEmail(
        usernameArgument = usernameArgument,
        emailArgument = emailArgument
    )

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            coroutineScope.launch {
                viewModel.onResume()
            }
        }
    }

    Content(
        ui = {
            AuthenticationScreenContent(viewModel = viewModel, coroutineScope = coroutineScope)
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.verifyAccount),
            onIconButtonClicked = { viewModel.onNavigateClose() }
        ),
        imageVector = Icons.Filled.Close
    )
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
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
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
            colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
            content = {
                Text(
                    text = stringResource(id = StringsIds.resendEmail),
                    style = TextStyles.small,
                    color = Color.White
                )
            }
        )
    }
}
