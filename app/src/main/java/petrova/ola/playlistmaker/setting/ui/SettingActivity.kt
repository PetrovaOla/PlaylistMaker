package petrova.ola.playlistmaker.setting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.databinding.ActivitySettingsBinding

class SettingActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.isDarkTheme.observe(this) {
//            if (binding.themeSwitcher.isChecked != it)
                binding.themeSwitcher.isChecked = it

        }


        binding.toolbarSetting.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeTheme(checked)
        }

        binding.supportButton.setOnClickListener {
            viewModel.support()
        }
        binding.shareButton.setOnClickListener {
            viewModel.shareApp()
        }
        binding.userAgreementButton.setOnClickListener {
            viewModel.userAgreement()
        }

    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java)
    }
}