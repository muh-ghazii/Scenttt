package com.contoh.scentapp.ui.shipping

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.data.model.ShippingOption
import com.contoh.scentapp.data.repository.CartRepository
import com.contoh.scentapp.ui.theme.*

@Composable
fun ShippingScreen(
    onBack       : () -> Unit = {},
    onCODSuccess : () -> Unit = {},
    onTransfer   : () -> Unit = {}
) {
    val repository    = CartRepository.getInstance()
    val listState     = rememberLazyListState()

    var selectedKurirId by rememberSaveable { mutableStateOf("jnt") }
    var selectedPayment by rememberSaveable { mutableStateOf("") }

    val shippingOptions = repository.shippingOptions
    val selectedOption  = shippingOptions.find { it.id == selectedKurirId }
        ?: shippingOptions.first()

    val subtotal    = 1_450_000
    val shippingFee = selectedOption.price
    val total       = subtotal + shippingFee

    fun formatRp(value: Int) = "Rp ${"%,d".format(value).replace(",", ".")}"

    // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state          = listState,
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // ── Top Bar ───────────────────────────────────────────────────────
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
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text  = "Checkout",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ScentTextMuted
                        )
                    )
                }
            }

            // ── Header ────────────────────────────────────────────────────────
            item(key = "header") {
                Column(
                    modifier = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        top    = 8.dp,
                        bottom = 24.dp
                    )
                ) {
                    Text(
                        text  = "Metode Pengiriman",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 26.sp
                        ),
                        // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text  = "Pilih kurir dan metode pembayaran untuk pesanan Anda.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color      = ScentTextMuted,
                            lineHeight = 22.sp
                        )
                    )
                }
            }

            // ── Pilihan Kurir ─────────────────────────────────────────────────
            item(key = "kurir_label") {
                ShippingSectionLabel(
                    text     = "PILIH KURIR",
                    modifier = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        bottom = 12.dp
                    )
                )
            }

            items(
                count = shippingOptions.size,
                key   = { shippingOptions[it].id }
            ) { index ->
                val option     = shippingOptions[index]
                val isSelected = option.id == selectedKurirId

                val borderColor by animateColorAsState(
                    // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                    targetValue   = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                    animationSpec = tween(200),
                    label         = "kurirBorder_${option.id}"
                )
                val bgColor by animateColorAsState(
                    // ✅ FIX: Ganti ScentSearchBg → MaterialTheme.colorScheme.surfaceVariant
                    targetValue   = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
                    animationSpec = tween(200),
                    label         = "kurirBg_${option.id}"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                        .clickable { selectedKurirId = option.id }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            // ✅ FIX: Ganti hardcode → MaterialTheme.colorScheme.surfaceVariant
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = iconForKurir(option.iconType),
                            contentDescription = option.name,
                            // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                            tint               = MaterialTheme.colorScheme.onBackground,
                            modifier           = Modifier.size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text  = option.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize   = 15.sp
                                ),
                                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    // ✅ FIX: Ganti hardcode → MaterialTheme.colorScheme.surfaceVariant
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text  = option.badge,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize      = 8.sp,
                                        letterSpacing = 1.sp,
                                        color         = ScentTextMuted
                                    )
                                )
                            }
                        }
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text  = option.estimasi,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = ScentTextMuted
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text  = option.formattedPrice,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize   = 14.sp,
                                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                                color      = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        RadioButton(
                            selected = isSelected,
                            onClick  = { selectedKurirId = option.id },
                            colors   = RadioButtonDefaults.colors(
                                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                                selectedColor   = MaterialTheme.colorScheme.onBackground,
                                unselectedColor = ScentTextMuted
                            )
                        )
                    }
                }
            }

            // ── Metode Pembayaran ─────────────────────────────────────────────
            item(key = "payment_label") {
                Spacer(Modifier.height(20.dp))
                ShippingSectionLabel(
                    text     = "METODE PEMBAYARAN",
                    modifier = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        bottom = 12.dp
                    )
                )
            }

            // COD
            item(key = "cod") {
                PaymentOptionCard(
                    icon       = Icons.Default.Money,
                    title      = "COD (Bayar di Tempat)",
                    subtitle   = "Bayar tunai saat bertemu langsung dengan penjual",
                    isSelected = selectedPayment == "cod",
                    onClick    = { selectedPayment = "cod" },
                    modifier   = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }

            // Transfer
            item(key = "transfer") {
                PaymentOptionCard(
                    icon       = Icons.Default.AccountBalance,
                    title      = "Transfer Bank",
                    subtitle   = "Transfer ke rekening penjual & upload bukti pembayaran",
                    isSelected = selectedPayment == "transfer",
                    onClick    = { selectedPayment = "transfer" },
                    modifier   = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }

            // ── Detail Pesanan ────────────────────────────────────────────────
            item(key = "order_summary") {
                Spacer(Modifier.height(20.dp))
                HorizontalDivider(
                    color     = ScentDivider,
                    thickness = 0.5.dp,
                    modifier  = Modifier.padding(horizontal = 20.dp)
                )
                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical   = 20.dp
                    )
                ) {
                    ShippingSectionLabel(text = "DETAIL PESANAN")
                    Spacer(Modifier.height(16.dp))
                    OrderSummaryRow("Subtotal Produk",  formatRp(subtotal))
                    Spacer(Modifier.height(10.dp))
                    OrderSummaryRow("Biaya Pengiriman", formatRp(shippingFee))
                    HorizontalDivider(
                        color     = ScentDivider,
                        thickness = 0.5.dp,
                        modifier  = Modifier.padding(vertical = 16.dp)
                    )
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            text  = "Total Tagihan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                                color      = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text  = formatRp(total),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize   = 20.sp,
                                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                                color      = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                }
            }
        }

        // ── Tombol Lanjutkan (sticky bottom) ──────────────────────────────────
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
                        if (selectedPayment.isNotEmpty()) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable(enabled = selectedPayment.isNotEmpty()) {
                        when (selectedPayment) {
                            "cod"      -> onCODSuccess()
                            "transfer" -> onTransfer()
                        }
                    }
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (selectedPayment) {
                        "transfer" -> "LANJUT UPLOAD BUKTI"
                        "cod"      -> "KONFIRMASI PESANAN"
                        else       -> "PILIH METODE PEMBAYARAN"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 12.sp,
                        letterSpacing = 2.sp,
                        fontWeight    = FontWeight.Bold,
                        // ✅ FIX: Ganti ScentBlack → MaterialTheme.colorScheme.background
                        color         = if (selectedPayment.isNotEmpty()) MaterialTheme.colorScheme.background
                        else ScentTextMuted
                    )
                )
            }
        }
    }
}

// ── Payment Option Card ───────────────────────────────────────────────────────

@Composable
private fun PaymentOptionCard(
    icon       : ImageVector,
    title      : String,
    subtitle   : String,
    isSelected : Boolean,
    onClick    : () -> Unit,
    modifier   : Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue   = if (isSelected) ScentGold else ScentDivider,
        animationSpec = tween(200),
        label         = "payBorder_$title"
    )
    val bgColor by animateColorAsState(
        targetValue   = if (isSelected) ScentGold.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        animationSpec = tween(200),
        label         = "payBg_$title"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                // ✅ FIX: Ganti hardcode → MaterialTheme.colorScheme.surfaceVariant
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = title,
                tint               = if (isSelected) ScentGold else ScentTextMuted,
                modifier           = Modifier.size(22.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp
                ),
                // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text  = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color      = ScentTextMuted,
                    lineHeight = 18.sp
                )
            )
        }
        RadioButton(
            selected = isSelected,
            onClick  = onClick,
            colors   = RadioButtonDefaults.colors(
                selectedColor   = ScentGold,
                unselectedColor = ScentTextMuted
            )
        )
    }
}

@Composable
private fun ShippingSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text     = text,
        modifier = modifier,
        style    = MaterialTheme.typography.labelSmall.copy(
            fontSize      = 10.sp,
            letterSpacing = 2.sp,
            color         = ScentTextLabel
        )
    )
}

@Composable
private fun OrderSummaryRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text  = label,
            // ✅ FIX: Ganti ScentTextMuted → MaterialTheme.colorScheme.onBackground untuk label
            style = MaterialTheme.typography.bodyMedium.copy(color = ScentTextMuted)
        )
        Text(
            text  = value,
            // ✅ FIX: Ganti ScentWhite → MaterialTheme.colorScheme.onBackground
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Composable
private fun iconForKurir(type: String): ImageVector = when (type) {
    "lightning" -> Icons.Default.FlashOn
    "plane"     -> Icons.Default.Flight
    "bike"      -> Icons.Default.DirectionsBike
    else        -> Icons.Default.LocalShipping
}