package petrova.ola.playlistmaker.setting.domain

class SettingInteractorImpl
    (private val settingRepository: SettingRepository) : SettingInteractor {
    override fun loadTheme(): Boolean {
        return settingRepository.loadTheme()
    }

    override fun saveTheme(value: Boolean) {
        return settingRepository.saveTheme(value = value)
    }
}