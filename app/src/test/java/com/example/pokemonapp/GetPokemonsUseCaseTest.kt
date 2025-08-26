package com.example.pokemonapp

import androidx.paging.PagingData
import com.example.pokemonapp.domain.model.Pokemon
import com.example.pokemonapp.domain.usecases.GetPokemonsUseCase
import kotlinx.coroutines.flow.first
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
class GetPokemonsUseCaseTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var useCase: GetPokemonsUseCase

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `get pokemons returns flow of paging data`(): Unit = runTest {
        val result = useCase().first()
        assertTrue(result is PagingData<Pokemon>)
    }
}
