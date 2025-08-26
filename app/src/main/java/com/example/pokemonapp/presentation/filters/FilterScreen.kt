package com.example.pokemonapp.presentation.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokemonapp.domain.model.PokemonType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onFilterSelected: (PokemonType) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    onApplyFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTypes by remember { mutableStateOf(emptySet<PokemonType>()) }
    var selectedSort by remember { mutableStateOf(SortOption.NUMBER) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Sort by",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Опции сортировки
            SortOptions(
                selectedOption = selectedSort,
                onOptionSelected = { selectedSort = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Filter by Type",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Фильтры по типам
            TypeFilters(
                selectedTypes = selectedTypes,
                onTypeSelected = { type ->
                    selectedTypes = if (selectedTypes.contains(type)) {
                        selectedTypes - type
                    } else {
                        selectedTypes + type
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    selectedTypes.forEach { onFilterSelected(it) }
                    onSortSelected(selectedSort)
                    onApplyFilters()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Apply Filters")
            }
        }
    }
}

@Composable
fun SortOptions(
    selectedOption: SortOption,
    onOptionSelected: (SortOption) -> Unit
) {
    val options = listOf(
        SortOption.NUMBER,
        SortOption.NAME,
        SortOption.HP,
        SortOption.ATTACK,
        SortOption.DEFENSE
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selectedOption == option,
                onClick = { onOptionSelected(option) },
                label = { Text(option.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TypeFilters(
    selectedTypes: Set<PokemonType>,
    onTypeSelected: (PokemonType) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PokemonType.values().forEach { type ->
            AssistChip(
                onClick = { onTypeSelected(type) },
                label = { Text(type.name) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selectedTypes.contains(type)) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            )
        }
    }
}

enum class SortOption(val displayName: String) {
    NUMBER("Number"),
    NAME("Name"),
    HP("HP"),
    ATTACK("Attack"),
    DEFENSE("Defense")
}
