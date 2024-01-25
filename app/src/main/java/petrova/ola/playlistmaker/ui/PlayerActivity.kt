package petrova.ola.playlistmaker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ActivityPlayerBinding
import petrova.ola.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private lateinit var track: Track
    private val simpleDateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbarPlayer.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        track = Gson().fromJson(
            intent.extras?.getString(SearchActivity.EXTRAS_KEY) ?: "",
            Track::class.java
        )
        val context =
            with(binding) {
                Glide // Отрисовка с Glide
                    .with(this.main)
                    .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .centerCrop()
                    .transform(RoundedCorners(8))
                    .error(R.drawable.album)
                    .placeholder(R.drawable.album)
                    .into(poster)

                name.text = track.trackName
                artist.text = track.artistName
                duration.text = simpleDateFormat.format(track.trackTimeMillis)
                album.text = track.collectionName
                album.isVisible = album.text.isNotEmpty()
                albumTv.isVisible = album.isVisible
                if (track.releaseDate.length > 3) {
                    year.text = track.releaseDate.substring(0, 4)
                }
                genre.text = track.primaryGenreName
                country.text = track.country
            }


    }


}
