package com.example.pokemonapp.data.remote.models

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemResponse>
)

data class PokemonListItemResponse(
    val name: String,
    val url: String
)

// Детальная информация о покемоне
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeResponse>,
    val stats: List<PokemonStatResponse>,
    val sprites: PokemonSpritesResponse
)

data class PokemonTypeResponse(
    val type: NamedApiResource
)

data class PokemonStatResponse(
    val stat: NamedApiResource,
    val base_stat: Int
)

data class PokemonSpritesResponse(
    val front_default: String,
    val other: OtherSprites
)

data class OtherSprites(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    val front_default: String
)

data class NamedApiResource(
    val name: String,
    val url: String
)
