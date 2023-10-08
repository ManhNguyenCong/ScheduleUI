package com.example.scheduleui.ui.setting.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Name of name setting's preference
private const val NAME_SETTING = "name_setting"

// Name of school setting's preference
private const val SCHOOL_SETTING = "school_setting"

// Name of schedule's type setting
private const val TYPE_OF_SCHEDULE_SETTING = "type_of_schedule_setting"

// Name of schedule display's type setting
private const val TYPE_OF_DISPLAY_SETTING = "type_of_display_setting"

// Name of subject display's type setting
private const val SUBJECT_DISPLAY_SETTING = "subject_display_setting"

class SettingViewModel(private val application: Application) : ViewModel() {

    // DATA STORE
    // Data store to save name setting
    private val Context.dataStoreNameSetting: DataStore<Preferences> by preferencesDataStore(name = NAME_SETTING)

    // Data store to save school setting
    private val Context.dataStoreSchoolSetting: DataStore<Preferences> by preferencesDataStore(name = SCHOOL_SETTING)

    // Data store to save type of schedule setting
    private val Context.dataStoreTypeOfScheduleSetting: DataStore<Preferences> by preferencesDataStore(
        name = TYPE_OF_SCHEDULE_SETTING
    )

    // Data store to save type of schedule display setting
    private val Context.dataStoreTypeOfDisplaySetting: DataStore<Preferences> by preferencesDataStore(
        name = TYPE_OF_DISPLAY_SETTING
    )

    // Data store to save type of subject display setting
    private val Context.dataStoreSubjectDisplay: DataStore<Preferences> by preferencesDataStore(
        name = SUBJECT_DISPLAY_SETTING
    )

    // KEY OF PREFERENCES
    // Get key of name setting preference
    private val nameSettingPreference = stringPreferencesKey(NAME_SETTING)

    // Get key of school setting preference
    private val schoolSettingPreferences = stringPreferencesKey(SCHOOL_SETTING)

    // Get key of schedule's type
    private val typeOfScheduleSettingPreferences = stringPreferencesKey(TYPE_OF_SCHEDULE_SETTING)

    // Get key of schedule display's type
    private val typeOfDisplaySettingPreferences = stringPreferencesKey(TYPE_OF_DISPLAY_SETTING)

    // Get key of subject display's type
    private val subjectDisplaySettingPreferences = stringPreferencesKey(SUBJECT_DISPLAY_SETTING)

    // GET
    // Get name setting
    val nameSetting: LiveData<String> =
        application.applicationContext.dataStoreNameSetting.data.map { preferences ->
            preferences[nameSettingPreference] ?: ""
        }.asLiveData()

    // Get school setting
    val schoolSetting: LiveData<String> =
        application.applicationContext.dataStoreSchoolSetting.data.map { preferences ->
            preferences[schoolSettingPreferences] ?: ""
        }.asLiveData()

    // Get type of schedule setting
    val typeOfScheduleSetting: LiveData<String> =
        application.applicationContext.dataStoreTypeOfScheduleSetting.data.map { preferences ->
            preferences[typeOfScheduleSettingPreferences] ?: ""
        }.asLiveData()

    // Get type of schedule display setting
    val typeOfDisplaySetting: LiveData<String> =
        application.applicationContext.dataStoreTypeOfDisplaySetting.data.map { preferences ->
            preferences[typeOfDisplaySettingPreferences] ?: ""
        }.asLiveData()

    // Get type of subject display setting
    val subjectDisplaySetting: LiveData<String> =
        application.applicationContext.dataStoreSubjectDisplay.data.map { preferences ->
            preferences[subjectDisplaySettingPreferences] ?: ""
        }.asLiveData()

    // PRIVATE SET
    /**
     * This function is used to save name setting to data store
     *
     * @param name
     */
    private suspend fun setName(name: String) {
        application.applicationContext.dataStoreNameSetting.edit { settings ->
            settings[nameSettingPreference] = name
        }
    }

    /**
     * This function is used to save school setting to data store
     *
     * @param school
     */
    private suspend fun setSchool(school: String) {
        application.applicationContext.dataStoreSchoolSetting.edit { settings ->
            settings[schoolSettingPreferences] = school
        }
    }

    /**
     * This function is used to save type of schedule to data store
     *
     * @param typeOfSchedule
     */
    private suspend fun setTypeOfSchedule(typeOfSchedule: String) {
        application.applicationContext.dataStoreTypeOfScheduleSetting.edit { preferences ->
            preferences[typeOfScheduleSettingPreferences] = typeOfSchedule
        }
    }

    /**
     * This function is used to save type of schedule display to data store
     *
     * @param typeOfDisplay
     */
    private suspend fun setTypeOfDisplay(typeOfDisplay: String) {
        application.applicationContext.dataStoreTypeOfDisplaySetting.edit { preferences ->
            preferences[typeOfDisplaySettingPreferences] = typeOfDisplay
        }
    }

    /**
     * This function is used to save type of subject display to data store
     *
     * @param subjectDisplay
     */
    private suspend fun setSubjectDisplay(subjectDisplay: String) {
        application.applicationContext.dataStoreSubjectDisplay.edit { preferences ->
            preferences[subjectDisplaySettingPreferences] = subjectDisplay
        }
    }

    // PUBLIC SET
    /**
     * This function is used to save new name
     *
     * @param name
     */
    fun setNewName(name: String) {
        viewModelScope.launch {
            setName(name)
        }
    }

    /**
     * This function is used to save new school setting to data store
     *
     * @param school
     */
    fun setNewSchool(school: String) {
        viewModelScope.launch {
            setSchool(school)
        }
    }

    /**
     * This function is used to save a new type of schedule setting
     *
     * @param typeOfSchedule
     */
    fun setNewTypeOfSchedule(typeOfSchedule: String) {
        viewModelScope.launch {
            setTypeOfSchedule(typeOfSchedule)
        }
    }

    /**
     * This function is used to save type of schedule display setting
     *
     * @param typeOfDisplay
     */
    fun setNewTypeOfDisplay(typeOfDisplay: String) {
        viewModelScope.launch {
            setTypeOfDisplay(typeOfDisplay)
        }
    }

    /**
     * This function is used to save type of subject display setting
     *
     * @param subjectDisplay
     */
    fun setNewSubjectDisplay(subjectDisplay: String) {
        viewModelScope.launch {
            setSubjectDisplay(subjectDisplay)
        }
    }
}

class SettingViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            SettingViewModel(application) as T
        } else {
            super.create(modelClass)
        }
    }
}