package petrova.ola.playlistmaker

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import petrova.ola.playlistmaker.data.Track
import petrova.ola.playlistmaker.databinding.ItemTrackBinding

class TrackViewHolder(val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Track) {
        binding.trackItemName.text = model.trackName
        binding.trackItemArtist.text = model.artistName
        binding.itemTime.text = model.trackTime
    }



}