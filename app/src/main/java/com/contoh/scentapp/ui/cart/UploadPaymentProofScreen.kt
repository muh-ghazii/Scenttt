package com.contoh.scentapp.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
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
fun UploadPaymentProofScreen(
    onBack: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    var isUploaded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onBack)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "UPLOAD BUKTI",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    ),
                    // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Instruksi Pembayaran",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Silakan transfer sesuai total tagihan ke rekening berikut:\nBCA - 1234567890 a.n Scent Official",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = ScentTextMuted,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    // ✅ FIX: Ganti Color(0xFF161616) → MaterialTheme.colorScheme.surfaceVariant
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = 1.dp,
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        color = if (isUploaded) MaterialTheme.colorScheme.onBackground else ScentDivider,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { isUploaded = !isUploaded },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Upload",
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        tint = if (isUploaded) MaterialTheme.colorScheme.onBackground else ScentTextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isUploaded) "Bukti berhasil dipilih (Ketuk untuk ganti)" else "Ketuk untuk upload gambar",
                        style = MaterialTheme.typography.bodyMedium,
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        color = if (isUploaded) MaterialTheme.colorScheme.onBackground else ScentTextMuted
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        // ✅ FIX: Ganti ScentWhite/Color(0xFF333333) → MaterialTheme.colorScheme
                        if (isUploaded) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable(enabled = isUploaded, onClick = onSubmit)
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "KONFIRMASI PEMBAYARAN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold,
                        // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
                        color = if (isUploaded) MaterialTheme.colorScheme.background
                        else ScentTextMuted
                    )
                )
            }
        }
    }
}