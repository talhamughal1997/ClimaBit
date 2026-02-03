package com.talhapps.climabit.navigation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.talhapps.climabit.navigation.model.Dashboard
import com.talhapps.climabit.navigation.model.Forecast
import com.talhapps.climabit.navigation.model.Search
import com.talhapps.climabit.navigation.model.TopLevelRoute

data class RouteMetadata(
    val route: TopLevelRoute,
    val icon: ImageVector,
    val contentDescription: String
)

val routeMetadataMap: Map<TopLevelRoute, RouteMetadata> = mapOf(
    Dashboard to RouteMetadata(Dashboard, Icons.Default.Home, "Dashboard"),
    Forecast to RouteMetadata(Forecast, Icons.Default.DateRange, "Forecast"),
    Search to RouteMetadata(Search, Icons.Default.Search, "Search")
)

val topLevelRoutes: List<TopLevelRoute> = listOf(Dashboard, Forecast, Search)

