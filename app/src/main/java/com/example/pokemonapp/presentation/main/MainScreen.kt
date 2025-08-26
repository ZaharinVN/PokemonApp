package com.example.pokemonapp.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.room.util.TableInfo
import com.example.pokemonapp.R
import com.example.pokemonapp.domain.model.Pokemon
import java.util.Locale


@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit,
    onShowFilters: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pokemons = viewModel.pokemons.collectAsLazyPagingItems()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAzppBar(
                title = {
                    Image(
                        painter = painterResource(R.drawable.pokemon_logo),
                        contentDescription = "Pokemon Logo",
                        modifier = Modifier.height(40.dp)
                    )
                },
                actions = {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { viewModel.onEvent(MainViewModel.MainEvent.Search(it)) },
                        active = false,
                        onActiveChange = {}
                    )

                    IconButton(onClick = onShowFilters) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is MainViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.gridlayout.widget.GridLayout.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MainViewModel.UiState.Error -> {
                val error = (uiState as MainViewModel.UiState.Error).message
                ErrorState(
                    error = error,
                    onRetry = { viewModel.onEvent(MainViewModel.MainEvent.Refresh) })
            }

            else -> {
                PokemonGrid(
                    pokemons = pokemons,
                    modifier = Modifier.padding(padding),
                    onItemClick = onNavigateToDetail
                )
            }
        }

        // Pull-to-Refresh
        Box(modifier = Modifier.fillMaxSize()) {
            val pullRefreshState = rememberPullRefreshState(
                refreshing = uiState is MainViewModel.UiState.Loading,
                onRefresh = { viewModel.onEvent(MainViewModel.MainEvent.Refresh) }
            )

            SwipeRefresh(
                state = pullRefreshState,
                onRefresh = { viewModel.onEvent(MainViewModel.MainEvent.Refresh) }
            ) {
                // Content
            }
        }
    }
}

@Composable
fun PokemonGrid(
    pokemons: LazyPagingItems<Pokemon>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(pokemons.itemCount) { index ->
            pokemons[index]?.let { pokemon ->
                PokemonItem(pokemon = pokemon, onItemClick = onItemClick)
            }
        }

        // Обработка состояний загрузки и ошибок
        when (pokemons.loadState.append) {
            is LoadState.Loading -> {
                item { LoadingItem() }
            }

            is LoadState.Error -> {
                item { ErrorItem() }
            }

            else -> {}
        }
    }
}

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clickable { onItemClick(pokemon.id) },
        elevation = 4.dp
    ) {
        TableInfo.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(96.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = pokemon.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                },
                style = typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
