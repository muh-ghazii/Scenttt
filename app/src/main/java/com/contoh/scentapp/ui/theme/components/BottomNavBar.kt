package com.contoh.scentapp.ui.theme.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.ui.theme.ScentDivider
import com.contoh.scentapp.ui.theme.ScentNavInactive

private data class NavItem(
    val route:   String,
    val label:   String,
    val iconOn:  ImageVector,
    val iconOff: ImageVector
)

private val navItems = listOf(
    NavItem("home",     "BERANDA",   Icons.Filled.Home,        Icons.Outlined.Home),
    NavItem("favorite", "FAVORIT",   Icons.Filled.Favorite,    Icons.Outlined.FavoriteBorder),
    NavItem("cart",     "KERANJANG", Icons.Filled.ShoppingBag, Icons.Outlined.ShoppingBag),
    NavItem("profile",  "PROFIL",    Icons.Filled.Person,      Icons.Outlined.Person)
)

@Composable
fun ScentBottomNavBar(
    currentRoute: String,
    onNavigate:   (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalDivider(thickness = 0.5.dp, color = ScentDivider)
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            navItems.forEach { item ->
                NavBarItem(
                    item       = item,
                    isSelected = currentRoute == item.route,
                    onClick    = { onNavigate(item.route) },
                    modifier   = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    item:      NavItem,
    isSelected: Boolean,
    onClick:   () -> Unit,
    modifier:  Modifier = Modifier
) {
    val iconTint by animateColorAsState(
        targetValue   = if (isSelected) MaterialTheme.colorScheme.onBackground else ScentNavInactive,
        animationSpec = tween(200),
        label         = "navIconColor_${item.route}"
    )
    val labelTint by animateColorAsState(
        targetValue   = if (isSelected) MaterialTheme.colorScheme.onBackground else ScentNavInactive,
        animationSpec = tween(200),
        label         = "navLabelColor_${item.route}"
    )

    Column(
        modifier            = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector        = if (isSelected) item.iconOn else item.iconOff,
            contentDescription = item.label,
            tint               = iconTint,
            modifier           = Modifier.size(22.dp)
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text  = item.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize      = 9.sp,
                letterSpacing = 0.8.sp,
                fontWeight    = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color         = labelTint
            )
        )
    }
}