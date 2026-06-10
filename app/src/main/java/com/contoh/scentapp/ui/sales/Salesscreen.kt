package com.contoh.scentapp.ui.sales

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
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
import com.contoh.scentapp.data.model.ActiveOrder
import com.contoh.scentapp.data.model.OrderStatus
import com.contoh.scentapp.data.model.SalesProduct
import com.contoh.scentapp.ui.theme.*


@Composable
fun SalesScreen(
    onBack         : () -> Unit = {},
    onAddProduct   : () -> Unit = {},
    viewModel      : SalesViewModel = viewModel(factory = SalesViewModelFactory())
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
                    Icon(
                        imageVector        = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint               = ScentWhite,
                        modifier           = Modifier.size(24.dp)
                    )
                }
            }
            item(key = "header") {
                Column(
                    modifier = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        top    = 4.dp,
                        bottom = 20.dp
                    )
                ) {
                    Text(
                        text  = "PERFORMA ATELIER",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 28.sp,
                            lineHeight = 34.sp
                        ),
                        color = ScentWhite
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text  = "Tinjauan komprehensif tentang kreasi olfaktori dan logistik musiman Anda.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color      = ScentTextMuted,
                            lineHeight = 18.sp
                        )
                    )
                    Spacer(Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, ScentDivider, RoundedCornerShape(10.dp))
                            .clickable(onClick = onAddProduct)
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector        = Icons.Default.Add,
                                contentDescription = null,
                                tint               = ScentWhite,
                                modifier           = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text  = "TAMBAH PRODUK BARU",
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

            item(key = "stats") {
                Column(
                    modifier            = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label    = "TOTAL PENDAPATAN",
                        value    = uiState.formattedPendapatan,
                        subLabel = uiState.growthPercent,
                        isLarge  = true
                    )
                    StatCard(
                        label   = "TOTAL PENJUALAN",
                        value   = "%,d".format(uiState.totalPenjualan).replace(",", "."),
                        isLarge = false
                    )
                }
                Spacer(Modifier.height(28.dp))
            }
            item(key = "koleksi_header") {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text  = "KOLEKSI",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 20.sp
                        ),
                        color = ScentWhite
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.FilterList, null,
                            tint = ScentTextMuted, modifier = Modifier.size(22.dp))
                        Icon(Icons.Default.Search, null,
                            tint = ScentTextMuted, modifier = Modifier.size(22.dp))
                    }
                }
            }
            items(
                items = uiState.products,
                key   = { "sales_product_${it.id}" }
            ) { product ->
                SalesProductItem(
                    product  = product,
                    onEdit   = { },
                    onDelete = { viewModel.deleteProduct(product.id) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
                HorizontalDivider(
                    color     = ScentDivider,
                    thickness = 0.5.dp,
                    modifier  = Modifier.padding(horizontal = 20.dp)
                )
            }
            item(key = "pengiriman_header") {
                Spacer(Modifier.height(28.dp))
                Text(
                    text     = "PENGIRIMAN AKTIF",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    style    = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp
                    ),
                    color    = ScentWhite
                )
            }

            items(
                items = uiState.activeOrders,
                key   = { "order_${it.orderId}" }
            ) { order ->
                ActiveOrderCard(
                    order        = order,
                    onMarkPacked = { viewModel.markAsPacked(order.orderId) },
                    onMarkShipped = { viewModel.markAsShipped(order.orderId) },
                    modifier     = Modifier.padding(
                        horizontal = 20.dp,
                        vertical   = 6.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label    : String,
    value    : String,
    subLabel : String?  = null,
    isLarge  : Boolean  = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ScentBlack)
            .padding(20.dp)
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize      = 10.sp,
                letterSpacing = 2.sp,
                color         = ScentTextLabel
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text  = value,
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize   = if (isLarge) 32.sp else 28.sp
            ),
            color = ScentWhite
        )
        if (subLabel != null) {
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint               = Color(0xFF4CAF50),
                    modifier           = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text  = subLabel,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = ScentTextMuted
                    )
                )
            }
        }
    }
}

@Composable
private fun SalesProductItem(
    product  : SalesProduct,
    onEdit   : () -> Unit,
    onDelete : () -> Unit,
    modifier : Modifier = Modifier
) {
    Row(
        modifier          = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
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
                        .width(8.dp).height(6.dp)
                        .background(Color(product.accentColor).copy(alpha = 0.5f))
                )
                Box(
                    modifier = Modifier
                        .width(28.dp).height(45.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(product.accentColor).copy(alpha = 0.2f))
                        .border(0.5.dp, Color(product.accentColor).copy(alpha = 0.4f),
                            RoundedCornerShape(3.dp))
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = product.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp
                ),
                color = ScentWhite
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = "${product.aromaFamily} • ${product.volume} • ${product.stockStatus}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (product.stockStatus == "STOK MENIPIS")
                        Color(0xFFD4A853) else ScentTextMuted
                )
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(
                    imageVector        = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint               = ScentTextMuted,
                    modifier           = Modifier
                        .size(20.dp)
                        .clickable(onClick = onEdit)
                )
                Icon(
                    imageVector        = Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint               = ScentTextMuted,
                    modifier           = Modifier
                        .size(20.dp)
                        .clickable(onClick = onDelete)
                )
            }
        }
    }
}

@Composable
private fun ActiveOrderCard(
    order         : ActiveOrder,
    onMarkPacked  : () -> Unit,
    onMarkShipped : () -> Unit,
    modifier      : Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ScentBlack)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text  = "ORDER #${order.orderId}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 10.sp,
                        letterSpacing = 1.5.sp,
                        color         = ScentTextLabel
                    )
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = "${order.buyerName} • ${order.itemCount} Item",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp
                    ),
                    color = ScentWhite
                )
            }
            // Status badge
            if (order.status != OrderStatus.DALAM_PROSES) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ScentSearchBg)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text  = "✓ ${order.status.label}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 10.sp,
                            letterSpacing = 1.sp,
                            color         = ScentGold
                        )
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ScentSearchBg)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text  = order.status.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 10.sp,
                            letterSpacing = 1.sp,
                            color         = ScentWhite
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        when (order.status) {
            OrderStatus.DALAM_PROSES -> {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, ScentDivider, RoundedCornerShape(8.dp))
                            .clickable(onClick = onMarkPacked)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = "TANDAI DIKEMAS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize      = 9.sp,
                                letterSpacing = 1.5.sp,
                                fontWeight    = FontWeight.Bold,
                                color         = ScentWhite
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, ScentDivider, RoundedCornerShape(8.dp))
                            .clickable(onClick = onMarkShipped)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = "TANDAI DIKIRIM",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize      = 9.sp,
                                letterSpacing = 1.5.sp,
                                fontWeight    = FontWeight.Bold,
                                color         = ScentWhite
                            )
                        )
                    }
                }
            }
            OrderStatus.DIKEMAS -> {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ScentWhite)
                            .clickable(onClick = onMarkShipped)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = "KIRIM SEKARANG",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize      = 10.sp,
                                letterSpacing = 1.5.sp,
                                fontWeight    = FontWeight.Bold,
                                color         = ScentBlack
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, ScentDivider, RoundedCornerShape(8.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Default.MoreHoriz,
                            contentDescription = "Lainnya",
                            tint               = ScentWhite,
                            modifier           = Modifier.size(20.dp)
                        )
                    }
                }
            }
            OrderStatus.DIKIRIM -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ScentSearchBg)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text  = "DALAM PENGIRIMAN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 10.sp,
                            letterSpacing = 1.5.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = ScentTextMuted
                        )
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ScentSearchBg)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text  = order.status.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 10.sp,
                            letterSpacing = 1.5.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = ScentTextMuted
                        )
                    )
                }
            }
        }
    }
}