package com.talhapps.climabit.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.talhapps.climabit.navigation.animations.backTransitionSpec
import com.talhapps.climabit.navigation.animations.forwardTransitionSpec
import com.talhapps.climabit.navigation.animations.predictiveBackTransitionSpec
import com.talhapps.climabit.navigation.components.BottomNavigation
import com.talhapps.climabit.navigation.components.ClimaBitNavigationDrawer
import com.talhapps.climabit.navigation.components.ClimaBitNavigationRail
import com.talhapps.climabit.navigation.components.ClimaBitTopAppBar
import com.talhapps.climabit.navigation.model.Dashboard
import com.talhapps.climabit.navigation.model.Forecast
import com.talhapps.climabit.navigation.model.NavigationType
import com.talhapps.climabit.navigation.model.Route
import com.talhapps.climabit.navigation.model.Search
import com.talhapps.climabit.navigation.model.Settings
import com.talhapps.climabit.navigation.model.WeatherDetails
import com.talhapps.climabit.navigation.util.topLevelRoutes
import com.talhapps.climabit.presentation.dashboard.DashboardScreen
import com.talhapps.climabit.presentation.details.WeatherDetailsScreen
import com.talhapps.climabit.presentation.forecast.ForecastScreen
import com.talhapps.climabit.presentation.search.SearchScreen
import com.talhapps.climabit.presentation.settings.SettingsScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val backStack: MutableList<Route> = remember {
        mutableStateListOf(Dashboard)
    }

    val coroutineScope = rememberCoroutineScope()

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo).copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<Any>(directive = directive)

    val windowSizeClass = windowAdaptiveInfo.windowSizeClass

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navigationType = when {
        windowSizeClass.isWidthAtLeastBreakpoint(840) -> NavigationType.Drawer
        windowSizeClass.isWidthAtLeastBreakpoint(600) -> NavigationType.Rail
        else -> NavigationType.BottomBar
    }

    val navigationContent: @Composable () -> Unit = {
        when (navigationType) {
            NavigationType.Drawer -> {
                ClimaBitNavigationDrawer(
                    drawerState = drawerState,
                    coroutineScope = coroutineScope,
                    topLevelRoutes = topLevelRoutes,
                    backStack = backStack
                )
            }

            NavigationType.Rail -> {
                ClimaBitNavigationRail(
                    topLevelRoutes = topLevelRoutes,
                    backStack = backStack
                )
            }

            NavigationType.BottomBar -> {
                BottomNavigation(
                    topLevelRoutes = topLevelRoutes,
                    backStack = backStack
                )
            }
        }
    }

    val mainContent: @Composable () -> Unit = {
        NavDisplay(
            modifier = Modifier.fillMaxSize(),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategy = listDetailStrategy,
            transitionSpec = { forwardTransitionSpec() },
            popTransitionSpec = { backTransitionSpec() },
            predictivePopTransitionSpec = { predictiveBackTransitionSpec() },
            entryProvider = entryProvider {
                entry<Dashboard>(
                    metadata = ListDetailSceneStrategy.listPane(
                        detailPlaceholder = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Select a location to view details",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        })
                ) {
                    DashboardScreen(
                        onLocationSelected = { lat, lon, locationName, locationCountry ->
                            backStack.add(
                                WeatherDetails(
                                    lat = lat,
                                    lon = lon,
                                    locationName = locationName,
                                    locationCountry = locationCountry
                                )
                            )
                        },
                        onSettingsClick = {
                            backStack.add(Settings)
                        }
                    )
                }

                entry<Forecast> {
                    ForecastScreen(
                        onForecastItemSelected = { lat, lon, locationName, locationCountry ->
                            backStack.add(
                                WeatherDetails(
                                    lat = lat,
                                    lon = lon,
                                    locationName = locationName,
                                    locationCountry = locationCountry
                                )
                            )
                        }
                    )
                }

                entry<Search> {
                    SearchScreen(
                        onLocationSelected = { location ->
                            backStack.add(
                                WeatherDetails(
                                    lat = location.latitude ?: 0.0,
                                    lon = location.longitude ?: 0.0,
                                    locationName = location.name,
                                    locationCountry = location.country
                                )
                            )
                        }
                    )
                }

                entry<Settings> {
                    SettingsScreen()
                }

                entry<WeatherDetails>(
                    metadata = ListDetailSceneStrategy.detailPane()
                ) { key ->
                    WeatherDetailsScreen(
                        lat = key.lat,
                        lon = key.lon,
                        locationName = key.locationName,
                        locationCountry = key.locationCountry
                    )
                }
            }
        )
    }

    when (navigationType) {
        NavigationType.Drawer -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        navigationContent()
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        ClimaBitTopAppBar(
                            drawerState = drawerState,
                            coroutineScope = coroutineScope
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues)
                    ) {
                        mainContent()
                    }
                }
            }
        }

        NavigationType.Rail -> {
            Scaffold(
                content = { paddingValues ->
                    Row(
                        modifier = Modifier
                            .padding(paddingValues)
                            .consumeWindowInsets(paddingValues)
                    ) {
                        navigationContent()
                        Box(modifier = Modifier.weight(1f)) {
                            mainContent()
                        }
                    }
                }
            )
        }

        NavigationType.BottomBar -> {
            Scaffold(
                bottomBar = {
                    navigationContent()
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                ) {
                    mainContent()
                }
            }
        }
    }
}
