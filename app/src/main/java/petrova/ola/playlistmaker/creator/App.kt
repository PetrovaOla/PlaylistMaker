package petrova.ola.playlistmaker.creator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        switchTheme()
    }

    private fun switchTheme() {
        val settingInteractor = Creator.provideSettingInteractor(this)
        if (settingInteractor.loadTheme()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}
