package com.talhapps.climabit.navigation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.navigation.model.Dashboard
import com.talhapps.climabit.navigation.model.Route
import com.talhapps.climabit.navigation.model.TopLevelRoute
import com.talhapps.climabit.navigation.util.routeMetadataMap

@Composable
fun ClimaBitNavigationRail(
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

