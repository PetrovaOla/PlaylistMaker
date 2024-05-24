package petrova.ola.playlistmaker.player.ui


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ItemBottomsheetBinding
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist


class BottomSheetViewHolder(
    private val binding: ItemBottomsheetBinding,
    val onClickListener: BottomSheetOnClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(list: Playlist) {

        with(binding) {
            nameBs.text = list.name
            countBs.text = getCount(list.trackIds.size)
            Glide // Отрисовка с Glide
                .with(imageBs)
                .load(list.img)
                .transform(RoundedCorners(8))
                .centerCrop()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imageBs)
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

fun interface BottomSheetOnClickListener {
    fun onClick(list: Playlist)
}