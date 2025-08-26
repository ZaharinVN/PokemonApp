package com.example.pokemonapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "pokemons")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "types") val types: String, // JSON string для списка типов
    @ColumnInfo(name = "stats") val stats: String, // JSON string для статистики
    @ColumnInfo(name = "page") val page: Int // Для пагинации
)

// TypeConverters для сложных данных
class Converters {
    @TypeConverter
    fun fromTypesList(types: List<String>): String = types.joinToString(",")

    @TypeConverter
    fun toTypesList(data: String): List<String> = data.split(",")
}
