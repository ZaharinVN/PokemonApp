package com.example.pokemonapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokemonapp.presentation.filters.FilterScreen
import com.example.pokemonapp.presentation.main.MainScreen


@Composable
fun PokemonNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onNavigateToDetail = { id ->
                    navController.navigate("detail/$id")
                },
                onShowFilters = {
                    navController.navigate("filters")
                }
            )
        }

        composable("filters") {
            FilterScreen(
                onFilterSelected = { /* Handle filter */ },
                onSortSelected = { /* Handle sort */ },
                onApplyFilters = { /* Apply filters */ },
                onDismiss = { navController.popBackStack() }
            )
        }

        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            id?.let {
                DetailScreen(pokemonId = it, onBack = { navController.popBackStack() })
            }
        }
    }
}
