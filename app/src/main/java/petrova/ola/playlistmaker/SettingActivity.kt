package petrova.ola.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_UNDEFINED
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import java.lang.IllegalStateException


class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSetting)
        val supportTv = findViewById<TextView>(R.id.supportTv)
        val userAgreementTv = findViewById<TextView>(R.id.user_agreementTv)
        val shareTv = findViewById<TextView>(R.id.shareTv)
        val switchSetting = findViewById<SwitchCompat>(R.id.switchSetting)


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

        toolbar.setNavigationOnClickListener {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
        }
        supportTv.setOnClickListener {
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
        userAgreementTv.setOnClickListener {
            val url = resources.getString(R.string.url)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))


            if (browserIntent.resolveActivity(packageManager) != null) {
                startActivity(browserIntent)
            } else {
                val error = resources.getString(R.string.message_error)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
        shareTv.setOnClickListener {
            val intent = Intent.makeMainSelectorActivity(
                    Intent.ACTION_MAIN, Intent.CATEGORY_APP_MARKET)

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)

            } else {
                val error = resources.getString(R.string.message_error)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }

//            val appId = "com.Practicum.PlaylistMaker"
//            val shareintent = Intent(Intent.ACTION_SEND)
//            shareintent.type = "text/plain"
//            shareintent.putExtra(
//                Intent.EXTRA_TEXT,
//                getString(R.string.share_app_text, appId)
//            )
//            startActivity(Intent.createChooser(shareintent, getString(R.string.app_name)))

        }



    }
}