package petrova.ola.playlistmaker.data.repository

import android.content.SharedPreferences
import petrova.ola.playlistmaker.domain.api.SettingRepository
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