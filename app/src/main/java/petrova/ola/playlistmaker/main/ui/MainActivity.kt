package petrova.ola.playlistmaker.main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.databinding.ActivityMainBinding
import petrova.ola.playlistmaker.media.ui.MediaActivity
import petrova.ola.playlistmaker.search.ui.SearchActivity
import petrova.ola.playlistmaker.setting.ui.SettingActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        viewModel.navigate.observe(this) { navigateTo(it) }


        binding.searchButton.setOnClickListener {
            viewModel.openSearch()
        }
        binding.mediaLibraryButton.setOnClickListener {
            viewModel.openMediateka()
        }

        val clickListener: View.OnClickListener = View.OnClickListener {
            viewModel.openSettings()
        }

        binding.settingsButton.setOnClickListener(clickListener)
    }

    private fun navigateTo(it: MainNavigate) {
        when (it) {
            MainNavigate.SEARCH -> {
                val searchIntent = Intent(this, SearchActivity::class.java)
                startActivity(searchIntent)
            }

            MainNavigate.MEDIA -> {
                val mediaIntent = Intent(this, MediaActivity::class.java)
                startActivity(mediaIntent)
            }

            MainNavigate.SETTING -> {
                val settingIntent = Intent(applicationContext, SettingActivity::class.java)
                startActivity(settingIntent)
            }

            MainNavigate.NONE -> {}
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}