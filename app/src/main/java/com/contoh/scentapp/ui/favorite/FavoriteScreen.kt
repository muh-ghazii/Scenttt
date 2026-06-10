package com.contoh.scentapp.ui.favorite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.contoh.scentapp.data.model.Product
import com.contoh.scentapp.ui.theme.ScentBlack
import com.contoh.scentapp.ui.theme.ScentDivider
import com.contoh.scentapp.ui.theme.ScentGold
import com.contoh.scentapp.ui.theme.ScentSearchBg
import com.contoh.scentapp.ui.theme.ScentTextLabel
import com.contoh.scentapp.ui.theme.ScentTextMuted
import com.contoh.scentapp.ui.theme.ScentWhite

@Composable
fun FavoriteScreen(
    onBack         : () -> Unit = {},
    onProductClick : (Int) -> Unit = {},
    viewModel      : FavoriteViewModel = viewModel(factory = FavoriteViewModelFactory())
) {
    val uiState   by viewModel.uiState.collectAsStateWithLifecycle()
    val listState  = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScentBlack)
    ) {
        LazyColumn(
            state          = listState,
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            item(key = "topbar") {
                FavoriteTopBar(onBack = onBack)
            }
            item(key = "header") {
                FavoriteHeader(
                    modifier = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        top    = 8.dp,
                        bottom = 24.dp
                    )
                )
            }
            if (uiState.isEmpty && !uiState.isLoading) {
                item(key = "empty") {
                    EmptyFavoriteState(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(top = 60.dp)
                    )
                }
            }
            items(
                items = uiState.favorites,
                key   = { product -> "fav_${product.id}" }
            ) { product ->
                AnimatedVisibility(
                    visible = true,
                    enter   = fadeIn() + slideInVertically(),
                    exit    = fadeOut() + slideOutVertically()
                ) {
                    FavoriteItemCard(
                        product        = product,
                        onProductClick = { onProductClick(product.id) },
                        onRemove       = { viewModel.removeFromFavorite(product.id) },
                        modifier       = Modifier.padding(
                            horizontal = 20.dp,
                            vertical   = 4.dp
                        )
                    )
                }
                HorizontalDivider(
                    color     = ScentDivider,
                    thickness = 0.5.dp,
                    modifier  = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun FavoriteTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector        = Icons.Default.ArrowBack,
            contentDescription = "Kembali",
            tint               = ScentWhite,
            modifier           = Modifier
                .size(24.dp)
                .clickable(onClick = onBack)
        )
        Text(
            text  = "SCENT",
            style = MaterialTheme.typography.titleLarge.copy(
                letterSpacing = 6.sp,
                fontSize      = 18.sp,
                fontWeight    = FontWeight.Bold
            ),
            color = ScentWhite
        )
        Spacer(Modifier.size(24.dp))
    }
}

@Composable
private fun FavoriteHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text  = "Kurasi Anda.",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight  = FontWeight.Bold,
                fontSize    = 36.sp,
                lineHeight  = 42.sp,
            ),
            color = ScentWhite
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text  = "Koleksi wewangian terpilih yang mencerminkan identitas dan karakter personal Anda.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color      = ScentTextMuted,
                lineHeight = 22.sp
            )
        )
    }
}

@Composable
private fun EmptyFavoriteState(modifier: Modifier = Modifier) {
    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector        = Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint               = ScentGold,
            modifier           = Modifier.size(56.dp)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text      = "Koleksi Favorit Kosong",
            style     = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            ),
            color     = ScentWhite,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text      = "Tekan ikon pada parfum\nuntuk menyimpannya di sini",
            style     = MaterialTheme.typography.bodyMedium.copy(
                color      = ScentTextMuted,
                lineHeight = 22.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FavoriteItemCard(
    product        : Product,
    onProductClick : () -> Unit,
    onRemove       : () -> Unit,
    modifier       : Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
            .padding(vertical = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier          = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(product.cardColor).copy(alpha = 0.9f),
                                Color(product.cardColor).copy(alpha = 0.5f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height(6.dp)
                            .background(Color(product.accentColor).copy(alpha = 0.5f))
                    )
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(product.accentColor).copy(alpha = 0.25f))
                            .border(
                                0.5.dp,
                                Color(product.accentColor).copy(alpha = 0.4f),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }

            Spacer(Modifier.width(16.dp))

            // Info produk
            Column {
                Text(
                    text  = product.collection.ifBlank { product.brand },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 10.sp,
                        letterSpacing = 1.5.sp,
                        color         = ScentTextLabel
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = product.name.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize   = 18.sp,
                        lineHeight = 22.sp
                    ),
                    color = ScentWhite
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text  = product.price,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color      = ScentTextMuted,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Default.Close,
                contentDescription = "Hapus dari favorit",
                tint               = ScentTextMuted,
                modifier           = Modifier.size(18.dp)
            )
        }
    }
}