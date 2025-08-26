package com.example.pokemonapp.domain.repositories

import androidx.paging.PagingData
import com.example.pokemonapp.domain.model.Pokemon
import com.example.pokemonapp.domain.model.PokemonType
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemons(): Flow<PagingData<Pokemon>>
    fun searchPokemons(query: String): Flow<PagingData<Pokemon>>
    fun filterByType(type: PokemonType): Flow<PagingData<Pokemon>>
    suspend fun refreshPokemons()
    suspend fun getPokemonDetail(id: Int): Pokemon?
}
