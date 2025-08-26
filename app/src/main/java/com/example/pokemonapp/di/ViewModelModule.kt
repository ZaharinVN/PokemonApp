package com.example.pokemonapp.di

import com.example.pokemonapp.domain.usecases.FilterPokemonsUseCase
import com.example.pokemonapp.domain.usecases.GetPokemonsUseCase
import com.example.pokemonapp.domain.usecases.RefreshPokemonsUseCase
import com.example.pokemonapp.domain.usecases.SearchPokemonsUseCase
import com.example.pokemonapp.presentation.main.MainViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideMainViewModel(
        getPokemonsUseCase: GetPokemonsUseCase,
        searchPokemonsUseCase: SearchPokemonsUseCase,
        filterPokemonsUseCase: FilterPokemonsUseCase,
        refreshPokemonsUseCase: RefreshPokemonsUseCase
    ): MainViewModel {
        return MainViewModel(
            getPokemonsUseCase,
            searchPokemonsUseCase,
            filterPokemonsUseCase,
            refreshPokemonsUseCase
        )
    }
}
