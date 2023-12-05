package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreatePlayerViewModel(
    private val application: Application,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val scope: CoroutineScope,
    private val navigation: CreatePlayerNavigation,
    private val network: Network
) : ViewModel() {

    internal val createPlayerMutableStateFlow = MutableStateFlow(value = CreatePlayerState())
    val createPlayerStateFlow = createPlayerMutableStateFlow.asStateFlow()

    fun onToolbarMenuClicked() = navigation.pop()

    fun onImageUploadClicked(uri: Uri?) {
        if (uri == null) {
            createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(
                sheet = Sheet(
                    title = application.getString(StringsIds.chooseOption),
                    values = listOf(
                        application.getString(StringsIds.chooseImageFromGallery),
                        application.getString(StringsIds.takeAPicture)
                    )
                )
            )
        } else {
            createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(
                sheet = Sheet(
                    title = application.getString(StringsIds.chooseOption),
                    values = listOf(application.getString(StringsIds.removeImage))
                )
            )
        }
    }

    fun onSelectedCreateEditImageOption(option: String): CreateEditImageOption {
        return when (option) {
            application.getString(StringsIds.chooseImageFromGallery) -> { CreateEditImageOption.CHOOSE_IMAGE_FROM_GALLERY }
            application.getString(StringsIds.takeAPicture) -> { CreateEditImageOption.TAKE_A_PICTURE }
            application.getString(StringsIds.removeImage) -> { CreateEditImageOption.REMOVE_IMAGE }
            else -> { CreateEditImageOption.CANCEL }
        }
    }

    fun onCreatePlayerClicked(uri: Uri?) {
    }

    fun onFirstNameValueChanged(newFirstName: String) {
        createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(firstName = newFirstName)
    }

    fun onLastNameValueChanged(newLastName: String) {
        createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(lastName = newLastName)
    }
}
