package com.example.pokemonapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonapp.data.local.entities.PokemonEntity

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Query("SELECT * FROM pokemons ORDER BY page, id")
    fun getPokemons(): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM pokemons WHERE name LIKE :query ORDER BY page, id")
    fun searchPokemons(query: String): PagingSource<Int, PokemonEntity>

    @Query("SELECT * FROM pokemons WHERE types LIKE :type ORDER BY page, id")
    fun filterByType(type: String): PagingSource<Int, PokemonEntity>

    @Query("SELECT COUNT(*) FROM pokemons")
    suspend fun getCount(): Int

    @Query("DELETE FROM pokemons")
    suspend fun clearAll()
}
