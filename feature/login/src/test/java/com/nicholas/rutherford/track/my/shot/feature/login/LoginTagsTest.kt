package com.nicholas.rutherford.track.my.shot.feature.login

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class LoginTagsTest {

    @Nested
    inner class Constants {

        @Test
        fun `login app image`() {
            Assertions.assertEquals(LoginTags.LOGIN_APP_IMAGE, "login_app_image")
        }

        @Test
        fun `proceed with your account text`() {
            Assertions.assertEquals(LoginTags.PROCEED_WITH_YOUR_ACCOUNT_TEXT, "proceed_with_your_account_text")
        }

        @Test
        fun `login text`() {
            Assertions.assertEquals(LoginTags.LOGIN_TEXT, "login_text")
        }

        @Test
        fun `email text field`() {
            Assertions.assertEquals(LoginTags.EMAIL_TEXT_FIELD, "email_text_field")
        }

        @Test
        fun `password text field`() {
            Assertions.assertEquals(LoginTags.PASSWORD_TEXT_FIELD, "password_text_field")
        }

        @Test
        fun `login button`() {
            Assertions.assertEquals(LoginTags.LOGIN_BUTTON, "login_button")
        }

        @Test
        fun `login button text`() {
            Assertions.assertEquals(LoginTags.LOGIN_BUTTON_TEXT, "login_button_text")
        }

        @Test
        fun `forgot password text`() {
            Assertions.assertEquals(LoginTags.FORGOT_PASSWORD_TEXT, "forgot_password_text")
        }

        @Test
        fun `click me to create account text`() {
            Assertions.assertEquals(LoginTags.CLICK_ME_TO_CREATE_ACCOUNT_TEXT, "click_me_to_create_account_text")
        }
    }
}
