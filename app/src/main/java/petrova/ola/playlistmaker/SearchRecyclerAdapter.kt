package petrova.ola.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import petrova.ola.playlistmaker.data.Track
import petrova.ola.playlistmaker.databinding.ItemTrackBinding

class SearchRecyclerAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
       val track =  tracks[position]
        val context = holder.itemView.context
        with(holder.binding) {
            trackItemName.text = track.trackName
            trackItemArtist.text = track.artistName
            itemTime.text = track.trackTime
            Glide // Отрисовка с Glide
                .with(context)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(2))
                .error(R.drawable.ic_person)
                .placeholder(R.drawable.ic_person)
                .into(imageTrack)
        }
        holder.itemView.setOnClickListener { /* здесь можем что-то сделать */ }
    }

    override fun getItemCount() = tracks.size
}