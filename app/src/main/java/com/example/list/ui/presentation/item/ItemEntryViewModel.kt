package com.example.list.ui.presentation.item

import com.example.list.data.Item
import com.example.list.data.ItemsRepository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel



class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    var selectedCategory by mutableStateOf("Food")
        private set

    val categories = listOf("Food", "Electronics", "Clothing", "Other")

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }


    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            title.isNotBlank() && (quantity.isNotBlank() && !quantity.equals("0")) && category.isNotBlank()
        }
    }
}

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val title: String = "",
    val quantity: String = "",
    val category: String = "",
)

fun ItemDetails.toItem(): Item = Item(
    id = id,
    itemName = title,
    quantity = quantity,
    category = category
)


fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)


fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    title = itemName,
    quantity = quantity,
    category = category
)