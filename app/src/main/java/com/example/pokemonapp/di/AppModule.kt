package com.example.pokemonapp.di

import android.content.Context
import com.example.pokemonapp.data.local.PokemonDatabase
import com.example.pokemonapp.data.local.dao.PokemonDao
import com.example.pokemonapp.data.remote.PokeApiService
import com.example.pokemonapp.data.remote.api.RetrofitClient
import com.example.pokemonapp.data.repositories.PokemonRepositoryImpl
import com.example.pokemonapp.domain.repositories.PokemonRepository
import com.example.pokemonapp.domain.usecases.GetPokemonsUseCase

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return PokemonDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    @Singleton
    fun providePokeApiService(): PokeApiService {
        return RetrofitClient.instance
    }

    @Provides
    @Singleton
    fun providePokemonRepository(
        apiService: PokeApiService,
        pokemonDao: PokemonDao
    ): PokemonRepository {
        return PokemonRepositoryImpl(apiService, pokemonDao)
    }

    @Provides
    fun provideGetPokemonsUseCase(repository: PokemonRepository): GetPokemonsUseCase {
        return GetPokemonsUseCase(repository)
    }

    // Другие UseCases
}
