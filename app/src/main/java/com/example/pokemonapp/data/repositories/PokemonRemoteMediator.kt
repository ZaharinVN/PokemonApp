package com.example.pokemonapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.pokemonapp.data.local.dao.PokemonDao
import com.example.pokemonapp.data.local.entities.PokemonEntity
import com.example.pokemonapp.data.remote.PokeApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val remoteDataSource: PokeApiService,
    private val localDataSource: PokemonDao
) : RemoteMediator<Int, PokemonEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        0
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val pokemons = remoteDataSource.getPokemons(
                offset = loadKey * state.config.pageSize,
                limit = state.config.pageSize
            )

            // Сохранение в базу данных
            localDataSource.insertAll(pokemons.results.mapIndexed { index, item ->
                val detail = remoteDataSource.getPokemonDetail(item.name)
                PokemonEntity(
                    id = detail.id,
                    name = detail.name,
                    imageUrl = detail.sprites.other.officialArtwork.front_default,
                    height = detail.height,
                    weight = detail.weight,
                    types = Json.encodeToString(detail.types.map { it.type.name }),
                    stats = Json.encodeToString(detail.stats),
                    page = loadKey
                )
            })

            MediatorResult.Success(
                endOfPaginationReached = pokemons.next == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
