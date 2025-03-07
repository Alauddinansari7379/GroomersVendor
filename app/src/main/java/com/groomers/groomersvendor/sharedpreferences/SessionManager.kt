package com.groomers.groomersvendor.sharedpreferences
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN, null)
        set(value) {
            prefs.edit().putString(ACCESS_TOKEN, value).apply()
        }

    var imageUrl: String?
        get() = prefs.getString(IMAGE_URL, null)
        set(value) {
            prefs.edit().putString(IMAGE_URL, value).apply()
        }

    var isLogin: Boolean
        get() = prefs.getBoolean(IS_LOGIN, false)
        set(value) {
            prefs.edit().putBoolean(IS_LOGIN, value).apply()
        }

//    fun clearSession() {
//        prefs.edit().clear().apply()
//    }

    fun clearSession() {
        val language = selectedLanguage // Store language before clearing
        prefs.edit().clear().apply() // Clear everything
        selectedLanguage = language // Restore language
    }

    var selectedLanguage: String?
        get() = prefs.getString(SELECTED_LANGUAGE, null)
        set(value) {
            prefs.edit().putString(SELECTED_LANGUAGE, value).apply()
        }
    var profilePictureUrl: String?
        get() = prefs.getString(PROFILE_PICTURE_URL, null)
        set(value) {
            prefs.edit().putString(PROFILE_PICTURE_URL, value).apply()
        }
    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val IS_LOGIN = "IS_LOGIN"
        private const val IMAGE_URL = "IMAGE_URL"
        private const val SELECTED_LANGUAGE = "SELECTED_LANGUAGE"
        private const val PROFILE_PICTURE_URL = "PROFILE_PICTURE_URL"
    }
}
