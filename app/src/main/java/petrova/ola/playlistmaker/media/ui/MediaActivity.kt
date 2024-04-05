package petrova.ola.playlistmaker.media.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private val viewModel by viewModel<MediaViewModel>()
    private val binding by lazy {
        ActivityMediaBinding.inflate(layoutInflater)
    }
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbarMedia.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val trackId = intent.getStringExtra("id") ?: ""
        binding.viewPager.adapter = MediaViewPagerAdapter(
            supportFragmentManager,
            lifecycle, trackId
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorites)
                1 -> tab.text = getString(R.string.playlist)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }



    companion object {
        fun newIntent(context: Context) = Intent(context, MediaActivity::class.java)
    }
}