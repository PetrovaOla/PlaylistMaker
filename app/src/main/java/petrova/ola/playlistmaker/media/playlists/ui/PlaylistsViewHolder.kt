package petrova.ola.playlistmaker.media.playlists.ui


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ItemPlaylistBinding
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist


class PlaylistsViewHolder(
    private val binding: ItemPlaylistBinding,
    val onClickListener: PlayListOnClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(list: Playlist) {

        with(binding) {
            namePl.text = list.name
            count.text = getCount(list.trackIds.size)
            Glide // Отрисовка с Glide
                .with(imagePl)
                .load(list.img)
                .transform(RoundedCorners(8))
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imagePl)
        }
        itemView.setOnClickListener { onClickListener.onClick(list) }
    }

    private fun getCount(count: Int): String {
        val pluralsString = binding.root.resources.getQuantityString(
            R.plurals.track_plurals,
            count,
            count
        )
        return pluralsString
    }
}

fun interface PlayListOnClickListener {
    fun onClick(list: Playlist)
}