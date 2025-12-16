package com.example.list.ui.presentation.item

import androidx.compose.ui.unit.dp
import com.example.list.PostTopAppBar
import com.example.list.R
import com.example.list.ui.presentation.AppViewModelProvider
import com.example.list.ui.presentation.navigation.NavigationDestination
import com.example.list.ui.theme.ListTheme
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

object ItemEntryDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = R.string.add_post
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val categories = viewModel.categories

    Scaffold(
        topBar = {
            PostTopAppBar(
                title = stringResource(ItemEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ItemEntryBody(
            categories = categories,
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                    Toast.makeText(context,"Storage Successfully...", Toast.LENGTH_SHORT).show()
                }
            },
            buttonName =  stringResource(R.string.submit_action),
            onCancelClick = onNavigateUp,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),

            )
    }
}

@Composable
fun ItemEntryBody(
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    buttonName: String,
    modifier: Modifier = Modifier,
    categories: List<String>,

    ) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        ItemInputForm(
            itemDetails = itemUiState.itemDetails,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onItemValueChange,
            categories = categories
        )
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly){

            OutlinedButton(
                onClick = onCancelClick,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Text(text =stringResource(R.string.cancel))
            }
            Spacer(Modifier.padding(8.dp))
            Button(
                onClick = onSaveClick,
                enabled = itemUiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = buttonName)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ItemDetails) -> Unit = {},
    enabled: Boolean = true,
    categories: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = itemDetails.title,
            onValueChange = { onValueChange(itemDetails.copy(title = it)) },
            label = { Text(stringResource(R.string.post_title)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = itemDetails.quantity,
            onValueChange = { onValueChange(itemDetails.copy(quantity = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.post_description)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = itemDetails.category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            onValueChange(itemDetails.copy(category = category))
                            expanded = false
                        }
                    )
                }
            }
        }

        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}
