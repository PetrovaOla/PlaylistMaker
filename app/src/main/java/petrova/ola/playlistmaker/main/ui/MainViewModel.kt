package petrova.ola.playlistmaker.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainViewModel : ViewModel() {

    private val _navigate = MutableLiveData(MainNavigate.NONE)
    val navigate: LiveData<MainNavigate> = _navigate

    fun openSearch() {
        _navigate.value = MainNavigate.SEARCH
    }

    fun openMediateka() {
        _navigate.value = MainNavigate.MEDIA
    }

    fun openSettings() {
        _navigate.value = MainNavigate.SETTING
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer { MainViewModel() }
        }
    }

}