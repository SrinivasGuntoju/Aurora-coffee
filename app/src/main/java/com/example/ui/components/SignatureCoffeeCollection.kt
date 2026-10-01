package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CoffeeProduct
import com.example.data.repository.CoffeeDataRepository
import com.example.ui.theme.*

@Composable
fun SignatureCoffeeCollection(
    wishlistIds: Set<String>,
    onToggleWishlist: (String) -> Unit,
    onProductClick: (CoffeeProduct) -> Unit,
    onQuickAddToCart: (CoffeeProduct) -> Unit,
    modifier: Modifier = Modifier
) {
    val products = CoffeeDataRepository.signatureProducts

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("signature_coffee_collection_section")
    ) {
        // Section Header
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "SIGNATURE RESERVE",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.5.sp),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "MASTER ROAST COLLECTION",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = AuroraCream
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Four uncompromising expressions of single-origin and micro-lot roasting. Each lot is cupped, certified, and sealed with degassing valves.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraMuted
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Product Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            products.forEach { product ->
                SignatureProductCard(
                    product = product,
                    isWishlisted = wishlistIds.contains(product.id),
                    onToggleWishlist = { onToggleWishlist(product.id) },
                    onClick = { onProductClick(product) },
                    onQuickAddToCart = { onQuickAddToCart(product) }
                )
            }
        }
    }
}

@Composable
fun SignatureProductCard(
    product: CoffeeProduct,
    isWishlisted: Boolean,
    onToggleWishlist: () -> Unit,
    onClick: () -> Unit,
    onQuickAddToCart: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val tiltZ by animateFloatAsState(
        targetValue = if (isPressed) -2f else 0f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "cardTilt"
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "cardScale"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraCard),
        border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.65f)),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationZ = tiltZ
                scaleX = scale
                scaleY = scale
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            }
            .testTag("product_card_${product.id}")
    ) {
        Column {
            // Image and Tag banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Atmospheric Gradient Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    AuroraBackground.copy(alpha = 0.4f),
                                    Color.Transparent,
                                    AuroraCard.copy(alpha = 0.9f),
                                    AuroraCard
                                )
                            )
                        )
                )

                // Category Tag
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AuroraBackground.copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = product.tag.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuroraGold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                // Wishlist Button
                IconButton(
                    onClick = onToggleWishlist,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(38.dp)
                        .background(AuroraBackground.copy(alpha = 0.75f), CircleShape)
                        .border(1.dp, AuroraBorder.copy(alpha = 0.5f), CircleShape)
                        .testTag("wishlist_button_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) AuroraAccentRed else AuroraCream,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Rating & Roast Indicator over bottom of image
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AuroraGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.rating} (${product.reviewsCount})",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = AuroraCream
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "•",
                        color = AuroraMuted
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${product.roastLevel} Roast",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuroraAmber
                    )
                }
            }

            // Card Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = AuroraCream
                        )
                        Text(
                            text = product.origin,
                            style = MaterialTheme.typography.bodySmall,
                            color = AuroraMuted
                        )
                    }

                    // Price Tag
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹${product.price}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = AuroraGold
                            )
                        )
                        Text(
                            text = "per 250g",
                            style = MaterialTheme.typography.labelSmall,
                            color = AuroraMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tasting Notes Tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    product.notes.take(3).forEach { note ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AuroraSurfaceVariant.copy(alpha = 0.7f),
                            border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.35f))
                        ) {
                            Text(
                                text = note,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    letterSpacing = 0.4.sp
                                ),
                                color = AuroraCream.copy(alpha = 0.85f),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onClick,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AuroraBorderLight),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AuroraCream),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "DETAILS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Button(
                        onClick = onQuickAddToCart,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AuroraGold,
                            contentColor = AuroraBackground
                        ),
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("quick_add_to_cart_${product.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ADD TO CART",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
