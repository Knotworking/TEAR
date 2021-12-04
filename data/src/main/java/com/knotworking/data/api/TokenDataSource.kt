package com.knotworking.data.api

import android.content.SharedPreferences

interface TokenDataSource {
    fun getToken(): String?
    fun setToken(token: String)
}

class SharedPrefsTokenDataSource(
    private val sharedPrefs: SharedPreferences
) : TokenDataSource {
    private val tokenKey = "token"

    override fun getToken(): String? = sharedPrefs.getString(tokenKey, null)

    override fun setToken(token: String) {
        sharedPrefs.edit().putString(tokenKey, token).apply()
    }

}