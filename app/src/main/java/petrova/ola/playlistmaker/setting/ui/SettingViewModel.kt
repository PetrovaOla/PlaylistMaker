package petrova.ola.playlistmaker.setting.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import petrova.ola.playlistmaker.creator.Creator
import petrova.ola.playlistmaker.setting.domain.SettingInteractor
import petrova.ola.playlistmaker.sharing.domain.SharingInteractor

class SettingViewModel(
    private val settingInteractor: SettingInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    init {
        _isDarkTheme.value = settingInteractor.loadTheme()
    }

    fun changeTheme(value: Boolean) {
        if (_isDarkTheme.value != value) {
            _isDarkTheme.value = value
            settingInteractor.saveTheme(value)
            if (value) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun support() {
        sharingInteractor.openSupport()
    }

    fun userAgreement() {
        sharingInteractor.userAgreement()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SettingViewModel(
                    settingInteractor = Creator.provideSettingInteractor(application),
                    sharingInteractor = Creator.provideSharingInteractor(application)
                )
            }
        }
    }
}