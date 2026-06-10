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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.ui.theme.*

@Composable
fun AccountDetailScreen(
    onBack : () -> Unit = {}
) {
    var name           by rememberSaveable { mutableStateOf("Julian Alexander") }
    var email          by rememberSaveable { mutableStateOf("julian.alex@atelier.com") }
    var password       by rememberSaveable { mutableStateOf("password123") }
    var showPassword   by rememberSaveable { mutableStateOf(false) }
    val listState       = rememberLazyListState()

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
                        text  = "Detail Akun",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 20.sp
                        ),
                        color = ScentWhite
                    )
                }
            }
            item(key = "avatar") {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ScentSearchBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Person,
                                contentDescription = null,
                                tint               = ScentTextMuted,
                                modifier           = Modifier.size(56.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ScentWhite)
                                .align(Alignment.BottomEnd)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector        = Icons.Default.Edit,
                                contentDescription = "Ganti foto",
                                tint               = ScentBlack,
                                modifier           = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text  = "CHANGE PHOTO",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 10.sp,
                            letterSpacing = 2.sp,
                            color         = ScentTextMuted
                        )
                    )
                }
            }
            item(key = "nama") {
                AccountFormField(
                    label    = "NAMA LENGKAP",
                    value    = name,
                    onChange = { name = it },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                HorizontalDivider(
                    color     = ScentDivider,
                    thickness = 0.5.dp,
                    modifier  = Modifier.padding(horizontal = 20.dp)
                )
            }

            item(key = "email") {
                AccountFormField(
                    label    = "EMAIL",
                    value    = email,
                    onChange = { email = it },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                HorizontalDivider(
                    color     = ScentDivider,
                    thickness = 0.5.dp,
                    modifier  = Modifier.padding(horizontal = 20.dp)
                )
            }

            item(key = "password") {
                PasswordFormField(
                    label        = "PASSWORD",
                    value        = password,
                    onChange     = { password = it },
                    showPassword = showPassword,
                    onToggle     = { showPassword = !showPassword },
                    modifier     = Modifier.padding(horizontal = 20.dp)
                )
                HorizontalDivider(
                    color     = ScentDivider,
                    thickness = 0.5.dp,
                    modifier  = Modifier.padding(horizontal = 20.dp)
                )
            }
            item(key = "security") {
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(ScentBlack)
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector        = Icons.Default.Security,
                        contentDescription = null,
                        tint               = ScentTextMuted,
                        modifier           = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text  = "Keamanan Akun",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = ScentWhite
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text  = "Informasi pribadi Anda dienkripsi dengan standar industri atelier yang ketat.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color      = ScentTextMuted,
                                lineHeight = 18.sp
                            )
                        )
                    }
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
                    text  = "SIMPAN PERUBAHAN",
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
private fun AccountFormField(
    label    : String,
    value    : String,
    onChange : (String) -> Unit,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 16.dp)) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize      = 10.sp,
                letterSpacing = 1.5.sp,
                color         = ScentTextLabel
            )
        )
        Spacer(Modifier.height(8.dp))
        BasicTextField(
            value         = value,
            onValueChange = onChange,
            textStyle     = MaterialTheme.typography.titleMedium.copy(
                color      = ScentWhite,
                fontSize   = 18.sp,
                fontWeight = FontWeight.Normal
            ),
            cursorBrush   = SolidColor(ScentGold),
            modifier      = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PasswordFormField(
    label        : String,
    value        : String,
    onChange     : (String) -> Unit,
    showPassword : Boolean,
    onToggle     : () -> Unit,
    modifier     : Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 16.dp)) {
        Text(
            text  = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize      = 10.sp,
                letterSpacing = 1.5.sp,
                color         = ScentTextLabel
            )
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value               = value,
                onValueChange       = onChange,
                textStyle           = MaterialTheme.typography.titleMedium.copy(
                    color      = ScentWhite,
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Normal
                ),
                visualTransformation = if (showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                cursorBrush          = SolidColor(ScentGold),
                modifier             = Modifier.weight(1f)
            )
            Icon(
                imageVector        = if (showPassword) Icons.Default.VisibilityOff
                else Icons.Default.Visibility,
                contentDescription = "Toggle password",
                tint               = ScentTextMuted,
                modifier           = Modifier
                    .size(22.dp)
                    .clickable(onClick = onToggle)
            )
        }
    }
}