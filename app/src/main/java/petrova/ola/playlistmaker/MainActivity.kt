package petrova.ola.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import petrova.ola.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        binding.mediaLibraryButton.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        val clickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val settingIntent = Intent(applicationContext, SettingActivity::class.java)
                startActivity(settingIntent)
            }
        }

       binding.settingsButton.setOnClickListener(clickListener)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}