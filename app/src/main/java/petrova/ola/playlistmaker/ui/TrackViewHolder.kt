package petrova.ola.playlistmaker.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ItemTrackBinding
import petrova.ola.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(
    private val binding: ItemTrackBinding,
    val trackOnClickListener: TrackOnClickListener
) : RecyclerView.ViewHolder(binding.root) {
    private val simpleDateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun bind(track: Track) {

        val context = itemView.context
        with(binding) {
            trackItemName.text = track.trackName
            trackItemArtist.text = track.artistName
            trackItemArtist.requestLayout()
            itemTime.text = simpleDateFormat.format(track.trackTimeMillis)

            Glide // Отрисовка с Glide
                .with(context)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(2))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imageTrack)
        }
        itemView.setOnClickListener { trackOnClickListener.onClick(track) }
    }
}

fun interface TrackOnClickListener {
    fun onClick(track: Track)
}