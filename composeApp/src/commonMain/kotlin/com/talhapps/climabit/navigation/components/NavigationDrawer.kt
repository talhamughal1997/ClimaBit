package com.talhapps.climabit.navigation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.talhapps.climabit.navigation.model.Dashboard
import com.talhapps.climabit.navigation.model.Route
import com.talhapps.climabit.navigation.model.Settings
import com.talhapps.climabit.navigation.model.TopLevelRoute
import com.talhapps.climabit.navigation.util.routeMetadataMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ClimaBitNavigationDrawer(
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
        modifier = Modifier.fillMaxSize()
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

