package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

enum class AccountInfoChanges(description: String?) {
    HAS_MADE_NO_CHANGES(description = "We have detected np changes. Please make changes to either username or email to continue."),
    MISSING_NEW_EMAIL(description = "Confirm new email fields are not matching. Please enter the same email, to confirm changes."),
    MISSING_CONFIRM_NEW_EMAIL(description = "Confirm new email fields are not matching. Please enter the same email, to confirm changes."),
    EMAIL_IS_NOT_A_VALID_EMAIL(description = "This email is not valid. Please enter a new valid email"),
    EMAIL_ALREADY_EXISTS_IN_APP(description = "That email already exists under another account. Please enter a new email"),
    MISSING_NEW_USERNAME(description = "Confirm new username fields are not matching. Please enter the same username, to confirm changes."),
    MISSING_CONFIRM_NEW_USERNAME(description = "Confirm new username fields are not matching. Please enter the same username, to confirm changes."),
    USERNAME_IS_NOT_A_VALID_USERNAME(description = "This username is not valid. Please enter a new valid username"),
    USERNAME_ALREADY_EXISTS_IN_APP(description = "That username already exists under another account. Please enter a new username"),
    HAS_VALID_CHANGES_FOR_USERNAME(description = null),
    HAS_VALID_CHANGES_FOR_EMAIL(description = null),
    HAS_VALID_CHANGES_FOR_USERNAME_AND_EMAIL(description = null)
}