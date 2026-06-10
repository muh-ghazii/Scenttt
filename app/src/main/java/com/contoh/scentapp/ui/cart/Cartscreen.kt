package com.contoh.scentapp.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.contoh.scentapp.data.model.CartItem
import com.contoh.scentapp.ui.theme.*

@Composable
fun CartScreen(
    onBack            : () -> Unit = {},
    onCheckout        : () -> Unit = {},
    onContinueShopping: () -> Unit = {},
    viewModel         : CartViewModel = viewModel(factory = CartViewModelFactory())
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
            contentPadding = PaddingValues(bottom = 140.dp)
        ) {
            item(key = "topbar") {
                CartTopBar(
                    itemCount = uiState.totalItems,
                    onBack    = onBack
                )
            }
            item(key = "header") {
                CartHeader(
                    subtitle = uiState.headerSubtitle,
                    modifier = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        top    = 8.dp,
                        bottom = 24.dp
                    )
                )
            }
            items(
                items = uiState.items,
                key   = { "cart_${it.productId}" }
            ) { item ->
                CartItemCard(
                    item       = item,
                    onIncrease = { viewModel.increaseQty(item.productId) },
                    onDecrease = { viewModel.decreaseQty(item.productId) },
                    onDelete   = { viewModel.removeItem(item.productId) },
                    modifier   = Modifier.padding(
                        horizontal = 20.dp,
                        vertical   = 8.dp
                    )
                )
                HorizontalDivider(
                    color     = ScentDivider,
                    thickness = 0.5.dp,
                    modifier  = Modifier.padding(horizontal = 20.dp)
                )
            }
            item(key = "summary") {
                Spacer(Modifier.height(32.dp))
                CartSummary(
                    subtotal  = uiState.formattedSubtotal,
                    total     = uiState.formattedTotal,
                    modifier  = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(ScentBlack)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ScentWhite)
                    .clickable(onClick = onCheckout)
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "MENGATUR PENGIRIMAN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 12.sp,
                        letterSpacing = 2.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = ScentBlack
                    )
                )
            }
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, ScentDivider, RoundedCornerShape(10.dp))
                    .clickable(onClick = onContinueShopping)
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "LANJUT BELANJA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 12.sp,
                        letterSpacing = 2.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = ScentWhite
                    )
                )
            }
        }
    }
}

@Composable
private fun CartTopBar(itemCount: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
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
        Box {
            Icon(
                imageVector        = Icons.Default.ShoppingBag,
                contentDescription = "Keranjang",
                tint               = ScentWhite,
                modifier           = Modifier.size(24.dp)
            )
            if (itemCount > 0) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(ScentGold)
                        .align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
private fun CartHeader(subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text  = "PILIHAN SAYA",
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize   = 32.sp
            ),
            color = ScentWhite
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text  = subtitle,
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                color         = ScentTextLabel,
                fontSize      = 11.sp
            )
        )
    }
}

@Composable
private fun CartItemCard(
    item       : CartItem,
    onIncrease : () -> Unit,
    onDecrease : () -> Unit,
    onDelete   : () -> Unit,
    modifier   : Modifier = Modifier
) {
    Row(
        modifier          = modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(width = 110.dp, height = 130.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(item.cardColor).copy(alpha = 0.9f),
                            Color(item.cardColor).copy(alpha = 0.6f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(12.dp).height(8.dp)
                        .background(Color(item.accentColor).copy(alpha = 0.5f))
                )
                Box(
                    modifier = Modifier
                        .width(36.dp).height(60.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(item.accentColor).copy(alpha = 0.2f))
                        .border(
                            0.5.dp,
                            Color(item.accentColor).copy(alpha = 0.4f),
                            RoundedCornerShape(4.dp)
                        )
                )
            }
        }

        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text  = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp
                        ),
                        color = ScentWhite
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text  = "${item.aromaProfile} • ${item.volume}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color      = ScentTextMuted,
                            lineHeight = 18.sp
                        )
                    )
                }
                Icon(
                    imageVector        = Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint               = ScentTextMuted,
                    modifier           = Modifier
                        .size(20.dp)
                        .clickable(onClick = onDelete)
                )
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ScentSearchBg),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable(onClick = onDecrease),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = "−",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = ScentWhite
                            )
                        )
                    }
                    Text(
                        text  = item.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color      = ScentWhite
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable(onClick = onIncrease),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = "+",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = ScentWhite
                            )
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))
                Text(
                    text  = item.formattedPrice,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color      = ScentWhite
                    )
                )
            }
        }
    }
}

@Composable
private fun CartSummary(
    subtotal : String,
    total    : String,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text  = "SUBTOTAL",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    color         = ScentTextLabel,
                    fontSize      = 11.sp
                )
            )
            Text(
                text  = subtotal.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.sp,
                    color         = ScentTextMuted,
                    fontSize      = 11.sp
                )
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text  = "PENGIRIMAN ATELIER",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    color         = ScentTextLabel,
                    fontSize      = 11.sp
                )
            )
            Text(
                text  = "GRATIS",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.sp,
                    color         = ScentTextMuted,
                    fontSize      = 11.sp
                )
            )
        }

        HorizontalDivider(
            color     = ScentDivider,
            thickness = 0.5.dp,
            modifier  = Modifier.padding(vertical = 20.dp)
        )
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text  = "ESTIMASI TOTAL",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    color         = ScentTextLabel,
                    fontSize      = 11.sp
                )
            )
            Text(
                text  = total,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 28.sp
                ),
                color = ScentWhite
            )
        }
    }
}