package petrova.ola.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import petrova.ola.playlistmaker.databinding.ItemBottomsheetBinding
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist

class BottomSheetAdapter (
    val playlists: MutableList<Playlist> = mutableListOf(),
    private val onClickListener: BottomSheetOnClickListener
) : RecyclerView.Adapter<BottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        val binding =
            ItemBottomsheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BottomSheetViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val list = playlists[position]
        holder.bind(list)
        holder.itemView.setOnClickListener { onClickListener.onClick(list) }
    }

    override fun getItemCount() = playlists.size
}