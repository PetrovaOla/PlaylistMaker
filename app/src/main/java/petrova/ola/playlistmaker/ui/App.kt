package petrova.ola.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import petrova.ola.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        switchTheme()
    }

    private fun switchTheme() {
        val settingInteractor = Creator.provideSettingInteractor(this)
        if (settingInteractor.darkTheme()) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}
