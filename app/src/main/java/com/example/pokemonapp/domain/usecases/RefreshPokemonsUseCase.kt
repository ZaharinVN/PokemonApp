package com.example.pokemonapp.domain.usecases

import com.example.pokemonapp.domain.repositories.PokemonRepository

class RefreshPokemonsUseCase(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke() {
        repository.refreshPokemons()
    }
}