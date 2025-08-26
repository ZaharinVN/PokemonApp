package com.example.pokemonapp.domain.usecases

import androidx.paging.PagingData
import com.example.pokemonapp.domain.model.Pokemon
import com.example.pokemonapp.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow

class SearchPokemonsUseCase(
    private val repository: PokemonRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Pokemon>> {
        return repository.searchPokemons(query)
    }
}
