package com.example.pokemonapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.pokemonapp.data.local.dao.PokemonDao
import com.example.pokemonapp.data.remote.PokeApiService
import com.example.pokemonapp.domain.model.Pokemon
import com.example.pokemonapp.domain.repositories.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PokemonRepositoryImpl(
    private val remoteDataSource: PokeApiService,
    private val localDataSource: PokemonDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokemonRepository {

    override fun getPokemons(): Flow<PagingData<Unit>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = PokemonRemoteMediator(remoteDataSource, localDataSource)
        ) {
            localDataSource.getPokemons()
        }.flow.map { pagingData ->
            pagingData.map { it.toPokemon() }
        }.flowOn(ioDispatcher)
    }

    override fun searchPokemons(query: String): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            )
        ) {
            localDataSource.searchPokemons("%$query%")
        }.flow.map { pagingData ->
            pagingData.map { it.toPokemon() }
        }.flowOn(ioDispatcher)
    }

    override suspend fun refreshPokemons() {
        withContext(ioDispatcher) {
            localDataSource.clearAll()
            // Запуск загрузки с первой страницы
        }
    }

    // Другие методы для фильтрации и получения деталей
}
