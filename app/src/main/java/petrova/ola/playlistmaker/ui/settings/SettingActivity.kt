package petrova.ola.playlistmaker.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ActivitySettingsBinding
import petrova.ola.playlistmaker.ui.App
import petrova.ola.playlistmaker.ui.App.Companion.APP_PREFERENCES
import petrova.ola.playlistmaker.ui.App.Companion.NIGHT_MODE


class SettingActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val app = applicationContext as App
        themeSwitcher = binding.themeSwitcher
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        binding.toolbarSetting.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        themeSwitcher.setChecked(app.darkTheme)
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            app.switchTheme(checked)
        }

        binding.supportButton.setOnClickListener {
            val mailSubject = resources.getString(R.string.messageSubject)
            val mailBody = getResources().getString(R.string.message)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse(getString(R.string.mailto))
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            shareIntent.putExtra(Intent.EXTRA_TEXT, mailSubject)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mailBody)

            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(shareIntent)
            } else {
                val error = resources.getString(R.string.message_error)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
        binding.shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareContent = getString(R.string.share_app_text)
            intent.setType(getString(R.string.text_plain))

            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            intent.putExtra(Intent.EXTRA_TEXT, shareContent)
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
        }
        binding.userAgreementButton.setOnClickListener {
            val url = resources.getString(R.string.url)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))


            if (browserIntent.resolveActivity(packageManager) != null) {
                startActivity(browserIntent)
            } else {
                val error = resources.getString(R.string.message_error)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onStop() {
        super.onStop()
        preferences.edit().putBoolean(NIGHT_MODE, themeSwitcher.isChecked).apply()
    }


    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java)
    }
}