package petrova.ola.playlistmaker.media.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import petrova.ola.playlistmaker.databinding.ItemPlaylistBinding
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist

class PlaylistAdapter(
    val playlists: MutableList<Playlist> = mutableListOf(),
    private val onClickListener: PlayListOnClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding =
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val list = playlists[position]
        holder.bind(list)
        holder.itemView.setOnClickListener { onClickListener.onClick(list) }
    }

    override fun getItemCount() = playlists.size
}