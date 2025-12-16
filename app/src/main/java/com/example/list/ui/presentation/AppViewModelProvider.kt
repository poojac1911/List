package com.example.list.ui.presentation

import com.example.list.PostApplication
import com.example.list.ui.presentation.home.HomeViewModel
import com.example.list.ui.presentation.item.ItemEntryViewModel
import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            ItemEntryViewModel(inventoryApplication().container.itemsRepository)
        }

        initializer {
            HomeViewModel(inventoryApplication().container.itemsRepository)
        }
    }
}

fun CreationExtras.inventoryApplication(): PostApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PostApplication)