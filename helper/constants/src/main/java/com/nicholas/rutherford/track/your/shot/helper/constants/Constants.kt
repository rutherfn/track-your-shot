package com.nicholas.rutherford.track.your.shot.helper.constants

object Constants {
    object Preferences {
        const val TRACK_MY_SHOT_PREFERENCES = "TRACK_MY_SHOT_PREFERENCES"

        const val APP_HAS_LAUNCHED = "appHasLaunched"
        const val IS_LOGGED_IN = "isLoggedIn"
        const val HAS_AUTHENTICATED_ACCOUNT = "hasAuthenticatedAccount"
        const val SHOULD_SHOW_TERM_AND_CONDITIONS = "shouldShowTermsAndConditions"
        const val SHOULD_UPDATE_LOGGED_IN_PLAYER_LIST = "shouldUpdateLoggedInPlayerList"
        const val SHOULD_UPDATE_LOGGED_IN_DECLARED_SHOT_LIST = "shouldUpdateLoggedInDeclaredShotList"
    }
    const val CREATE_REPORT_INDEX = 0
    const val APP_DATABASE_NAME = "app_database.db"
    const val ACTIVE_USER_ID = 1
    const val ACCOUNT_INFO = "accountInfo"
    const val CENTER = 4
    const val CONTENT_LAST_UPDATED_PATH = "contentLastUpdated"
    const val DATE_PATTERN = "MMMM dd, yyyy"
    const val DEFAULT_SHOT_ID = 0
    const val DELAY_IN_MILLISECONDS_BEFORE_LOGGING_OUT = 1000L
    const val DELAY_IN_MILLISECONDS_TO_SHOW_PROGRESS_MASK_ON_LOG_OUT = 3000L
    const val EMAIL = "email"
    const val FIREBASE_CHILDREN_COUNT_ZERO = 0L
    const val FIRST_NAME = "firstName"
    const val IMAGE = "image/*"
    const val IMAGES = "images"
    const val IMAGES_DIR = "/images/"
    const val IMAGE_URL = "imageUrl"
    const val LAST_NAME = "lastName"
    const val LAST_UPDATED = "lastUpdated"
    const val LOGGED_DATE_VALUE = "loggedDateValue"
    const val NOTIFICATION_CHANNEL_ID = "track_your_shot_notification"
    const val NOTIFICATION_NAME = "track_your_shot"
    const val PDFS = "pdfs"
    const val PDF_MIME_TYPE = "application/pdf"
    const val PDF_SUCCESSFUL_GENERATE_CODE = 1
    const val PDF_URL = "pdfUrl"
    const val PDF_CANNOT_SAVE_PDF_CODE = 2
    const val PDF_CANNOT_CREATE_PDF_CODE = 3
    const val PENDING_PLAYERS_EXPECTED_SIZE = 1
    const val PLAYER_NAME = "playerName"
    const val PLAYERS = "players"
    const val PLAYERS_INDIVIDUAL_REPORTS = "playersIndividualReports"
    const val POP_DEFAULT_ACTION = "pop"
    const val POSITION_VALUE = "positionValue"
    const val POSITION_NA = 5
    const val SHOT_ZERO_VALUE = 0.0
    const val USERS_PATH = "users"
    const val POINT_GUARD_VALUE = 0
    const val SHOOTING_GUARD_VALUE = 1
    const val SHOTS_LOGGED = "shotsLogged"
    const val SMALL_FORWARD_VALUE = 2
    const val POWER_FORWARD_VALUE = 3
    const val USERNAME = "userName"
    const val USERS = "users"
}
