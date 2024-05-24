package petrova.ola.playlistmaker.media.playlists.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import petrova.ola.playlistmaker.databinding.ItemPlaylistBinding
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist

class PlaylistsAdapter(
    val playlists: MutableList<Playlist> = mutableListOf(),
    private val onClickListener: PlayListOnClickListener
) : RecyclerView.Adapter<PlaylistsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val binding =
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistsViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val list = playlists[position]
        holder.bind(list)
        holder.itemView.setOnClickListener { onClickListener.onClick(list) }
    }

    override fun getItemCount() = playlists.size
}