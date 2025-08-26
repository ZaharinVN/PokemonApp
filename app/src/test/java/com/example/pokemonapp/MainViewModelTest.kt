package com.example.pokemonapp

import com.example.pokemonapp.domain.repositories.PokemonRepository
import com.example.pokemonapp.domain.usecases.FilterPokemonsUseCase
import com.example.pokemonapp.domain.usecases.GetPokemonsUseCase
import com.example.pokemonapp.domain.usecases.RefreshPokemonsUseCase
import com.example.pokemonapp.domain.usecases.SearchPokemonsUseCase
import com.example.pokemonapp.presentation.main.MainViewModel
import okhttp3.internal.tls.OkHostnameVerifier.verify
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private val mockRepository = mockk<PokemonRepository>()

    @Before
    fun setup() {
        viewModel = MainViewModel(
            GetPokemonsUseCase(mockRepository),
            SearchPokemonsUseCase(mockRepository),
            FilterPokemonsUseCase(mockRepository),
            RefreshPokemonsUseCase(mockRepository)
        )
    }

    @Test
    fun `search event updates query`(): Unit = runTest {
        viewModel.onEvent(MainViewModel.MainEvent.Search("pikachu"))

        // Verify that search is triggered
        verify { mockRepository.searchPokemons("pikachu") }
    }
}
