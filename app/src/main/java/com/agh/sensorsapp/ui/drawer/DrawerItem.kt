package com.agh.sensorsapp.ui.drawer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DrawerItem(
    item: NavigationItem,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(text = item.title)
        },
        selected = isSelected,
        onClick = {
            onItemClick()
        },
        icon = {
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title
            )
        },
        badge = {
            item.badgeCount?.let {
                Text(text = item.badgeCount.toString())
            }
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
