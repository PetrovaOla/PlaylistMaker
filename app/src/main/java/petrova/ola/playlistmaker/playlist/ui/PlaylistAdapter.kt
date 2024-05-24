package petrova.ola.playlistmaker.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import petrova.ola.playlistmaker.databinding.ItemTrackBinding
import petrova.ola.playlistmaker.search.domain.model.Track

class PlaylistAdapter(
    val tracks: MutableList<Track> = mutableListOf(),
    private val trackOnClickListener: TrackOnClickListener,
    private val trackOnLongClickListener: TrackOnLongClickListener,
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, trackOnClickListener, trackOnLongClickListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { trackOnClickListener.onClick(track) }
        holder.itemView.setOnLongClickListener {
            trackOnLongClickListener.onLongClick(track)
            true
        }
    }

    override fun getItemCount() = tracks.size
}