package petrova.ola.playlistmaker.domain.impl

import petrova.ola.playlistmaker.domain.api.SettingInteractor
import petrova.ola.playlistmaker.domain.api.SettingRepository

class SettingInteractorImpl
    (private val settingRepository: SettingRepository) : SettingInteractor {
    override fun loadTheme(): Boolean {
        return settingRepository.loadTheme()
    }

    override fun saveTheme(value: Boolean) {
        return settingRepository.saveTheme(value = value)
    }
}