package petrova.ola.playlistmaker.media.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private val viewModel by viewModel<MediaViewModel>()
    private val binding by lazy {
        ActivityMediaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MediaActivity::class.java)
    }
}