package petrova.ola.playlistmaker.presentation

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class GlideImageLoader : ImageLoader {
    override fun loadImage(
        imageUrl: String,
        context: View,
        placeholder: Int,
        errorPlaceholder: Int,
        into: ImageView
    ) {
        Glide
            .with(context)
            .load(imageUrl)
            .centerCrop()
            .transform(RoundedCorners(8))
            .error(errorPlaceholder)
            .placeholder(placeholder)
            .into(into)
    }
}