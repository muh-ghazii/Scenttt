package com.contoh.scentapp.ui.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contoh.scentapp.data.model.LanguageOption
import com.contoh.scentapp.ui.theme.*

private val languageOptions = listOf(
    LanguageOption("id", "Bahasa Indonesia", "INDONESIAN"),
    LanguageOption("en", "English", "UNITED KINGDOM")
)

@Composable
fun LanguageScreen(
    onBack : () -> Unit = {}
) {
    var selectedLang by rememberSaveable { mutableStateOf("id") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScentBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                        text  = "LANGUAGE",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 14.sp,
                            letterSpacing = 3.sp
                        ),
                        color = ScentWhite
                    )
                }
                Text(
                    text  = "SCENT",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 6.sp,
                        fontSize      = 18.sp
                    ),
                    color = ScentWhite
                )
            }

            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text  = "INTERFACE PREFERENCES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize      = 10.sp,
                        letterSpacing = 2.sp,
                        color         = ScentTextLabel
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text  = "Select your preferred language",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize   = 28.sp,
                        lineHeight = 36.sp
                    ),
                    color = ScentWhite
                )
            }

            Spacer(Modifier.height(32.dp))
            languageOptions.forEach { lang ->
                val isSelected = lang.id == selectedLang

                val bgColor by animateColorAsState(
                    targetValue   = if (isSelected) ScentSearchBg else Color(0xFF161616),
                    animationSpec = tween(200),
                    label         = "langBg_${lang.id}"
                )
                val borderColor by animateColorAsState(
                    targetValue   = if (isSelected) ScentWhite else ScentDivider,
                    animationSpec = tween(200),
                    label         = "langBorder_${lang.id}"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                        .clickable { selectedLang = lang.id }
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text  = lang.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 18.sp
                            ),
                            color = ScentWhite
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text  = lang.subtitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize      = 10.sp,
                                letterSpacing = 1.5.sp,
                                color         = ScentTextMuted
                            )
                        )
                    }
                    RadioButton(
                        selected = isSelected,
                        onClick  = { selectedLang = lang.id },
                        colors   = RadioButtonDefaults.colors(
                            selectedColor   = ScentWhite,
                            unselectedColor = ScentTextMuted
                        )
                    )
                }
            }
        }
    }
}