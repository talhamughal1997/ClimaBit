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
import androidx.compose.material3.HorizontalDivider
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

/**
 * Base route interface for Navigation 3.
 */
@Serializable
private sealed interface Route

/**
 * Top level routes that appear in the navigation bar.
 */
@Serializable
private sealed interface TopLevelRoute : Route

/**
 * Dashboard route - main weather screen.
 */
@Serializable
private data object Dashboard : TopLevelRoute

/**
 * Forecast route - 5-day forecast screen.
 */
@Serializable
private data object Forecast : TopLevelRoute

/**
 * Search route - location search screen.
 */
@Serializable
private data object Search : TopLevelRoute

/**
 * Settings route - app settings screen.
 */
@Serializable
private data object Settings : Route

/**
 * Weather details route with location parameters.
 */
@Serializable
private data class WeatherDetails(
    val lat: Double,
    val lon: Double,
    val locationName: String? = null,
    val locationCountry: String? = null
) : Route

/**
 * List of top level routes for navigation bar.
 */
private val topLevelRoutes: List<TopLevelRoute> = listOf(Dashboard, Forecast, Search)

/**
 * Animation duration for navigation transitions.
 */
private const val NAVIGATION_ANIMATION_DURATION = 300

/**
 * Forward navigation transition - slides in from right, fades in.
 */
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


/**
 * Back navigation transition - slides in from left, fades in.
 */
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


/**
 * Predictive back navigation transition - scales and fades out.
 * Used for Android's predictive back gesture.
 */
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


/**
 * Navigation 3 implementation with adaptive layout support.
 * Uses NavDisplay with ListDetailSceneStrategy for tablet/mobile layouts.
 * Based on: https://johnoreilly.dev/posts/navigation3-cmp/
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    // Back stack for Navigation 3 - using mutableStateListOf for state management
    val backStack: MutableList<Route> = remember {
        mutableStateListOf(Dashboard)
    }

    val coroutineScope = rememberCoroutineScope()

    // Adaptive layout setup for tablet/mobile
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo).copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<Any>(directive = directive)

    // Determine window size class for adaptive navigation
    val windowSizeClass = windowAdaptiveInfo.windowSizeClass

    // Drawer state for extra large windows
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Determine navigation type based on window size using breakpoints
    // COMPACT: < 600dp (BottomBar)
    // MEDIUM: >= 600dp and < 840dp (Rail)
    // EXPANDED: >= 840dp (Drawer)
    val navigationType = when {
        windowSizeClass.isWidthAtLeastBreakpoint(840) -> NavigationType.Drawer
        windowSizeClass.isWidthAtLeastBreakpoint(600) -> NavigationType.Rail
        else -> NavigationType.BottomBar
    }

    // Navigation content
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

    // Main content
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
                // Dashboard entry with list/detail pane support
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
                    })) {
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

                // Forecast entry with list/detail pane support
                entry<Forecast>(
                    metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Select a forecast item to view details",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    })) {
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

                // Search entry
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

                // Settings entry
                entry<Settings> {
                    SettingsScreen()
                }

                // Weather Details entry (detail pane for tablet, full screen for mobile)
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

    // Render based on navigation type
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
                    Box(modifier = Modifier.padding(paddingValues)) {
                        mainContent()
                    }
                }
            }
        }

        NavigationType.Rail -> {
            Scaffold(
                content = { paddingValues ->
                    Row(modifier = Modifier.padding(paddingValues)) {
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
                Box {
                    mainContent()
                }
            }
        }
    }
}

/**
 * Enum for navigation types based on window size.
 */
private enum class NavigationType {
    BottomBar,  // Small windows (mobile)
    Rail,       // Large windows (tablet)
    Drawer      // Extra large windows (desktop)
}

/**
 * Route metadata for navigation display.
 */
private data class RouteMetadata(
    val route: TopLevelRoute, val icon: ImageVector, val contentDescription: String
)

/**
 * Route metadata mapping.
 */
private val routeMetadataMap = mapOf<TopLevelRoute, RouteMetadata>(
    Dashboard to RouteMetadata(Dashboard, Icons.Default.Home, "Dashboard"),
    Forecast to RouteMetadata(Forecast, Icons.Default.DateRange, "Forecast"),
    Search to RouteMetadata(Search, Icons.Default.Search, "Search")
)

/**
 * Bottom navigation bar for small windows (mobile).
 */
@Composable
private fun ClimaBitBottomNavigation(
    topLevelRoutes: List<TopLevelRoute>,
    backStack: MutableList<Route>
) {
    // Get current route from back stack - observe changes
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

/**
 * Navigation rail for large windows (tablet).
 */
@Composable
private fun ClimaBitNavigationRail(
    topLevelRoutes: List<TopLevelRoute>,
    backStack: MutableList<Route>
) {
    // Get current route from back stack - observe changes
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

/**
 * Navigation drawer for extra large windows (desktop).
 */
@Composable
private fun ClimaBitNavigationDrawer(
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    topLevelRoutes: List<TopLevelRoute>,
    backStack: MutableList<Route>
) {
    // Get current route from back stack - observe changes
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
        // Header
        Text(
            text = "ClimaBit",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Top level routes
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

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Settings
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
