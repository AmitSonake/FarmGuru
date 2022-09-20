package com.example.dogs.util

import android.content.Context
import android.content.SharedPreferences

import androidx.core.content.edit

import androidx.preference.PreferenceManager

class SharedPreferencesHelper {

    companion object {
        private var prefs: SharedPreferences? = null
        private const val PREF_TIME = "Pref time"
        private const val USER_TOKEN = "Token"
        private const val USER_NAME = "Username"
        private const val USER_ACTIVE = "Useractive"
        private const val USER_LANGUAGE = "user_language"
        private const val SELECTED_LANGUAGE = "selected_language"

        @Volatile
        private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper =
            instance ?: synchronized(LOCK) {
                instance ?: buildHelper(context).also {
                    instance = it
                }
            }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }


    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true){
            putLong(PREF_TIME, time)
        }

    }
    fun saveUserToken(token: String?) {
        prefs?.edit(commit = true){
            putString(USER_TOKEN, token)
        }

    }
    fun saveUserName(userName: String?) {
        prefs?.edit(commit = true){
            putString(USER_NAME, userName)
        }

    }
    fun saveUserLanguage(languageID: String?) {
        prefs?.edit(commit = true){
            putString(USER_LANGUAGE, languageID)
        }

    }
    fun saveSelectedLanguage(languageID: String?) {
        prefs?.edit(commit = true){
            putString(SELECTED_LANGUAGE, languageID)
        }

    }
    fun getSelectedLanguage()=prefs?.getString(SELECTED_LANGUAGE,null)
    fun getUserLanguage()=prefs?.getString(USER_LANGUAGE,null)
    fun getToken()=prefs?.getString(USER_TOKEN,null)
    fun getUserName()=prefs?.getString(USER_NAME,null)
    fun getUpdateTime()=prefs?.getLong(PREF_TIME,0)
    fun getCacheDuration()=prefs?.getString("pref_cache_duration","")
    fun saveUserLoggedIn(isActive: Boolean) {
        prefs?.edit(commit = true){
            putBoolean(USER_ACTIVE, isActive)
        }
    }
    fun getUserLoggedIn()=prefs?.getBoolean(USER_ACTIVE,false)
}