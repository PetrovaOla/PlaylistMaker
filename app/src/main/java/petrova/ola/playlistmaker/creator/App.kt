package petrova.ola.playlistmaker.creator

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import petrova.ola.playlistmaker.di.dataModule
import petrova.ola.playlistmaker.di.interactorModule
import petrova.ola.playlistmaker.di.repositoryModule
import petrova.ola.playlistmaker.di.viewModelModule
import petrova.ola.playlistmaker.setting.domain.SettingInteractor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
        switchTheme()
    }

    private fun switchTheme() {
        val settingInteractor: SettingInteractor by inject()
        settingInteractor.applyTheme()
    }

}
