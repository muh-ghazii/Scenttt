package com.contoh.scentapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Menyimpan sesi login agar akun tidak logout saat aplikasi ditutup.
 */
class SessionManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME     = "scent_session"
        private const val KEY_IS_LOGGED  = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_NAME  = "user_name"
        private const val KEY_LAST_OPEN  = "last_open_ms"

        @Volatile private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also { INSTANCE = it }
            }
    }

    /** Panggil saat login/register berhasil */
    fun saveSession(email: String, name: String = "") {
        prefs.edit {
            putBoolean(KEY_IS_LOGGED, true)
            putString(KEY_USER_EMAIL, email)
            putString(KEY_USER_NAME, name)
            putLong(KEY_LAST_OPEN, System.currentTimeMillis())
        }
    }

    /** Panggil saat tombol Logout ditekan */
    fun clearSession() {
        prefs.edit {
            putBoolean(KEY_IS_LOGGED, false)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_NAME)
        }
    }

    /** Update waktu terakhir buka */
    fun updateLastOpen() {
        prefs.edit { putLong(KEY_LAST_OPEN, System.currentTimeMillis()) }
    }

    val isLoggedIn: Boolean  get() = prefs.getBoolean(KEY_IS_LOGGED, false)
    val userEmail : String   get() = prefs.getString(KEY_USER_EMAIL, "") ?: ""
    val userName  : String   get() = prefs.getString(KEY_USER_NAME, "") ?: ""
    val lastOpenMs: Long     get() = prefs.getLong(KEY_LAST_OPEN, 0L)
}
