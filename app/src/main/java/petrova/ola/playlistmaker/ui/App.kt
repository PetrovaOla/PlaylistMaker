package petrova.ola.playlistmaker.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false
    lateinit var appPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        appPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        darkTheme = appPreferences.getBoolean(NIGHT_MODE, false)
        switchTheme(darkTheme)
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    companion object{
        const val APP_PREFERENCES = "app_preferences"
        const val NIGHT_MODE = "NIGHT_MODE"
    }
}
