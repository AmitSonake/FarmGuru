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
        private const val USER_PASSWORD = "password"
        private const val USER_ACTIVE = "Useractive"
        private const val USER_LANGUAGE = "user_language"
        private const val SELECTED_LANGUAGE = "selected_language"
        private const val USER_EMAIL = "Useremail"
        private const val USER_PHONE_NO= "UserPhoneNo"

        private const val NEW_READ_NOTE_ID = "new_note_id"
        private const val OLD_READ_NOTE_ID= "old_note_id"

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

    fun saveUserEmail(userEmail: String?) {
        prefs?.edit(commit = true){
            putString(USER_EMAIL, userEmail)
        }

    }

    fun saveUserPhoneNo(userPhone: String?) {
        prefs?.edit(commit = true){
            putString(USER_PHONE_NO, userPhone)
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

    fun saveNewNoteID(newNoteID: Int) {
        prefs?.edit(commit = true){
            putInt(NEW_READ_NOTE_ID, newNoteID)
        }

    }
    fun saveOldNoteID(oldNoteID: Int) {
        prefs?.edit(commit = true){
            putInt(OLD_READ_NOTE_ID, oldNoteID)
        }

    }
    fun getNewNoteID()=prefs?.getInt(NEW_READ_NOTE_ID,0)
    fun getOldNoteID()=prefs?.getInt(OLD_READ_NOTE_ID,0)
    fun getUserEmail()=prefs?.getString(USER_EMAIL,null)
    fun getUserPhoneNo()=prefs?.getString(USER_PHONE_NO,null)
    fun getSelectedLanguage()=prefs?.getString(SELECTED_LANGUAGE,null)
    fun getUserLanguage()=prefs?.getString(USER_LANGUAGE,null)
    fun getToken()=prefs?.getString(USER_TOKEN,null)
    fun getUserName()=prefs?.getString(USER_NAME,null)
    fun getUpdateTime()=prefs?.getLong(PREF_TIME,0)
    fun getCacheDuration()=prefs?.getString("pref_cache_duration","")
    fun getUserPassword()=prefs?.getString(USER_PASSWORD,null)
    fun saveUserLoggedIn(isActive: Boolean) {
        prefs?.edit(commit = true){
            putBoolean(USER_ACTIVE, isActive)
        }
    }
    fun getUserLoggedIn()=prefs?.getBoolean(USER_ACTIVE,false)
    fun saveUserPassword(password: String?) {
        prefs?.edit(commit = true){
            putString(USER_PASSWORD, password)
        }
    }
}