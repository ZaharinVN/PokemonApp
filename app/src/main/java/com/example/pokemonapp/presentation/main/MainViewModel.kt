package com.example.pokemonapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemonapp.domain.model.Pokemon
import com.example.pokemonapp.domain.model.PokemonType
import com.example.pokemonapp.domain.usecases.FilterPokemonsUseCase
import com.example.pokemonapp.domain.usecases.GetPokemonsUseCase
import com.example.pokemonapp.domain.usecases.RefreshPokemonsUseCase
import com.example.pokemonapp.domain.usecases.SearchPokemonsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPokemonsUseCase: GetPokemonsUseCase,
    private val searchPokemonsUseCase: SearchPokemonsUseCase,
    private val filterPokemonsUseCase: FilterPokemonsUseCase,
    private val refreshPokemonsUseCase: RefreshPokemonsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val currentQuery = MutableStateFlow<String?>(null)
    private val currentFilter = MutableStateFlow<PokemonType?>(null)

    val pokemons: Flow<PagingData<Pokemon>> = currentQuery.combine(currentFilter) { query, filter ->
        when {
            !query.isNullOrEmpty() -> searchPokemonsUseCase(query)
            filter != null -> filterPokemonsUseCase(filter)
            else -> getPokemonsUseCase()
        }
    }.flatMapLatest { it }.cachedIn(viewModelScope)

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.Search -> {
                currentQuery.value = event.query
                currentFilter.value = null
            }
            is MainEvent.Filter -> {
                currentFilter.value = event.type
                currentQuery.value = null
            }
            is MainEvent.Refresh -> {
                viewModelScope.launch {
                    _uiState.value = UiState.Loading
                    try {
                        refreshPokemonsUseCase()
                        _uiState.value = UiState.Success
                    } catch (e: Exception) {
                        _uiState.value = UiState.Error(e.message ?: "Unknown error")
                    }
                }
            }
            MainEvent.ClearFilters -> {
                currentQuery.value = null
                currentFilter.value = null
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }

    sealed class MainEvent {
        data class Search(val query: String) : MainEvent()
        data class Filter(val type: PokemonType) : MainEvent()
        object Refresh : MainEvent()
        object ClearFilters : MainEvent()
    }
}
