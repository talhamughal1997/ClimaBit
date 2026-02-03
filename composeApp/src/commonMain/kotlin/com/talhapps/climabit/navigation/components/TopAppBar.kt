package com.talhapps.climabit.navigation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimaBitTopAppBar(
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
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
        },
        modifier = modifier
    )
}

