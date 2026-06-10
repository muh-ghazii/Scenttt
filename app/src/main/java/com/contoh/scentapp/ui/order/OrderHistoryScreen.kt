package com.contoh.scentapp.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.ui.theme.*

@Composable
fun OrderHistoryScreen(onBack: () -> Unit, onOrderDetailClick: (String) -> Unit) {
    val tabs = listOf("Semua", "Belum Bayar", "Dikemas", "Dikirim", "Selesai", "Batal")
    var selectedTab by remember { mutableStateOf(tabs[0]) }
    val listIdPesanan = listOf("#SCNT-99283", "#SCNT-99284", "#SCNT-99285", "#SCNT-99286")

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier          = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint               = MaterialTheme.colorScheme.onBackground,
                    modifier           = Modifier.size(24.dp).clickable(onClick = onBack)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text  = "RIWAYAT PESANAN",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            LazyRow(
                contentPadding        = PaddingValues(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.padding(bottom = 16.dp)
            ) {
                items(tabs.size) { index ->
                    val tab        = tabs[index]
                    val isSelected = tab == selectedTab
                    Text(
                        text  = tab,
                        color = if (isSelected) MaterialTheme.colorScheme.onBackground else ScentTextMuted,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
            LazyColumn(
                contentPadding      = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listIdPesanan.size) { index ->
                    val idPesanan = listIdPesanan[index]
                    OrderCardItem(
                        orderId = idPesanan,
                        status  = if (idPesanan == "#SCNT-99283") "Dikirim" else "Selesai",
                        date    = "10 Juni 2026",
                        total   = "Rp 1.450.000",
                        onClick = { onOrderDetailClick(idPesanan) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderCardItem(orderId: String, status: String, date: String, total: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, ScentDivider, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = date,   style = MaterialTheme.typography.labelSmall.copy(color = ScentTextMuted))
            Text(text = status, style = MaterialTheme.typography.labelSmall.copy(color = ScentGold, fontWeight = FontWeight.Bold))
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text  = orderId,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(4.dp))
        Text(text = "Santal Blanc - 50ml", style = MaterialTheme.typography.bodySmall.copy(color = ScentTextMuted))
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = ScentDivider, thickness = 0.5.dp)
        Spacer(Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Total Pembayaran", style = MaterialTheme.typography.bodySmall.copy(color = ScentTextMuted))
            Text(
                text  = total,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}