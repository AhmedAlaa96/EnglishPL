package com.ahmed.english_pl.data.shared_prefrences

import android.content.Context
import android.content.SharedPreferences
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.utils.Constants
import com.ahmed.english_pl.utils.Constants.SharedPreference.MATCHES_LIST
import com.ahmed.english_pl.utils.Constants.SharedPreference.SHARED_PREF_NAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesDataSource(context: Context, private val mGson: Gson) : IPreferencesDataSource {

    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = mPrefs.edit()

    private fun removeString(key: String) {
        editor.remove(key)
        editor.commit()
    }

    private fun setString(key: String, value: String) {
        mPrefs.edit().putString(key, value).apply()
    }

    private fun setInt(key: String, value: Int) {
        mPrefs.edit().putInt(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String): String? {
        return mPrefs.getString(key, defaultValue)
    }

    private fun getInt(key: String, defaultValue: Int): Int {
        return mPrefs.getInt(key, defaultValue)
    }

    override fun loadFavoriteMatches(): ArrayList<Match> {
        val matchString =
            getString(MATCHES_LIST, Constants.General.EMPTY_TEXT)

        return if (matchString.isNullOrEmpty()) arrayListOf()
        else {
            val listType = object : TypeToken<ArrayList<Match>>() {}.type
            return mGson.fromJson(matchString, listType)
        }
    }

    override fun insertMatch(match: Match?) {
        val loadFavoriteMatches = loadFavoriteMatches()
        if (match != null) {
            loadFavoriteMatches.add(match)
            setString(MATCHES_LIST, mGson.toJson(loadFavoriteMatches))
        }
    }

    override fun deleteMatch(match: Match?) {
        val loadFavoriteMatches = loadFavoriteMatches()
        if (match != null) {
            loadFavoriteMatches.removeIf { favMatch -> match.id == favMatch.id }
            setString(MATCHES_LIST, mGson.toJson(loadFavoriteMatches))
        }
    }


}