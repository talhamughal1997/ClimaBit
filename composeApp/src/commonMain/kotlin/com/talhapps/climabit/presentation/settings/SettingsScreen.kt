package com.talhapps.climabit.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.core.theme.ThemeManager
import com.talhapps.climabit.core.ui.mvi.useMvi
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    themeManager: ThemeManager = koinInject()
) {
    val state by useMvi(
        viewModel = viewModel,
        onEffect = { effect ->
            when (effect) {
                is SettingsEffect.ShowMessage -> {

                }
            }
        }
    )

    // Observe theme changes from ThemeManager to keep UI in sync
    val isDarkTheme by themeManager.observeTheme()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Units Section
            item {
                SettingsSectionCard(
                    title = "Units",
                    {
                        // Temperature Setting
                        SettingRow(
                            title = "Temperature",
                            icon = Icons.Default.Thermostat,
                            content = {
                                SingleChoiceSegmentedButtonRow(
                                    modifier = Modifier.width(140.dp)
                                ) {
                                    SegmentedButton(
                                        selected = state.temperatureUnit == "Celsius",
                                        onClick = {
                                            if (state.temperatureUnit != "Celsius") {
                                                viewModel.handleIntent(SettingsIntent.ToggleTemperatureUnit)
                                            }
                                        },
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = 0,
                                            count = 2
                                        )
                                    ) {
                                        Text("°C", style = MaterialTheme.typography.labelMedium)
                                    }
                                    SegmentedButton(
                                        selected = state.temperatureUnit == "Fahrenheit",
                                        onClick = {
                                            if (state.temperatureUnit != "Fahrenheit") {
                                                viewModel.handleIntent(SettingsIntent.ToggleTemperatureUnit)
                                            }
                                        },
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = 1,
                                            count = 2
                                        )
                                    ) {
                                        Text("°F", style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )

                        // Wind Speed Setting
                        SettingRow(
                            title = "Wind Speed",
                            icon = Icons.Default.Speed,
                            content = {
                                SingleChoiceSegmentedButtonRow(
                                    modifier = Modifier.width(140.dp)
                                ) {
                                    SegmentedButton(
                                        selected = state.windSpeedUnit == "m/s",
                                        onClick = {
                                            if (state.windSpeedUnit != "m/s") {
                                                viewModel.handleIntent(SettingsIntent.ToggleWindSpeedUnit)
                                            }
                                        },
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = 0,
                                            count = 2
                                        )
                                    ) {
                                        Text("m/s", style = MaterialTheme.typography.labelMedium)
                                    }
                                    SegmentedButton(
                                        selected = state.windSpeedUnit == "km/h",
                                        onClick = {
                                            if (state.windSpeedUnit != "km/h") {
                                                viewModel.handleIntent(SettingsIntent.ToggleWindSpeedUnit)
                                            }
                                        },
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = 1,
                                            count = 2
                                        )
                                    ) {
                                        Text("km/h", style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }
                        )
                    }
                )
            }

            // Appearance Section
            item {
                SettingsSectionCard(
                    title = "Appearance",
                    {
                        SettingRow(
                            title = "Dark Theme",
                            icon = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            content = {
                                Switch(
                                    checked = isDarkTheme,
                                    onCheckedChange = {
                                        viewModel.handleIntent(SettingsIntent.ToggleTheme)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Section Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            content()
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        content()
    }
}
