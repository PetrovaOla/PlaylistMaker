package petrova.ola.playlistmaker

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import petrova.ola.playlistmaker.data.Track
import petrova.ola.playlistmaker.databinding.ItemTrackBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: ItemTrackBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {

        val context = itemView.context
        with(binding) {
            binding.trackItemName.text = track.trackName
            binding.trackItemArtist.text = track.artistName
            binding.trackItemArtist.requestLayout()
            binding.itemTime.text =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            Glide // Отрисовка с Glide
                .with(context)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(2))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imageTrack)
        }

    }

}