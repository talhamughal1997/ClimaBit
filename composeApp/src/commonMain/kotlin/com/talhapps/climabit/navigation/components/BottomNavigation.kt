package com.talhapps.climabit.navigation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.talhapps.climabit.navigation.model.Dashboard
import com.talhapps.climabit.navigation.model.Route
import com.talhapps.climabit.navigation.model.TopLevelRoute
import com.talhapps.climabit.navigation.util.routeMetadataMap

@Composable
fun BottomNavigation(
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

