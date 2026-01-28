package com.talhapps.climabit.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.talhapps.climabit.presentation.dashboard.DashboardScreen
import com.talhapps.climabit.presentation.details.WeatherDetailsScreen
import com.talhapps.climabit.presentation.forecast.ForecastScreen
import com.talhapps.climabit.presentation.search.SearchScreen
import com.talhapps.climabit.presentation.settings.SettingsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
private sealed interface Route

@Serializable
private sealed interface TopLevelRoute : Route

@Serializable
private data object Dashboard : TopLevelRoute

@Serializable
private data object Forecast : TopLevelRoute

@Serializable
private data object Search : TopLevelRoute

@Serializable
private data object Settings : Route

@Serializable
private data class WeatherDetails(
    val lat: Double,
    val lon: Double,
    val locationName: String? = null,
    val locationCountry: String? = null
) : Route

private val topLevelRoutes: List<TopLevelRoute> = listOf(Dashboard, Forecast, Search)

private const val NAVIGATION_ANIMATION_DURATION = 300

private fun forwardTransitionSpec() = slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) togetherWith slideOutHorizontally(
    targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
)


private fun backTransitionSpec() = slideInHorizontally(
    initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) togetherWith slideOutHorizontally(
    targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
)


private fun predictiveBackTransitionSpec() = scaleIn(
    initialScale = 0.95f, animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) togetherWith scaleOut(
    targetScale = 0.95f, animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = NAVIGATION_ANIMATION_DURATION, easing = FastOutSlowInEasing
    )
)


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
                    topLevelRoutes = topLevelRoutes, backStack = backStack
                )
            }

            NavigationType.BottomBar -> {
                ClimaBitBottomNavigation(
                    topLevelRoutes = topLevelRoutes, backStack = backStack
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
                    DashboardScreen(onLocationSelected = { lat, lon, locationName, locationCountry ->
                        backStack.add(
                            WeatherDetails(
                                lat = lat,
                                lon = lon,
                                locationName = locationName,
                                locationCountry = locationCountry
                            )
                        )
                    }, onSettingsClick = {
                        backStack.add(Settings)
                    })
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
                        })
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
                        })
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
            })
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
                        TopAppBar(
                            title = { Text("ClimaBit") },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            drawerState.open()
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Menu,
                                        contentDescription = "Menu"
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
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
                        modifier = Modifier.padding(paddingValues)
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
                },
            ) { paddingValues ->
                Box(
                    modifier = Modifier.padding(paddingValues)
                        .consumeWindowInsets(paddingValues)
                ) {
                    mainContent()
                }
            }
        }
    }
}

private enum class NavigationType {
    BottomBar,
    Rail,
    Drawer      
}

private data class RouteMetadata(
    val route: TopLevelRoute, val icon: ImageVector, val contentDescription: String
)

private val routeMetadataMap = mapOf<TopLevelRoute, RouteMetadata>(
    Dashboard to RouteMetadata(Dashboard, Icons.Default.Home, "Dashboard"),
    Forecast to RouteMetadata(Forecast, Icons.Default.DateRange, "Forecast"),
    Search to RouteMetadata(Search, Icons.Default.Search, "Search")
)

@Composable
private fun ClimaBitBottomNavigation(
    topLevelRoutes: List<TopLevelRoute>,
    backStack: MutableList<Route>
) {

    val currentRoute = remember {
        derivedStateOf {
            backStack.lastOrNull() as? TopLevelRoute ?: Dashboard
        }
    }.value

    NavigationBar {
        topLevelRoutes.forEach { route ->
            val metadata = routeMetadataMap[route] ?: return@forEach
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = metadata.icon,
                        contentDescription = metadata.contentDescription
                    )
                },
                selected = route == currentRoute,
                onClick = {
                    backStack.clear()
                    backStack.add(route)
                }
            )
        }
    }
}

@Composable
private fun ClimaBitNavigationRail(
    topLevelRoutes: List<TopLevelRoute>,
    backStack: MutableList<Route>
) {

    val currentRoute = remember {
        derivedStateOf {
            backStack.lastOrNull() as? TopLevelRoute ?: Dashboard
        }
    }.value

    NavigationRail {
        topLevelRoutes.forEach { route ->
            val metadata = routeMetadataMap[route] ?: return@forEach
            NavigationRailItem(
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = metadata.icon,
                        contentDescription = metadata.contentDescription
                    )
                },
                selected = route == currentRoute,
                onClick = {
                    backStack.clear()
                    backStack.add(route)
                }
            )
        }
    }
}

@Composable
private fun ClimaBitNavigationDrawer(
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    topLevelRoutes: List<TopLevelRoute>,
    backStack: MutableList<Route>
) {

    val currentRoute = remember {
        derivedStateOf {
            backStack.lastOrNull() as? TopLevelRoute ?: Dashboard
        }
    }.value
    val isSettingsSelected = remember {
        derivedStateOf {
            backStack.lastOrNull() is Settings
        }
    }.value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Text(
            text = "ClimaBit",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )



        topLevelRoutes.forEach { route ->
            val metadata = routeMetadataMap[route] ?: return@forEach
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = metadata.icon,
                        contentDescription = metadata.contentDescription
                    )
                },
                label = { Text(metadata.contentDescription) },
                selected = route == currentRoute && !isSettingsSelected,
                onClick = {
                    backStack.clear()
                    backStack.add(route)
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }



        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            },
            label = { Text("Settings") },
            selected = isSettingsSelected,
            onClick = {
                backStack.clear()
                backStack.add(Settings)
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
