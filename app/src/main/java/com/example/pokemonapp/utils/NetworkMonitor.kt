package com.example.pokemonapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pokemonapp.data.repositories.PokemonRemoteMediator
import com.example.pokemonapp.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val isOnline: Flow<Boolean> = flow {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
            emit(hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
        } ?: emit(false)
    }.distinctUntilChanged()
}

// В репозитории
override fun getPokemons(): Flow<PagingData<Pokemon>> {
    return networkMonitor.isOnline.flatMapLatest { isOnline ->
        if (isOnline) {
            // Загрузка из сети с сохранением в БД
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator = PokemonRemoteMediator(remoteDataSource, localDataSource)
            ) {
                localDataSource.getPokemons()
            }.flow
        } else {
            // Только локальные данные
            Pager(PagingConfig(pageSize = 20)) {
                localDataSource.getPokemons()
            }.flow
        }
    }.map { pagingData ->
        pagingData.map { it.toPokemon() }
    }.flowOn(ioDispatcher)
}
