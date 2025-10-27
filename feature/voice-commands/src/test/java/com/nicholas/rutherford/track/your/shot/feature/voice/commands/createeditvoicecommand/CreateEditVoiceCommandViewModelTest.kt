package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.test.BaseTest
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateEditVoiceCommandViewModelTest : BaseTest() {

    private lateinit var viewModel: CreateEditVoiceCommandViewModel

    private var savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()
    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)
    private val updateFirebaseUserInfo = mockk<UpdateFirebaseUserInfo>(relaxed = true)
    private val deleteFirebaseUserInfo = mockk<DeleteFirebaseUserInfo>(relaxed = true)
    private val savedVoiceCommandRepository = mockk<SavedVoiceCommandRepository>(relaxed = true)
    private val navigation = mockk<CreateEditVoiceCommandNavigation>(relaxed = true)

    private val recordedPhraseParam = "recorded phrase"

    @BeforeEach
    fun beforeEach() {

        every { savedStateHandle.get<Int>("voiceCommandTypeValueParam") } returns 2
        every { savedStateHandle.get<String>("recordedPhraseParam") } returns recordedPhraseParam

        viewModel = CreateEditVoiceCommandViewModel(
            savedStateHandle = savedStateHandle,
            application = application,
            scope = scope,
            createFirebaseUserInfo = createFirebaseUserInfo,
            updateFirebaseUserInfo = updateFirebaseUserInfo,
            deleteFirebaseUserInfo = deleteFirebaseUserInfo,
            savedVoiceCommandRepository = savedVoiceCommandRepository,
            navigation = navigation
        )
    }

    @Test
    fun `on toolbar menu clicked should call pop`() {
        viewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }
}