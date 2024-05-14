package petrova.ola.playlistmaker.media.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment : Fragment() {

    private val viewModel by viewModel<MediaViewModel>()

    private lateinit var tabMediator: TabLayoutMediator

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
        )
        bottomNavView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavView.isVisible = true

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorites)
                1 -> tab.text = getString(R.string.playlist)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _binding = null
    }



    companion object {
        private const val ARGS_TRACK_ID = "track_id"
        private const val ARGS_EMPTY = ""
    }
}