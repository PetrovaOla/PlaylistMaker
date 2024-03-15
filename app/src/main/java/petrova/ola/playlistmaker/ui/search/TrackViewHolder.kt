package petrova.ola.playlistmaker.ui.search

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ItemTrackBinding
import petrova.ola.playlistmaker.domain.models.Track
import petrova.ola.playlistmaker.utils.msToTime

class TrackViewHolder(
    private val binding: ItemTrackBinding,
    val trackOnClickListener: TrackOnClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {

        val context = itemView.context
        with(binding) {
            trackItemName.text = track.trackName
            trackItemArtist.text = track.artistName
            trackItemArtist.requestLayout()
            itemTime.text = msToTime(track.trackTimeMillis)

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