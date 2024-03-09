package petrova.ola.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import petrova.ola.playlistmaker.databinding.ItemTrackBinding
import petrova.ola.playlistmaker.domain.models.Track

class SearchRecyclerAdapter(
    val type: SearchListType,
    private val tracks: List<Track>,
    private val trackOnClickListener: TrackOnClickListener
) : RecyclerView.Adapter<TrackViewHolder>() {
    enum class SearchListType {
        HISTORY, SEARCH
    }

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