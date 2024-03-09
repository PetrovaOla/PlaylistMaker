package petrova.ola.playlistmaker.presentation

import android.view.View
import android.widget.ImageView

interface ImageLoader {
    fun loadImage(
        imageUrl: String,
        context: View,
        placeholder: Int,
        errorPlaceholder: Int,
        into: ImageView
    )
}