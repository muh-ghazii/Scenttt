package com.contoh.scentapp.ui.sales

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.contoh.scentapp.data.model.SalesProduct
import com.contoh.scentapp.ui.theme.*
import java.io.File

private val aromaFamilies = listOf("Woody", "Floral", "Oriental", "Citrus", "Gourmand", "Aquatic")
private val sizeOptions   = listOf("30", "50", "100")
private val usageOptions  = listOf("SIANG", "MALAM", "KEDUANYA")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddProductScreen(
    onBack : () -> Unit = {},
    onSave : (SalesProduct) -> Unit = {}
) {
    val context = LocalContext.current

    // ── Form state ────────────────────────────────────────────────────────────
    var namaParfum      by rememberSaveable { mutableStateOf("") }
    var deskripsi       by rememberSaveable { mutableStateOf("") }
    var hargaPenuh      by rememberSaveable { mutableStateOf("") }
    var hargaDecant     by rememberSaveable { mutableStateOf("") }
    var isDecantAvail   by rememberSaveable { mutableStateOf(false) }
    var selectedAroma   by rememberSaveable { mutableStateOf("Woody") }
    var selectedUsage   by rememberSaveable { mutableStateOf("KEDUANYA") }
    var jumlahStok      by rememberSaveable { mutableStateOf("") }
    var selectedSize    by rememberSaveable { mutableStateOf("50") }
    var aromaChips      by rememberSaveable { mutableStateOf(listOf("OUD", "BERGAMOT")) }
    var newChipInput    by rememberSaveable { mutableStateOf("") }
    var showAromaMenu   by rememberSaveable { mutableStateOf(false) }
    var showChipInput   by rememberSaveable { mutableStateOf(false) }
    var showPhotoDialog by rememberSaveable { mutableStateOf(false) }

    // ✅ FIX 1: imageUri pakai rememberSaveable dengan String? agar survive recomposition
    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }

    // ✅ FIX 2: cameraUri pakai rememberSaveable dengan String? bukan Uri?
    // Uri tidak bisa di-serialize, simpan sebagai String lalu convert saat dipakai
    var cameraUriString by rememberSaveable { mutableStateOf<String?>(null) }

    // ✅ FIX 3: Gallery launcher — tidak ada perubahan, sudah benar
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri = it.toString() }
    }

    // ✅ FIX 4: Camera launcher — pakai cameraUriString bukan cameraUri langsung
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && cameraUriString != null) {
            imageUri = cameraUriString
        }
    }

    // ✅ FIX 5: launchCamera dipindah ke fungsi biasa (bukan nested lambda di composable)
    // File.createTempFile aman dipanggil di sini karena di dalam lambda onClick
    fun launchCamera() {
        try {
            val photoFile = File.createTempFile("scent_photo_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                photoFile
            )
            cameraUriString = uri.toString()
            cameraLauncher.launch(uri)
        } catch (e: Exception) {
            // FileProvider belum dikonfigurasi di AndroidManifest — tangkap error
            // agar tidak force close, dan hanya skip kamera
            e.printStackTrace()
        }
    }

    val listState = rememberLazyListState()
    val canSave   = namaParfum.isNotBlank() && hargaPenuh.isNotBlank()

    // ── Photo source dialog ───────────────────────────────────────────────────
    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            containerColor   = MaterialTheme.colorScheme.surfaceVariant,
            title = {
                Text(
                    text  = "Pilih Sumber Foto",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .clickable {
                                showPhotoDialog = false
                                galleryLauncher.launch("image/*")
                            }
                            .padding(16.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.PhotoLibrary, null,
                            tint     = ScentGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                "Galeri",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color      = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                "Pilih foto dari galeri",
                                style = MaterialTheme.typography.bodySmall.copy(color = ScentTextMuted)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .clickable {
                                showPhotoDialog = false
                                launchCamera()
                            }
                            .padding(16.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.CameraAlt, null,
                            tint     = ScentGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                "Kamera",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color      = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                "Ambil foto baru",
                                style = MaterialTheme.typography.bodySmall.copy(color = ScentTextMuted)
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showPhotoDialog = false }) {
                    Text("Batal", color = ScentTextMuted)
                }
            }
        )
    }

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
                        Icons.AutoMirrored.Filled.ArrowBack, "Kembali",
                        tint     = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp).clickable(onClick = onBack)
                    )
                    Text(
                        "SCENT",
                        style = MaterialTheme.typography.titleLarge.copy(
                            letterSpacing = 6.sp, fontSize = 18.sp, fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(Icons.Default.Help, null, tint = ScentTextMuted, modifier = Modifier.size(22.dp))
                }
            }

            item(key = "header") {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        "MANAJEMEN INVENTARIS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 2.sp, color = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Tambah Produk Baru",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold, fontSize = 26.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            item(key = "upload") {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            if (imageUri != null) ScentGold.copy(alpha = 0.5f) else ScentDivider,
                            RoundedCornerShape(12.dp)
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showPhotoDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model            = imageUri,
                            contentDescription = "Foto produk",
                            contentScale     = ContentScale.Crop,
                            modifier         = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment     = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit, null,
                                    tint     = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    "GANTI",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize      = 9.sp,
                                        letterSpacing = 1.sp,
                                        color         = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.AddAPhoto, "Upload",
                                tint     = ScentTextMuted,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                "UNGGAH GAMBAR PRODUK",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp, letterSpacing = 2.sp, color = ScentTextMuted
                                )
                            )
                            Text(
                                "Ketuk untuk memilih dari galeri atau kamera",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = ScentTextLabel, fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            item(key = "nama") {
                ProductFormField(
                    label       = "NAMA PARFUM",
                    value       = namaParfum,
                    onChange    = { namaParfum = it },
                    placeholder = "contoh: Noir Éphémère",
                    modifier    = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item(key = "deskripsi") {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        "DESKRIPSI PARFUM",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
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
                            value          = deskripsi,
                            onValueChange  = { deskripsi = it },
                            textStyle      = MaterialTheme.typography.bodyMedium.copy(
                                color      = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 22.sp
                            ),
                            cursorBrush = SolidColor(ScentGold),
                            minLines    = 4,
                            modifier    = Modifier.fillMaxWidth(),
                            decorationBox = { inner ->
                                if (deskripsi.isEmpty()) Text(
                                    "Gambarkan karakter dan jiwa dari wewangian ini...",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = ScentTextMuted, lineHeight = 22.sp
                                    )
                                )
                                inner()
                            }
                        )
                    }
                }
            }

            item(key = "penggunaan") {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        "COCOK DIGUNAKAN",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        usageOptions.forEach { usage ->
                            val isSelected = usage == selectedUsage
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) ScentGold.copy(alpha = 0.15f)
                                        else Color.Transparent
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) ScentGold else ScentDivider,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedUsage = usage }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    usage,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize   = 10.sp,
                                        letterSpacing = 1.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color      = if (isSelected) ScentGold else ScentTextMuted
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item(key = "harga_penuh") {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        "HARGA PENUH (RP)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Rp",
                                style = MaterialTheme.typography.bodyMedium.copy(color = ScentTextMuted)
                            )
                            Spacer(Modifier.width(8.dp))
                            BasicTextField(
                                value          = hargaPenuh,
                                onValueChange  = { hargaPenuh = it },
                                textStyle      = MaterialTheme.typography.bodyMedium.copy(
                                    color    = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 16.sp
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                cursorBrush    = SolidColor(ScentGold),
                                singleLine     = true,
                                modifier       = Modifier.fillMaxWidth(),
                                decorationBox  = { inner ->
                                    if (hargaPenuh.isEmpty()) Text(
                                        "250.000",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = ScentTextMuted, fontSize = 16.sp
                                        )
                                    )
                                    inner()
                                }
                            )
                        }
                    }
                }
            }

            item(key = "decant_toggle") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            "TERSEDIA SEBAGAI DECANT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
                            )
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "Jual dalam ukuran kecil (5ml, 10ml, dst)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = ScentTextMuted, fontSize = 11.sp
                            )
                        )
                    }
                    Switch(
                        checked         = isDecantAvail,
                        onCheckedChange = { isDecantAvail = it },
                        colors          = SwitchDefaults.colors(
                            checkedThumbColor   = MaterialTheme.colorScheme.background,
                            checkedTrackColor   = ScentGold,
                            uncheckedThumbColor = ScentTextMuted,
                            uncheckedTrackColor = ScentDivider
                        )
                    )
                }
            }

            if (isDecantAvail) {
                item(key = "harga_decant") {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
                        Text(
                            "HARGA DECANT (RP)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
                            )
                        )
                        Spacer(Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, ScentGold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 14.dp, vertical = 14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Rp",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = ScentTextMuted)
                                )
                                Spacer(Modifier.width(8.dp))
                                BasicTextField(
                                    value          = hargaDecant,
                                    onValueChange  = { hargaDecant = it },
                                    textStyle      = MaterialTheme.typography.bodyMedium.copy(
                                        color    = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 16.sp
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    cursorBrush    = SolidColor(ScentGold),
                                    singleLine     = true,
                                    modifier       = Modifier.fillMaxWidth(),
                                    decorationBox  = { inner ->
                                        if (hargaDecant.isEmpty()) Text(
                                            "25.000",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = ScentTextMuted, fontSize = 16.sp
                                            )
                                        )
                                        inner()
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item(key = "wangi") {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        "WANGI (OLFACTORY FAMILY)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    Box {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, ScentDivider, RoundedCornerShape(8.dp))
                                .clickable { showAromaMenu = !showAromaMenu }
                                .padding(horizontal = 14.dp, vertical = 14.dp)
                        ) {
                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Text(
                                    selectedAroma,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color    = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 16.sp
                                    )
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown, null,
                                    tint     = ScentTextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded        = showAromaMenu,
                            onDismissRequest = { showAromaMenu = false },
                            modifier        = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            aromaFamilies.forEach { aroma ->
                                DropdownMenuItem(
                                    text    = {
                                        Text(
                                            aroma,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onBackground
                                            )
                                        )
                                    },
                                    onClick = { selectedAroma = aroma; showAromaMenu = false }
                                )
                            }
                        }
                    }
                }
            }

            item(key = "notes") {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, ScentDivider, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        "CATATAN AROMA (NOTES)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Tambahkan bahan-bahan aroma utama",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = ScentTextMuted, fontSize = 11.sp
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement   = Arrangement.spacedBy(8.dp)
                    ) {
                        aromaChips.forEach { chip ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment     = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        chip,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize      = 10.sp,
                                            letterSpacing = 1.sp,
                                            color         = MaterialTheme.colorScheme.onBackground
                                        )
                                    )
                                    Icon(
                                        Icons.Default.Close, "Hapus",
                                        tint     = ScentTextMuted,
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clickable {
                                                aromaChips = aromaChips.filter { it != chip }
                                            }
                                    )
                                }
                            }
                        }
                        if (showChipInput) {
                            BasicTextField(
                                value          = newChipInput,
                                onValueChange  = { newChipInput = it },
                                textStyle      = MaterialTheme.typography.labelSmall.copy(
                                    color    = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 12.sp
                                ),
                                cursorBrush = SolidColor(ScentGold),
                                singleLine  = true,
                                modifier    = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .border(1.dp, ScentGold, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .width(100.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, ScentDivider, RoundedCornerShape(6.dp))
                                .clickable {
                                    if (showChipInput && newChipInput.isNotBlank()) {
                                        aromaChips   = aromaChips + newChipInput.uppercase()
                                        newChipInput = ""
                                        showChipInput = false
                                    } else {
                                        showChipInput = !showChipInput
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                if (showChipInput && newChipInput.isNotBlank()) "✓ SIMPAN"
                                else "+ TAMBAH NOTE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize      = 10.sp,
                                    letterSpacing = 1.sp,
                                    color         = ScentTextMuted
                                )
                            )
                        }
                    }
                }
            }

            item(key = "stok") {
                ProductFormField(
                    label        = "JUMLAH STOK",
                    value        = jumlahStok,
                    onChange     = { jumlahStok = it },
                    placeholder  = "48",
                    keyboardType = KeyboardType.Number,
                    modifier     = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item(key = "ukuran") {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        "UKURAN BOTOL (ML)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
                        )
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        sizeOptions.forEach { size ->
                            val isSelected = size == selectedSize
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.onBackground
                                        else Color.Transparent
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.onBackground
                                        else ScentDivider,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedSize = size }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    size,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize      = 12.sp,
                                        letterSpacing = 1.sp,
                                        fontWeight    = FontWeight.Bold,
                                        color         = if (isSelected) MaterialTheme.colorScheme.background
                                        else ScentTextMuted
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Tombol Submit ─────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (canSave) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable(enabled = canSave) {
                        val hargaBersih = hargaPenuh
                            .replace(".", "")
                            .replace(",", "")
                            .trim()
                        val hargaInt = hargaBersih.toIntOrNull() ?: 0
                        val stokInt  = jumlahStok.toIntOrNull() ?: 0

                        val newProduct = SalesProduct(
                            id          = System.currentTimeMillis().toInt(),
                            name        = namaParfum.uppercase().trim(),
                            aromaFamily = selectedAroma.uppercase(),
                            volume      = "${selectedSize}ML",
                            stockStatus = when {
                                stokInt == 0  -> "HABIS"
                                stokInt <= 5  -> "STOK MENIPIS"
                                else          -> "TERSEDIA"
                            },
                            price       = hargaInt,
                            stock       = stokInt
                        )
                        onSave(newProduct)
                    }
                    .padding(vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "TAMBAH PRODUK",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 12.sp,
                        letterSpacing = 2.sp,
                        fontWeight    = FontWeight.Bold,
                        color         = if (canSave) MaterialTheme.colorScheme.background
                        else ScentTextMuted
                    )
                )
            }
        }
    }
}
@Composable
private fun ProductFormField(
    label        : String,
    value        : String,
    onChange     : (String) -> Unit,
    placeholder  : String       = "",
    keyboardType : KeyboardType = KeyboardType.Text,
    modifier     : Modifier     = Modifier
) {
    Column(modifier = modifier) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp, letterSpacing = 1.5.sp, color = ScentTextLabel
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
                value          = value,
                onValueChange  = onChange,
                textStyle      = MaterialTheme.typography.bodyMedium.copy(
                    color    = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                cursorBrush    = SolidColor(ScentGold),
                singleLine     = true,
                modifier       = Modifier.fillMaxWidth(),
                decorationBox  = { inner ->
                    if (value.isEmpty()) Text(
                        placeholder,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ScentTextMuted, fontSize = 16.sp
                        )
                    )
                    inner()
                }
            )
        }
    }
}