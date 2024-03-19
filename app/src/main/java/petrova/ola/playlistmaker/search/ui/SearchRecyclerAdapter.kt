package petrova.ola.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import petrova.ola.playlistmaker.databinding.ItemTrackBinding
import petrova.ola.playlistmaker.search.domain.model.Track

class SearchRecyclerAdapter(
    val tracks: MutableList<Track> = mutableListOf(),
    private val trackOnClickListener: TrackOnClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, trackOnClickListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { trackOnClickListener.onClick(track) }
    }

    override fun getItemCount() = tracks.size
}