package com.example.pokemonapp.domain.usecases

import androidx.paging.PagingData
import com.example.pokemonapp.domain.model.Pokemon
import com.example.pokemonapp.domain.model.PokemonType
import com.example.pokemonapp.domain.repositories.PokemonRepository
import kotlinx.coroutines.flow.Flow

class FilterPokemonsUseCase(
    private val repository: PokemonRepository
) {
    operator fun invoke(type: PokemonType): Flow<PagingData<Pokemon>> {
        return repository.filterByType(type)
    }
}
