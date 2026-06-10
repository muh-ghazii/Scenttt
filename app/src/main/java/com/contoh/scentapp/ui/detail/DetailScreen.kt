package com.contoh.scentapp.ui.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.contoh.scentapp.data.model.Product
import com.contoh.scentapp.data.model.Review
import com.contoh.scentapp.data.model.SizeOption
import com.contoh.scentapp.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun DetailScreen(
    productId : Int,
    onBack    : () -> Unit,
    viewModel : DetailViewModel = viewModel(
        factory = DetailViewModel.DetailViewModelFactory(productId)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedSizeId by rememberSaveable { mutableStateOf("full") }

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScentBlack)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color    = ScentGold
                )
            }
            uiState.product == null -> {
                Text(
                    text     = uiState.errorMessage ?: "Produk tidak ditemukan",
                    modifier = Modifier.align(Alignment.Center),
                    color    = ScentTextMuted
                )
            }
            else -> {
                LazyColumn(
                    state          = listState,
                    modifier       = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 40.dp)
                ) {
                    item(key = "topbar") {
                        DetailTopBar(onBack = onBack)
                    }
                    item(key = "image") {
                        ProductImageSection(
                            product  = uiState.product!!,
                            modifier = Modifier.padding(
                                horizontal = 20.dp,
                                vertical   = 8.dp
                            )
                        )
                    }
                    item(key = "info") {
                        ProductInfoSection(
                            product  = uiState.product!!,
                            modifier = Modifier.padding(
                                horizontal = 20.dp,
                                vertical   = 16.dp
                            )
                        )
                    }
                    item(key = "size") {
                        SizeSelectorSection(
                            sizeOptions    = uiState.sizeOptions,
                            selectedSizeId = selectedSizeId,
                            onSizeSelected = { id ->
                                selectedSizeId = id
                                viewModel.onSizeSelected(id)
                            },
                            modifier       = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                    item(key = "cart_button") {
                        AddToCartButton(
                            onClick  = { viewModel.addToCart() },
                            modifier = Modifier.padding(
                                horizontal = 20.dp,
                                vertical   = 16.dp
                            )
                        )
                    }
                    item(key = "divider") {
                        HorizontalDivider(
                            color     = ScentDivider,
                            thickness = 0.5.dp,
                            modifier  = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                    item(key = "reviews_header") {
                        ReviewsHeader(
                            product  = uiState.product!!,
                            modifier = Modifier.padding(
                                horizontal = 20.dp,
                                vertical   = 20.dp
                            )
                        )
                    }
                    items(
                        items = uiState.reviews,
                        key   = { review -> "review_${review.id}" }
                    ) { review ->
                        ReviewCard(
                            review   = review,
                            modifier = Modifier.padding(
                                horizontal = 20.dp,
                                vertical   = 8.dp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailTopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector        = Icons.Default.ArrowBack,
            contentDescription = "Kembali",
            tint               = ScentWhite,
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
            color = ScentWhite
        )
        Icon(
            imageVector        = Icons.Default.ShoppingBag,
            contentDescription = "Keranjang",
            tint               = ScentWhite,
            modifier           = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun ProductImageSection(
    product  : Product,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(product.cardColor).copy(alpha = 0.85f),
                        Color(product.cardColor).copy(alpha = 0.5f),
                        ScentBlack
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(Color(0xFF3A3A3A))
            )
            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(12.dp)
                    .background(Color(0xFF2A2A2A))
            )
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF4A4A4A), Color(0xFF222222))
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(product.accentColor).copy(alpha = 0.3f),
                                Color(0xFF1A1A1A),
                                Color(product.accentColor).copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        width = 0.8.dp,
                        color = Color(product.accentColor).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(6.dp)
                    )
            ) {
                Column(
                    modifier            = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text      = product.brand,
                        style     = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 8.sp,
                            letterSpacing = 1.sp,
                            color         = Color(product.accentColor).copy(alpha = 0.9f)
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text      = product.name.uppercase(),
                        style     = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 7.sp,
                            letterSpacing = 0.5.sp,
                            color         = ScentWhite.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductInfoSection(
    product  : Product,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text  = product.collection,
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                color         = ScentTextLabel
            )
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text  = product.name.uppercase(),
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize   = 30.sp
            )
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text  = product.fullBrand,
            style = MaterialTheme.typography.bodyMedium.copy(
                color      = ScentTextMuted,
                fontWeight = FontWeight.Normal
            )
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text  = product.description,
            style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 22.sp,
                color      = ScentTextPrimary
            )
        )
    }
}

@Composable
private fun SizeSelectorSection(
    sizeOptions    : List<SizeOption>,
    selectedSizeId : String,
    onSizeSelected : (String) -> Unit,
    modifier       : Modifier = Modifier
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        sizeOptions.forEach { option ->
            val isSelected = option.id == selectedSizeId

            val borderColor by animateColorAsState(
                targetValue   = if (isSelected) ScentWhite else ScentDivider,
                animationSpec = tween(200),
                label         = "sizeBorder_${option.id}"
            )
            val bgColor by animateColorAsState(
                targetValue   = if (isSelected) ScentSearchBg else Color.Transparent,
                animationSpec = tween(200),
                label         = "sizeBg_${option.id}"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                    .clickable { onSizeSelected(option.id) }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text  = option.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 10.sp,
                            letterSpacing = 1.5.sp,
                            fontWeight    = FontWeight.Bold,
                            color         = if (isSelected) ScentWhite else ScentTextMuted
                        )
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text  = "${option.size} ${option.price}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isSelected) ScentTextPrimary else ScentTextLabel
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun AddToCartButton(
    onClick  : () -> Unit,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(ScentWhite)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text  = "TAMBAH KE KERANJANG",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize      = 12.sp,
                letterSpacing = 2.sp,
                fontWeight    = FontWeight.Bold,
                color         = ScentBlack
            )
        )
    }
}

@Composable
private fun ReviewsHeader(
    product  : Product,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text  = "Refleksi",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) { index ->
                Icon(
                    imageVector        = if (index < product.rating.roundToInt())
                        Icons.Filled.Star
                    else Icons.Outlined.StarOutline,
                    contentDescription = null,
                    tint               = ScentGold,
                    modifier           = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text  = "${product.rating} / ${product.reviewCount} ULASAN",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = ScentTextMuted
                )
            )
        }
        Spacer(Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(0.5.dp, ScentDivider, RoundedCornerShape(8.dp))
                .clickable { }
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text  = "TULIS ULASAN",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize      = 10.sp,
                    letterSpacing = 2.sp,
                    color         = ScentTextMuted
                )
            )
        }
    }
}
@Composable
private fun ReviewCard(
    review   : Review,
    modifier : Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ScentBlack)
            .padding(16.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(review.avatarColor)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text  = review.initials,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize   = 10.sp,
                            color      = ScentGold,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text  = review.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text  = review.badge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize      = 8.sp,
                            letterSpacing = 1.sp,
                            color         = ScentGold
                        )
                    )
                }
            }
            Text(
                text  = review.date,
                style = MaterialTheme.typography.bodySmall.copy(
                    color    = ScentTextMuted,
                    fontSize = 11.sp
                )
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text  = review.text,
            style = MaterialTheme.typography.bodyMedium.copy(
                lineHeight = 22.sp,
                color      = ScentTextPrimary
            )
        )
        if (review.imageCount > 0) {
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(review.imageCount) { i ->
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (i == 0) Color(0xFF1A2535) else Color(0xFF2A2A2A)
                            )
                    )
                }
            }
        }
    }
}