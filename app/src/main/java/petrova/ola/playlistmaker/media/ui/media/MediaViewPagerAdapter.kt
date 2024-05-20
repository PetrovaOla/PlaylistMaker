package petrova.ola.playlistmaker.media.ui.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import petrova.ola.playlistmaker.media.favorite.ui.FavoritesFragment
import petrova.ola.playlistmaker.media.playlist.ui.PlaylistFragment

class MediaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
//    private val trackId: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FavoritesFragment.newInstance()

            }

            else -> {
                PlaylistFragment.newInstance()
            }

        }
    }

}