package com.contoh.scentapp.ui.ordersuccess

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.ui.theme.*

@Composable
fun OrderSuccessScreen(
    isTransfer   : Boolean = false,
    orderId      : String = "#SCNT-99283",
    totalPayment : String = "Rp 2.495.000",
    estimasi     : String = "2-3 Hari Kerja",
    onBackHome   : () -> Unit = {}
) {
    val instruksiText = if (isTransfer) {
        "Terima kasih atas pesanan Anda. Pembayaran transfer Anda sedang diproses dan akan segera dikonfirmasi."
    } else {
        "Terima kasih atas pesanan Anda. Silakan siapkan pembayaran saat kurir mengantarkan paket Anda."
    }

    val metodeText = if (isTransfer) "Transfer Bank" else "Bayar di Tempat (COD)"
    val metodeIcon = if (isTransfer) Icons.Default.AccountBalance else Icons.Default.Money

    Box(
        // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup",
                    // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp).clickable(onClick = onBackHome)
                )
                Text(
                    text = "SCENT",
                    style = MaterialTheme.typography.titleLarge.copy(
                        letterSpacing = 6.sp, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    ),
                    // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.size(24.dp))
            }
            Spacer(Modifier.height(32.dp))
            Box(
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(20.dp))
                    // ✅ FIX: Ganti hardcode → MaterialTheme.colorScheme.surfaceVariant
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Berhasil",
                    // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(Modifier.height(28.dp))
            Text(
                text = "Pesanan Berhasil\nDitempatkan",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp
                ),
                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = instruksiText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = ScentTextMuted, lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    // ✅ FIX: Ganti hardcode → MaterialTheme.colorScheme.surfaceVariant
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(20.dp)
            ) {
                OrderDetailRow(label = "ORDER ID", value = orderId, isLabelSmall = true)
                HorizontalDivider(color = ScentDivider, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
                Column {
                    Text(
                        text = "METODE PEMBAYARAN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = metodeIcon,
                            contentDescription = null,
                            tint = ScentTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = metodeText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
                HorizontalDivider(color = ScentDivider, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
                Column {
                    Text(
                        text = "ESTIMASI TIBA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = ScentTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = estimasi,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
                HorizontalDivider(color = ScentDivider, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Pembayaran",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Text(
                        text = totalPayment,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold, fontSize = 22.sp,
                            // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                    // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickable(onClick = onBackHome)
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "KEMBALI KE BERANDA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold,
                        // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
                        color = MaterialTheme.colorScheme.background
                    )
                )
            }
        }
    }
}

@Composable
private fun OrderDetailRow(label: String, value: String, isLabelSmall: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isLabelSmall) MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
            )
            else MaterialTheme.typography.bodyMedium.copy(color = ScentTextMuted)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}