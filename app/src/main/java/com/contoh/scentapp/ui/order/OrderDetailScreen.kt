package com.contoh.scentapp.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.ui.theme.*

@Composable
fun OrderDetailScreen(
    orderId: String,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(ScentBlack)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali",
                    tint = ScentWhite, modifier = Modifier.size(24.dp).clickable(onClick = onBack)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "DETAIL PESANAN",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                    color = ScentWhite
                )
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Status Pesanan", style = MaterialTheme.typography.labelSmall.copy(color = ScentTextLabel, letterSpacing = 1.sp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Sedang Dikirim", style = MaterialTheme.typography.titleLarge.copy(color = ScentGold, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Estimasi tiba: 12 Juni 2026", style = MaterialTheme.typography.bodyMedium.copy(color = ScentTextMuted))
            }

            HorizontalDivider(color = ScentDivider, thickness = 0.5.dp)

            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "INFO PENGIRIMAN", style = MaterialTheme.typography.labelSmall.copy(color = ScentTextLabel, letterSpacing = 1.sp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "J&T Express - Resi: JP1234567890", style = MaterialTheme.typography.bodyMedium.copy(color = ScentWhite, fontWeight = FontWeight.Medium))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Alamat Pengiriman", style = MaterialTheme.typography.labelSmall.copy(color = ScentTextMuted))
                Text(
                    text = "Muhammad Ghazi Rakhmadi\nUniversitas Lambung Mangkurat, Banjarbaru",
                    style = MaterialTheme.typography.bodyMedium.copy(color = ScentWhite),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            HorizontalDivider(color = ScentDivider, thickness = 0.5.dp)

            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "RINCIAN PRODUK", style = MaterialTheme.typography.labelSmall.copy(color = ScentTextLabel, letterSpacing = 1.sp))
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Santal Blanc (50ml) x1", style = MaterialTheme.typography.bodyMedium.copy(color = ScentWhite))
                    Text(text = "Rp 1.450.000", style = MaterialTheme.typography.bodyMedium.copy(color = ScentWhite))
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Tombol Aksi di bagian bawah layar
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(ScentBlack).padding(horizontal = 20.dp, vertical = 16.dp).navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(ScentWhite).clickable { /* Konfirmasi Action */ }.padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("PESANAN DITERIMA", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = ScentBlack))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).border(1.dp, ScentWhite, RoundedCornerShape(10.dp)).clickable { /* Lapor Action */ }.padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("LAPOR MASALAH", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = ScentWhite))
            }
        }
    }
}