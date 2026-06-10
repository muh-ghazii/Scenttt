package com.contoh.scentapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.contoh.scentapp.MainActivity
import com.contoh.scentapp.ui.theme.*

@Composable
fun ProfileScreen(
    onBack           : () -> Unit = {},
    onDetailAkun     : () -> Unit = {},
    onAlamat         : () -> Unit = {},
    onRiwayatPesanan : () -> Unit = {},
    onBahasa         : () -> Unit = {},
    onPenjualan      : () -> Unit = {},
    onLogout         : () -> Unit = {},
    viewModel        : ProfileViewModel = viewModel(factory = ProfileViewModelFactory())
) {
    val uiState   by viewModel.uiState.collectAsStateWithLifecycle()
    val listState  = rememberLazyListState()
    val darkMode   = MainActivity.isDarkModeState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state          = listState,
            modifier       = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
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
                        tint               = MaterialTheme.colorScheme.onBackground,
                        modifier           = Modifier
                            .size(24.dp)
                            .clickable(onClick = onBack)
                    )
                    Text(
                        text  = "AKUN",
                        style = MaterialTheme.typography.titleLarge.copy(
                            letterSpacing = 4.sp,
                            fontSize      = 16.sp,
                            fontWeight    = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(
                        imageVector        = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Keluar",
                        tint               = MaterialTheme.colorScheme.onBackground,
                        modifier           = Modifier
                            .size(24.dp)
                            .clickable(onClick = onLogout)
                    )
                }
            }

            // ── Header ────────────────────────────────────────────────────────
            item(key = "header") {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (darkMode) ScentSearchBg else Color(0xFFE9ECEF)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Person,
                            contentDescription = null,
                            tint               = ScentTextMuted,
                            modifier           = Modifier.size(48.dp)
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text  = uiState.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize   = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text  = uiState.email,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = ScentTextMuted
                            )
                        )
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, ScentTextMuted, RoundedCornerShape(6.dp))
                                .clickable(onClick = onDetailAkun)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text  = "EDIT PROFIL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize      = 10.sp,
                                    letterSpacing = 2.sp,
                                    fontWeight    = FontWeight.Bold,
                                    color         = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    }
                }
            }

            item(key = "divider1") {
                HorizontalDivider(color = ScentDivider, thickness = 0.5.dp)
                Spacer(Modifier.height(24.dp))
            }

            // ── Informasi Pribadi ─────────────────────────────────────────────
            item(key = "section_personal") {
                Text(
                    text     = "INFORMASI PRIBADI",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    style    = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 10.sp,
                        letterSpacing = 2.sp,
                        color         = ScentTextLabel
                    )
                )
            }
            item(key = "detail_akun") {
                ProfileMenuItem(Icons.Default.Person, "Detail Akun", onDetailAkun)
                HorizontalDivider(
                    color    = ScentDivider,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item(key = "alamat") {
                ProfileMenuItem(Icons.Default.LocationOn, "Alamat Pengiriman", onAlamat)
                HorizontalDivider(
                    color    = ScentDivider,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item(key = "pesanan_saya") {
                ProfileMenuItem(Icons.AutoMirrored.Filled.ListAlt, "Pesanan Saya", onRiwayatPesanan)
                HorizontalDivider(
                    color    = ScentDivider,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // ── Preferensi Aplikasi ───────────────────────────────────────────
            item(key = "section_pref") {
                Spacer(Modifier.height(24.dp))
                Text(
                    text     = "PREFERENSI APLIKASI",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    style    = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 10.sp,
                        letterSpacing = 2.sp,
                        color         = ScentTextLabel
                    )
                )
            }
            item(key = "bahasa") {
                ProfileMenuItemWithSubtitle(
                    icon     = Icons.Default.Language,
                    label    = "Bahasa",
                    subtitle = uiState.language,
                    onClick  = onBahasa
                )
                HorizontalDivider(
                    color    = ScentDivider,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item(key = "dark_mode") {
                ProfileMenuItemWithToggle(
                    icon      = Icons.Default.DarkMode,
                    label     = "Mode Gelap",
                    isChecked = darkMode,
                    onToggle  = { MainActivity.isDarkModeState = !darkMode }
                )
                HorizontalDivider(
                    color    = ScentDivider,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item(key = "penjualan") {
                ProfileMenuItem(Icons.Default.Store, "Penjualan", onPenjualan)
                HorizontalDivider(
                    color    = ScentDivider,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // ── Hapus Akun ────────────────────────────────────────────────────
            item(key = "delete") {
                Spacer(Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, ScentDivider, RoundedCornerShape(10.dp))
                        .clickable { viewModel.showDeleteDialog() }
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text  = "HAPUS AKUN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 12.sp,
                            letterSpacing = 3.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = Color(0xFFCF6679)
                        )
                    )
                }
            }
        }

        // ── Dialog Hapus Akun ─────────────────────────────────────────────────
        if (uiState.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.hideDeleteDialog() },
                containerColor   = MaterialTheme.colorScheme.surface,
                title = {
                    Text(
                        "Hapus Akun",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                text = {
                    Text(
                        "Apakah kamu yakin ingin menghapus akun? Tindakan ini tidak dapat dibatalkan.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = ScentTextMuted)
                    )
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.confirmDeleteAccount() }) {
                        Text(
                            "HAPUS",
                            color = Color(0xFFCF6679),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight    = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.hideDeleteDialog() }) {
                        Text(
                            "BATAL",
                            color = ScentTextMuted,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight    = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            )
        }
    }
}

// ── Helper Composables ────────────────────────────────────────────────────────

@Composable
private fun ProfileMenuItem(
    icon    : ImageVector,
    label   : String,
    onClick : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = ScentTextMuted, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(16.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
        Icon(
            Icons.Default.ChevronRight,
            null,
            tint     = ScentTextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ProfileMenuItemWithSubtitle(
    icon     : ImageVector,
    label    : String,
    subtitle : String,
    onClick  : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = ScentTextMuted, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    label,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 10.sp,
                        letterSpacing = 1.sp,
                        color         = ScentTextMuted
                    )
                )
            }
        }
        Icon(
            Icons.Default.ChevronRight,
            null,
            tint     = ScentTextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ProfileMenuItemWithToggle(
    icon      : ImageVector,
    label     : String,
    isChecked : Boolean,
    onToggle  : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = ScentTextMuted, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(16.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
        Switch(
            checked         = isChecked,
            onCheckedChange = { onToggle() },
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = ScentBlack,
                checkedTrackColor   = ScentGold,
                uncheckedThumbColor = ScentTextMuted,
                uncheckedTrackColor = ScentSearchBg
            )
        )
    }
}