package com.example.list.ui.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.list.PostTopAppBar
import com.example.list.R
import com.example.list.data.Item
import com.example.list.ui.presentation.AppViewModelProvider
import com.example.list.ui.presentation.navigation.NavigationDestination
import com.example.list.ui.theme.ListTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.all_post
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            PostTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_post)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            onItemDeleteClick = {item ->
                selectedItem = item
                deleteConfirmationRequired = true
            },
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }

    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteCancel = {
                coroutineScope.launch {
                    selectedItem?.let { viewModel.deleteItem(it) }
                    snackbarHostState.showSnackbar("Deleted Successfully")
                }
                deleteConfirmationRequired = false
            },
            onDeleteConfirm= {
                deleteConfirmationRequired = false
            },
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Item>, onItemDeleteClick: (Item) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (itemList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_item_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        } else {
            PostList(
                itemList = itemList,
                onItemDeleteClick =  onItemDeleteClick ,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
private fun PostList(
    itemList: List<Item>, onItemDeleteClick: (Item) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = itemList, key = { it.id }) { item ->
            PostItem(
                item = item,
                onItemDeleteClick =onItemDeleteClick,
                modifier = Modifier
                    .padding(12.dp))
        }
    }
}

@Composable
private fun PostItem(
    item: Item, onItemDeleteClick: (Item) -> Unit ,modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.itemName,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = item.category.toString(),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
            Text(
                text = "Quantity :"+item.quantity.toString(),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End){

                ItemDetailslike(
                    icon = Icons.Default.Delete,
                    ThumbNo = stringResource(R.string.delete),
                    Modifier.clickable(onClick = {onItemDeleteClick(item)})
                )
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    ListTheme {
        HomeBody(listOf(), onItemDeleteClick = {})
    }
}



@Composable
private fun ItemDetailslike(
    icon : ImageVector,ThumbNo:String, modifier: Modifier= Modifier
) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(R.string.edit_post),
            modifier = Modifier
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = ThumbNo)
    }
}


@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier,
) {
    AlertDialog(onDismissRequest = { onDeleteConfirm() },
        title = { Text(stringResource(R.string.delete_title)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.yes))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.cancel))
            }
        })
}

