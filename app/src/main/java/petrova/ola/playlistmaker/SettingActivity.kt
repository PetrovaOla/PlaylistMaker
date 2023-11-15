package petrova.ola.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import petrova.ola.playlistmaker.databinding.ActivitySettingsBinding


class SettingActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val switchSetting = binding.switchSetting

        switchSetting.setChecked(
            when (resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
                UI_MODE_NIGHT_YES -> true
                UI_MODE_NIGHT_NO -> false
                else -> throw IllegalStateException()
            }
        )
        switchSetting.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }

        binding.toolbarSetting.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.supportTv.setOnClickListener {
            val mailSubject = resources.getString(R.string.messageSubject)
            val mailBody = getResources().getString(R.string.message)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ola.mzsk@ya.ru"))
            shareIntent.putExtra(Intent.EXTRA_TEXT, mailSubject)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mailBody)

            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(shareIntent)
            } else {
                val error = resources.getString(R.string.message_error)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
        binding.userAgreementTv.setOnClickListener {
            val url = resources.getString(R.string.url)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))


            if (browserIntent.resolveActivity(packageManager) != null) {
                startActivity(browserIntent)
            } else {
                val error = resources.getString(R.string.message_error)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
        binding.shareTv.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareContent = getString(R.string.share_app_text)
            intent.setType("text/plain")

            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            intent.putExtra(Intent.EXTRA_TEXT, shareContent)
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)))
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java)
    }
}