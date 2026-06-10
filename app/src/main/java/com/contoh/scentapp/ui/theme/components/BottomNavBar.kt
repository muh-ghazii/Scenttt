package com.contoh.scentapp.ui.theme.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.ui.theme.ScentDarkSurface
import com.contoh.scentapp.ui.theme.ScentDivider
import com.contoh.scentapp.ui.theme.ScentNavInactive
import com.contoh.scentapp.ui.theme.ScentWhite

private data class NavItem(
    val route:   String,
    val label:   String,
    val iconOn: ImageVector,
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
            .background(ScentDarkSurface)
    ) {
        HorizontalDivider(
            thickness = 0.5.dp,
            color     = ScentDivider
        )
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            navItems.forEach { item ->
                NavBarItem(
                    item        = item,
                    isSelected  = currentRoute == item.route,
                    onClick     = { onNavigate(item.route) },
                    modifier    = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconTint by animateColorAsState(
        targetValue   = if (isSelected) ScentWhite else ScentNavInactive,
        animationSpec = tween(durationMillis = 200),
        label         = "navIconColor_${item.route}"
    )
    val labelTint by animateColorAsState(
        targetValue   = if (isSelected) ScentWhite else ScentNavInactive,
        animationSpec = tween(durationMillis = 200),
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