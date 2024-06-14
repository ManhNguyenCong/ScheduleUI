package com.example.scheduleui.data.localdatabase

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map

private const val SETTING_PREFERENCES = "come.example.scheduleui.setting_preferences"
private const val NAME_PREFERENCE = "setting_prefs_name"
private const val SCHOOL_PREFERENCE = "setting_prefs_school"
private const val OPACITY_PREFERENCE = "setting_prefs_opacity"

class SettingPreferences(private val context: Context) {
    private val Context.dataStoreSetting: DataStore<Preferences> by preferencesDataStore(
        name = SETTING_PREFERENCES
    )

    private val namePreference = stringPreferencesKey(NAME_PREFERENCE)
    private val schoolPreference = stringPreferencesKey(SCHOOL_PREFERENCE)
    private val opacityPreference = floatPreferencesKey(OPACITY_PREFERENCE)

    val nameSetting: LiveData<String?> = context.dataStoreSetting.data.map { prefs ->
        prefs[namePreference]
    }.asLiveData()

    val schoolSetting: LiveData<String?> = context.dataStoreSetting.data.map { prefs ->
        prefs[schoolPreference]
    }.asLiveData()

    val opacitySetting: LiveData<Float?> = context.dataStoreSetting.data.map { prefs ->
        prefs[opacityPreference]
    }.asLiveData()

    suspend fun setName(name: String) {
        context.dataStoreSetting.edit { prefs ->
            prefs[namePreference] = name
        }
    }

    suspend fun setSchool(school: String) {
        context.dataStoreSetting.edit { prefs ->
            prefs[schoolPreference] = school
        }
    }

    suspend fun setOpacity(opacity: Float) {
        context.dataStoreSetting.edit { prefs ->
            prefs[opacityPreference] = opacity
        }
    }
}