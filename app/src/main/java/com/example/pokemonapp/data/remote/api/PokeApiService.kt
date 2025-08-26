package com.example.pokemonapp.data.remote.api

import com.example.pokemonapp.data.remote.models.PokemonDetailResponse
import com.example.pokemonapp.data.remote.models.PokemonListResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): PokemonDetailResponse
}
// Retrofit клиент
object RetrofitClient {
    private const val BASE_URL = "https://pokeapi.co/api/v2/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            // Проверка на коды ошибок
            if (!response.isSuccessful) {
                throw IOException("HTTP error: ${response.code}")
            }

            response
        }
        .build()

    val instance: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }
}
