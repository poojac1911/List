package com.example.list.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.list.data.Item
import com.example.list.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class HomeViewModel(val itemsRepository: ItemsRepository) : ViewModel() {


    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )
    suspend fun deleteItem(item: Item) {
        itemsRepository.deleteItem(item)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(val itemList: List<Item> = listOf())