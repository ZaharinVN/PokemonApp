package com.example.pokemonapp.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val stats: Map<String, Int>
)
