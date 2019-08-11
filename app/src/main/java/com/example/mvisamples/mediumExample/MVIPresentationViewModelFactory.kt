package com.example.mvisamples.mediumExample

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class MVIPresentationViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        LoadDataViewModel(DataRepoImpl()) as T
}
