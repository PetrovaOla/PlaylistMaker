package petrova.ola.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaLibraryButton = findViewById<Button>(R.id.mediaLibrary_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        searchButton.setOnClickListener{
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        mediaLibraryButton.setOnClickListener{
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        val clickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val settingIntent = Intent(applicationContext,SettingActivity::class.java)
                startActivity(settingIntent)
            }
        }

        settingsButton.setOnClickListener(clickListener)
    }
}