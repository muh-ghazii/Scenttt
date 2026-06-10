package com.contoh.scentapp.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.ui.theme.*

@Composable
fun UploadBuktiPembayaranScreen(
    totalPembayaran : String  = "Rp 1.465.000",
    onBack          : () -> Unit = {},
    onSubmit        : () -> Unit = {}
) {
    val listState        = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current

    var isFotoUploaded  by rememberSaveable { mutableStateOf(false) }
    var isCopied        by rememberSaveable { mutableStateOf(false) }

    val nomorRekening = "1234567890"
    val namaBank      = "BCA"
    val namaPenerima  = "SCENT ATELIER"

    Box(
        modifier = Modifier
            .fillMaxSize()
            // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state          = listState,
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
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
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        tint               = MaterialTheme.colorScheme.onBackground,
                        modifier           = Modifier.size(24.dp).clickable(onClick = onBack)
                    )
                    Text(
                        text  = "SCENT",
                        style = MaterialTheme.typography.titleLarge.copy(
                            letterSpacing = 6.sp, fontSize = 18.sp, fontWeight = FontWeight.Bold
                        ),
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.size(24.dp))
                }
            }
            item(key = "header") {
                Column(
                    modifier = Modifier.padding(
                        start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp
                    )
                ) {
                    Text(
                        text  = "Upload Bukti\nPembayaran",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp
                        ),
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text  = "Transfer ke rekening berikut dan upload bukti pembayaran.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ScentTextMuted, lineHeight = 22.sp
                        )
                    )
                }
            }
            item(key = "rekening") {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.surfaceVariant
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector        = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint               = ScentGold,
                            modifier           = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text  = "INFORMASI REKENING",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp, letterSpacing = 2.sp, color = ScentGold
                            )
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    RekeningRow(label = "Bank",      value = namaBank)
                    Spacer(Modifier.height(10.dp))
                    RekeningRow(label = "Atas Nama", value = namaPenerima)
                    Spacer(Modifier.height(10.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text  = "Nomor Rekening",
                                style = MaterialTheme.typography.bodySmall.copy(color = ScentTextMuted)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text  = nomorRekening,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold, letterSpacing = 2.sp
                                ),
                                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isCopied) ScentGold.copy(alpha = 0.2f)
                                    // ✅ FIX: Ganti ScentSearchBg → MaterialTheme.colorScheme.background
                                    else MaterialTheme.colorScheme.background
                                )
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(nomorRekening))
                                    isCopied = true
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector        = if (isCopied) Icons.Default.CheckCircle else Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    tint               = if (isCopied) ScentGold else ScentTextMuted,
                                    modifier           = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text  = if (isCopied) "Tersalin!" else "Salin",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        color    = if (isCopied) ScentGold else ScentTextMuted
                                    )
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = ScentDivider, thickness = 0.5.dp)
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text  = "Total Transfer",
                            style = MaterialTheme.typography.bodyMedium.copy(color = ScentTextMuted)
                        )
                        Text(
                            text  = totalPembayaran,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ScentGold
                            )
                        )
                    }
                }
            }

            item(key = "upload") {
                Spacer(Modifier.height(24.dp))
                Text(
                    text     = "UPLOAD BUKTI TRANSFER",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    style    = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp, letterSpacing = 2.sp, color = ScentTextLabel
                    )
                )
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.surfaceVariant
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            width = 1.dp,
                            color = if (isFotoUploaded) ScentGold else ScentDivider,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { isFotoUploaded = !isFotoUploaded },
                    contentAlignment = Alignment.Center
                ) {
                    if (isFotoUploaded) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector        = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint               = ScentGold,
                                modifier           = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text  = "Bukti berhasil diupload",
                                style = MaterialTheme.typography.bodyMedium.copy(color = ScentGold)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text  = "Tap untuk ganti foto",
                                style = MaterialTheme.typography.bodySmall.copy(color = ScentTextMuted)
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector        = Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint               = ScentTextMuted,
                                modifier           = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text      = "Tap untuk upload foto\nbukti transfer",
                                style     = MaterialTheme.typography.bodyMedium.copy(
                                    color = ScentTextMuted, lineHeight = 22.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            item(key = "catatan") {
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        // ✅ FIX: Ganti hardcode Color(0xFF1A1A2E) → MaterialTheme.colorScheme.surfaceVariant
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp)
                ) {
                    Text(
                        text  = "⚠️  Pastikan nominal transfer sesuai dengan total tagihan. Penjual akan mengkonfirmasi pembayaran dalam 1x24 jam.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ScentTextMuted, lineHeight = 18.sp
                        )
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
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        if (isFotoUploaded) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable(enabled = isFotoUploaded, onClick = onSubmit)
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "KIRIM BUKTI PEMBAYARAN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 12.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold,
                        // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
                        color = if (isFotoUploaded) MaterialTheme.colorScheme.background
                        else ScentTextMuted
                    )
                )
            }
        }
    }
}

@Composable
private fun RekeningRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.bodySmall.copy(color = ScentTextMuted)
        )
        Text(
            text  = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}