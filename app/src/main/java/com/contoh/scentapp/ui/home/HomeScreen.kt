package com.contoh.scentapp.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.contoh.scentapp.data.model.HeroBanner
import com.contoh.scentapp.data.model.HomeUiState
import com.contoh.scentapp.data.model.Product
import com.contoh.scentapp.ui.theme.ScentBlack
import com.contoh.scentapp.ui.theme.ScentDivider
import com.contoh.scentapp.ui.theme.ScentGold
import com.contoh.scentapp.ui.theme.ScentSearchBg
import com.contoh.scentapp.ui.theme.ScentTextLabel
import com.contoh.scentapp.ui.theme.ScentTextMuted
import com.contoh.scentapp.ui.theme.ScentTextPrimary
import com.contoh.scentapp.ui.theme.ScentWhite

@Composable
fun HomeScreen(
    onProductClick : (Int) -> Unit = {},
    onSearchClick  : () -> Unit    = {},
    viewModel      : HomeViewModel = viewModel(factory = HomeViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState: LazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScentBlack)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color    = ScentGold
            )
        } else {
            HomeContent(
                uiState          = uiState,
                listState        = listState,
                onSearchClick    = onSearchClick,
                onProductClick   = onProductClick,
                onFavoriteToggle = { viewModel.toggleFavorite(it) }
            )
        }
    }
}

@Composable
private fun HomeContent(
    uiState          : HomeUiState,
    listState        : LazyListState,
    onSearchClick    : () -> Unit,
    onProductClick   : (Int) -> Unit,
    onFavoriteToggle : (Int) -> Unit
) {
    LazyColumn(
        state          = listState,
        modifier       = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item(key = "topbar") {
            ScentTopBar()
        }
        item(key = "search") {
            ScentSearchBarButton(
                onClick  = onSearchClick,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
        uiState.heroBanner?.let { banner ->
            item(key = "hero") {
                HeroBannerCard(
                    banner   = banner,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
        item(key = "section_header") {
            CollectionHeader(
                modifier = Modifier.padding(
                    start  = 20.dp,
                    end    = 20.dp,
                    top    = 28.dp,
                    bottom = 16.dp
                )
            )
        }
        val products = uiState.filteredProducts
        val rows     = products.chunked(2)

        items(
            count = rows.size,
            key   = { "row_$it" }
        ) { rowIndex ->
            ProductRow(
                products         = rows[rowIndex],
                onProductClick   = onProductClick,
                onFavoriteToggle = onFavoriteToggle,
                modifier         = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun ScentTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector        = Icons.Default.Menu,
            contentDescription = "Menu",
            tint               = ScentWhite,
            modifier           = Modifier.size(24.dp)
        )
        Text(
            text  = "SCENT",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight    = FontWeight.Bold,
                letterSpacing = 6.sp,
                fontSize      = 18.sp
            ),
            color = ScentWhite
        )
        Spacer(Modifier.size(24.dp))
    }
}

@Composable
private fun ScentSearchBarButton(
    onClick  : () -> Unit,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ScentSearchBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector        = Icons.Default.Search,
                contentDescription = "Search",
                tint               = ScentTextMuted,
                modifier           = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text  = "Cari esens Anda...",
                style = MaterialTheme.typography.bodyMedium.copy(color = ScentTextMuted)
            )
        }
    }
}

@Composable
private fun HeroBannerCard(
    banner   : HeroBanner,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(banner.gradientStart),
                        Color(banner.gradientEnd)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0x00000000), Color(0xCC000000))
                    )
                )
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 32.dp)
                .width(80.dp)
                .height(160.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(70.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF4A4A4A), Color(0xFF1A1A1A))
                        )
                    )
                    .border(0.5.dp, Color(0xFF666666), RoundedCornerShape(6.dp))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(30.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(Color(0xFF222222))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 22.dp)
                    .width(14.dp)
                    .height(18.dp)
                    .background(Color(0xFF333333))
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                text  = banner.tag,
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    color         = ScentGold
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text  = banner.title,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text     = banner.description,
                style    = MaterialTheme.typography.bodySmall.copy(
                    color      = ScentTextPrimary,
                    lineHeight = 18.sp
                ),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CollectionHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text  = "KURASI",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                color         = ScentTextLabel
            )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text  = "Koleksi Kami",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ProductRow(
    products         : List<Product>,
    onProductClick   : (Int) -> Unit,
    onFavoriteToggle : (Int) -> Unit,
    modifier         : Modifier = Modifier
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        products.forEach { product ->
            ProductCard(
                product          = product,
                onClick          = { onProductClick(product.id) },
                onFavoriteToggle = { onFavoriteToggle(product.id) },
                modifier         = Modifier.weight(1f)
            )
        }
        if (products.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun ProductCard(
    product          : Product,
    onClick          : () -> Unit,
    onFavoriteToggle : () -> Unit,
    modifier         : Modifier = Modifier
) {
    val heartTint by animateColorAsState(
        targetValue   = if (product.isFavorite) ScentGold
        else ScentWhite.copy(alpha = 0.7f),
        animationSpec = tween(300),
        label         = "heartColor_${product.id}"
    )

    Column(modifier = modifier.clickable(onClick = onClick)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(product.cardColor).copy(alpha = 0.9f),
                            Color(product.cardColor).copy(alpha = 0.5f)
                        )
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(44.dp)
                    .height(80.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .width(40.dp)
                        .height(66.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(product.accentColor).copy(alpha = 0.5f),
                                    Color(product.accentColor).copy(alpha = 0.1f)
                                )
                            )
                        )
                        .border(
                            0.5.dp,
                            Color(product.accentColor).copy(alpha = 0.4f),
                            RoundedCornerShape(4.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(16.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
                        .background(Color(product.accentColor).copy(alpha = 0.4f))
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(ScentBlack.copy(alpha = 0.5f))
                    .clickable(onClick = onFavoriteToggle),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = if (product.isFavorite) Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorit",
                    tint               = heartTint,
                    modifier           = Modifier.size(16.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        Text(
            text  = product.brand,
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 1.5.sp,
                color         = ScentTextLabel
            )
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text     = product.name,
            style    = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text  = product.price,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color      = ScentWhite
                )
            )
            Text(
                text  = product.volume,
                style = MaterialTheme.typography.bodySmall.copy(color = ScentTextMuted)
            )
        }
    }
}