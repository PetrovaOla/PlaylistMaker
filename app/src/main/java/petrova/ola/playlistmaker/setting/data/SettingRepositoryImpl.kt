package petrova.ola.playlistmaker.setting.data

import android.content.SharedPreferences
import petrova.ola.playlistmaker.setting.domain.SettingRepository
import petrova.ola.playlistmaker.utils.Utils.Companion.DARK_THEME

class SettingRepositoryImpl
    (private val sharedPreferences: SharedPreferences) : SettingRepository {

    override fun saveTheme(value: Boolean) {
        return sharedPreferences.edit().putBoolean(DARK_THEME, value).apply()
    }

    override fun loadTheme(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME, false)
    }


}