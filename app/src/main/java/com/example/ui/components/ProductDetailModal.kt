package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CoffeeProduct
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailModal(
    product: CoffeeProduct?,
    isWishlisted: Boolean,
    onToggleWishlist: () -> Unit,
    onClose: () -> Unit,
    onAddToCart: (CoffeeProduct, String, String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (product == null) return

    var selectedWeight by remember { mutableStateOf("250g") }
    var selectedGrind by remember { mutableStateOf("Whole Bean") }
    var quantity by remember { mutableIntStateOf(1) }

    val weights = listOf("250g", "500g", "1kg")
    val grinds = listOf("Whole Bean", "Fine Espresso", "Medium Pour-Over", "Coarse French Press")

    val unitPrice = when (selectedWeight) {
        "500g" -> (product.price * 1.85).toInt()
        "1kg" -> (product.price * 3.4).toInt()
        else -> product.price
    }
    val totalPrice = unitPrice * quantity

    ModalBottomSheet(
        onDismissRequest = onClose,
        containerColor = AuroraSurface,
        dragHandle = {
            Surface(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(44.dp)
                    .height(4.dp),
                shape = RoundedCornerShape(2.dp),
                color = AuroraBorder
            ) {}
        },
        modifier = modifier.testTag("product_detail_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Close & Wishlist Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = AuroraMuted
                    )
                }

                IconButton(onClick = onToggleWishlist) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) AuroraAccentRed else AuroraCream
                    )
                }
            }

            // Hero Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AuroraSurfaceVariant)
            ) {
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AuroraBackground.copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = product.tag.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuroraGold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AuroraCream
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.origin,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuroraMuted
                    )
                }

                Text(
                    text = "₹$totalPrice",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AuroraGold
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Rating & Technical Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AuroraGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${product.rating} (${product.reviewsCount} master cupping reviews)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = AuroraCream
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Specs Row (Elevation, Process, Roast)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailSpecPill("ROAST", product.roastLevel, Modifier.weight(1f))
                DetailSpecPill("ELEVATION", product.elevation, Modifier.weight(1f))
                DetailSpecPill("PROCESS", product.process, Modifier.weight(1.3f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = AuroraCream.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Flavor Notes Tags
            Text(
                text = "TASTING NOTES",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                product.notes.forEach { note ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AuroraSurfaceVariant,
                        border = BorderStroke(1.dp, AuroraBorder)
                    ) {
                        Text(
                            text = note,
                            style = MaterialTheme.typography.labelSmall,
                            color = AuroraAmber,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Weight Selector
            Text(
                text = "PACKAGE WEIGHT",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                weights.forEach { w ->
                    val isSel = selectedWeight == w
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSel) AuroraGold else AuroraCard,
                        border = BorderStroke(1.dp, if (isSel) AuroraGold else AuroraBorder),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedWeight = w }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = w,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSel) AuroraBackground else AuroraCream
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Grind Selector
            Text(
                text = "GRIND PROFILE",
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(grinds) { g ->
                    val isSel = selectedGrind == g
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSel) AuroraGold else AuroraCard,
                        border = BorderStroke(1.dp, if (isSel) AuroraGold else AuroraBorder),
                        modifier = Modifier.clickable { selectedGrind = g }
                    ) {
                        Text(
                            text = g,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSel) AuroraBackground else AuroraCream,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quantity & Add to Cart Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Stepper
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(AuroraCard, RoundedCornerShape(14.dp))
                        .border(1.dp, AuroraBorder, RoundedCornerShape(14.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = AuroraMuted)
                    }

                    Text(
                        text = "$quantity",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = AuroraCream,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = AuroraGold)
                    }
                }

                // Add to Cart Button
                Button(
                    onClick = {
                        onAddToCart(product, selectedWeight, selectedGrind, quantity)
                        onClose()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuroraGold,
                        contentColor = AuroraBackground
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("detail_add_to_cart_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ADD TO CART • ₹$totalPrice",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun DetailSpecPill(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = AuroraCard,
        border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    letterSpacing = 1.sp
                ),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AuroraCream,
                maxLines = 1
            )
        }
    }
}
