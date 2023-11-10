package com.nicholas.rutherford.track.your.shot.feature.create.account.authentication

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AuthenticationTagsTest {

    @Nested
    inner class Constants {

        @Test
        fun `check if account has been verified text`() {
            Assertions.assertEquals(AuthenticationTags.CHECK_IF_ACCOUNT_HAS_BEEN_VERIFIED_TEXT, "check_if_account_has_been_verified_text")
        }

        @Test
        fun `email image`() {
            Assertions.assertEquals(AuthenticationTags.EMAIL_IMAGE, "email_image")
        }

        @Test
        fun `email has been sent text`() {
            Assertions.assertEquals(AuthenticationTags.EMAIL_HAS_BEEN_SENT_TEXT, "email_has_been_sent_text")
        }

        @Test
        fun `open email button`() {
            Assertions.assertEquals(AuthenticationTags.OPEN_EMAIL_BUTTON, "open_email_button")
        }

        @Test
        fun `resend email button`() {
            Assertions.assertEquals(AuthenticationTags.RESEND_EMAIL_BUTTON, "resend_email_button")
        }
    }
}
