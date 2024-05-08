package petrova.ola.playlistmaker.media.playlist.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ItemPlaylistBinding
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist


class PlaylistViewHolder(private val binding: ItemPlaylistBinding,
                         val onClickListener: PlayListOnClickListener
): RecyclerView.ViewHolder(binding.root)  {
    fun bind(list: Playlist) {

        val context = itemView.context
        with(binding) {
            namePl.text = list.name
            count.text = list.count.toString()
            Glide // Отрисовка с Glide
                .with(context)
                .load(list.img)
                .centerCrop()
                .transform(RoundedCorners(8))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imagePl)
        }
        itemView.setOnClickListener { onClickListener.onClick(list) }
    }
}

fun interface PlayListOnClickListener {
    fun onClick(list: Playlist)
}