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
    var userName: String?
        get() = prefs.getString(USERNAME, null)
        set(value) {
            prefs.edit().putString(USERNAME, value).apply()
        }

    var vendorId: String?
        get() = prefs.getString(VENDOR_ID, null)
        set(value) {
            prefs.edit().putString(VENDOR_ID, value).apply()
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

    var coverPictureUrl: String?
        get() = prefs.getString(COVER_PICTURE_URL, null)
        set(value) {
            prefs.edit().putString(COVER_PICTURE_URL, value).apply()
        }

    var name: String?
        get() = prefs.getString(NAME, null)
        set(value) {
            prefs.edit().putString(NAME, value).apply()
        }

    var role: String?
        get() = prefs.getString(ROLE, null)
        set(value) {
            prefs.edit().putString(ROLE, value).apply()
        }

    var mobile: String?
        get() = prefs.getString(MOBILE, null)
        set(value) {
            prefs.edit().putString(MOBILE, value).apply()
        }

    var email: String?
        get() = prefs.getString(EMAIL, null)
        set(value) {
            prefs.edit().putString(EMAIL, value).apply()
        }

    var businessName: String?
        get() = prefs.getString(BUSINESS_NAME, null)
        set(value) {
            prefs.edit().putString(BUSINESS_NAME, value).apply()
        }

    var coverImage: String?
        get() = prefs.getString(COVER_IMAGE, null)
        set(value) {
            prefs.edit().putString(COVER_IMAGE, value).apply()
        }
    var teamSize: String?
        get() = prefs.getString(TEAM_SIZE, null)
        set(value) {
            prefs.edit().putString(TEAM_SIZE, value).apply()
        }

    var address: String?
        get() = prefs.getString(ADDRESS, null)
        set(value) {
            prefs.edit().putString(ADDRESS, value).apply()
        }

    var isBank: String?
        get() = prefs.getString(IS_BANK, "")
        set(value) {
            prefs.edit().putString(IS_BANK, value).apply()
        }
    var accountNumber: String?
        get() = prefs.getString(ACCOUNT_NUMBER, "")
        set(value) {
            prefs.edit().putString(ACCOUNT_NUMBER, value).apply()
        }
    var accountName: String?
        get() = prefs.getString(ACCOUNT_NAME, "")
        set(value) {
            prefs.edit().putString(ACCOUNT_NAME, value).apply()
        }
    var bankName: String?
        get() = prefs.getString(BANK_NAME, "")
        set(value) {
            prefs.edit().putString(BANK_NAME, value).apply()
        }
    var ifscCode: String?
        get() = prefs.getString(IFSC_CODE, "")
        set(value) {
            prefs.edit().putString(IFSC_CODE, value).apply()
        }

    var branchName: String?
        get() = prefs.getString(BRANCH_NAME, "")
        set(value) {
            prefs.edit().putString(BRANCH_NAME, value).apply()
        }
    var onlineOffline: String?
        get() = prefs.getString(ONLINE, "")
        set(value) {
            prefs.edit().putString(ONLINE, value).apply()
        }
    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val IS_LOGIN = "IS_LOGIN"
        private const val IMAGE_URL = "IMAGE_URL"
        private const val SELECTED_LANGUAGE = "SELECTED_LANGUAGE"
        private const val PROFILE_PICTURE_URL = "PROFILE_PICTURE_URL"
        private const val COVER_PICTURE_URL = "COVER_PICTURE_URL"
        private const val USERNAME = "USERNAME"
        private const val VENDOR_ID = "VENDOR_ID"
        private const val NAME = "NAME"
        private const val ROLE = "ROLE"
        private const val MOBILE = "MOBILE"
        private const val EMAIL = "EMAIL"
        private const val BUSINESS_NAME = "BUSINESS_NAME"
        private const val COVER_IMAGE = "COVER_IMAGE"
        private const val TEAM_SIZE = "TEAM_SIZE"
        private const val ADDRESS = "ADDRESS"
        private const val IS_BANK = "IS_BANK"
        private const val ACCOUNT_NAME = "ACCOUNT_NAME"
        private const val ACCOUNT_NUMBER = "ACCOUNT_NUMBER"
        private const val BANK_NAME = "BANK_NAME"
        private const val IFSC_CODE = "IFSC_CODE"
        private const val BRANCH_NAME = "BRANCH_NAME"
        private const val ONLINE = "ONLINE"
    }
}
