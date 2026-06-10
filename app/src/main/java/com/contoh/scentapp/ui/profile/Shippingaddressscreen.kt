package com.contoh.scentapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.ui.theme.*

@Composable
fun ShippingAddressScreen(
    onBack : () -> Unit = {}
) {
    var namaPenerima   by rememberSaveable { mutableStateOf("") }
    var noTelepon      by rememberSaveable { mutableStateOf("") }
    var kota           by rememberSaveable { mutableStateOf("") }
    var kodePos        by rememberSaveable { mutableStateOf("") }
    var alamatLengkap  by rememberSaveable { mutableStateOf("") }
    var labelAlamat    by rememberSaveable { mutableStateOf("RUMAH") }
    var isAlamatUtama  by rememberSaveable { mutableStateOf(false) }
    val listState       = rememberLazyListState()
    val labelOptions = listOf("RUMAH", "KANTOR", "LAINNYA")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScentBlack)
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector        = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint               = ScentWhite,
                        modifier           = Modifier
                            .size(24.dp)
                            .clickable(onClick = onBack)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text  = "SHIPPING ADDRESS",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 14.sp,
                            letterSpacing = 2.sp
                        ),
                        color = ScentWhite
                    )
                }
            }
            item(key = "header") {
                Column(
                    modifier = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        top    = 8.dp,
                        bottom = 28.dp
                    )
                ) {
                    Text(
                        text  = "INFORMASI PENGIRIMAN",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 26.sp,
                            lineHeight = 32.sp
                        ),
                        color = ScentWhite
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text  = "Masukkan detail alamat untuk pengiriman pesanan Anda.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color      = ScentTextMuted,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
            item(key = "nama") {
                AddressFormField(
                    label       = "NAMA PENERIMA",
                    value       = namaPenerima,
                    onChange    = { namaPenerima = it },
                    placeholder = "Contoh: Adrian Wijaya",
                    modifier    = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
            item(key = "telepon") {
                AddressFormField(
                    label           = "NOMOR TELEPON",
                    value           = noTelepon,
                    onChange        = { noTelepon = it },
                    placeholder     = "0812 3456 7890",
                    keyboardType    = KeyboardType.Phone,
                    modifier        = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
            item(key = "kota_kodepos") {
                Row(
                    modifier              = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AddressFormField(
                        label       = "KOTA / KECAMATAN",
                        value       = kota,
                        onChange    = { kota = it },
                        placeholder = "Bandung",
                        modifier    = Modifier.weight(1.5f)
                    )
                    AddressFormField(
                        label       = "KODE POS",
                        value       = kodePos,
                        onChange    = { kodePos = it },
                        placeholder = "40123",
                        keyboardType = KeyboardType.Number,
                        modifier    = Modifier.weight(1f)
                    )
                }
            }

            // ── Alamat Lengkap ────────────────────────────────────────────────
            item(key = "alamat") {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text  = "ALAMAT LENGKAP",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 10.sp,
                            letterSpacing = 1.5.sp,
                            color         = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, ScentDivider, RoundedCornerShape(8.dp))
                            .padding(14.dp)
                    ) {
                        BasicTextField(
                            value         = alamatLengkap,
                            onValueChange = { alamatLengkap = it },
                            textStyle     = MaterialTheme.typography.bodyMedium.copy(
                                color = ScentWhite
                            ),
                            cursorBrush   = SolidColor(ScentGold),
                            minLines      = 3,
                            modifier      = Modifier.fillMaxWidth(),
                            decorationBox = { inner ->
                                if (alamatLengkap.isEmpty()) {
                                    Text(
                                        text  = "Nama jalan, nomor rumah, blok, atau unit apartemen",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color      = ScentTextMuted,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                                inner()
                            }
                        )
                    }
                }
            }
            item(key = "label") {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text  = "LABEL ALAMAT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 10.sp,
                            letterSpacing = 1.5.sp,
                            color         = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        labelOptions.forEach { option ->
                            val isSelected = option == labelAlamat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) ScentWhite else Color.Transparent
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) ScentWhite else ScentDivider,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { labelAlamat = option }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text  = option,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize      = 10.sp,
                                        letterSpacing = 1.5.sp,
                                        fontWeight    = FontWeight.Bold,
                                        color         = if (isSelected) ScentBlack else ScentTextMuted
                                    )
                                )
                            }
                        }
                    }
                }
            }
            item(key = "utama") {
                Row(
                    modifier          = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .clickable { isAlamatUtama = !isAlamatUtama },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked         = isAlamatUtama,
                        onCheckedChange = { isAlamatUtama = it },
                        colors          = CheckboxDefaults.colors(
                            checkedColor        = ScentWhite,
                            uncheckedColor      = ScentTextMuted,
                            checkmarkColor      = ScentBlack
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text  = "Jadikan Alamat Utama",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ScentWhite
                        )
                    )
                }
            }
        }
        Box(
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
                    .clickable { onBack() }
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "SIMPAN ALAMAT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 12.sp,
                        letterSpacing = 2.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = ScentBlack
                    )
                )
            }
        }
    }
}

@Composable
private fun AddressFormField(
    label        : String,
    value        : String,
    onChange     : (String) -> Unit,
    placeholder  : String        = "",
    keyboardType : KeyboardType  = KeyboardType.Text,
    modifier     : Modifier      = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize      = 10.sp,
                letterSpacing = 1.5.sp,
                color         = ScentTextLabel
            )
        )
        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, ScentDivider, RoundedCornerShape(8.dp))
                .padding(horizontal = 14.dp, vertical = 14.dp)
        ) {
            BasicTextField(
                value           = value,
                onValueChange   = onChange,
                textStyle       = MaterialTheme.typography.bodyMedium.copy(
                    color    = ScentWhite,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                cursorBrush     = SolidColor(ScentGold),
                singleLine      = true,
                modifier        = Modifier.fillMaxWidth(),
                decorationBox   = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text  = placeholder,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color    = ScentTextMuted,
                                fontSize = 16.sp
                            )
                        )
                    }
                    inner()
                }
            )
        }
    }
}