package petrova.ola.playlistmaker.setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private val viewModel by viewModel<SettingViewModel>()

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var appBar: Toolbar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appBar = requireActivity().findViewById(R.id.toolbar)
        appBar.isVisible = true

        viewModel.isDarkTheme.observe(viewLifecycleOwner) {
//            if (binding.themeSwitcher.isChecked != it)
            binding.themeSwitcher.isChecked = it

        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.changeTheme(checked)
        }

        binding.supportButton.setOnClickListener {
            viewModel.support()
        }
        binding.shareButton.setOnClickListener {
            viewModel.shareApp()
        }
        binding.userAgreementButton.setOnClickListener {
            viewModel.userAgreement()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}