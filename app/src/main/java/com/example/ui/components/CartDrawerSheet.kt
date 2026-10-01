package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartDrawerSheet(
    isOpen: Boolean,
    items: List<CartItem>,
    isDiscountApplied: Boolean,
    discountPercent: Int,
    discountCode: String,
    onClose: () -> Unit,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onApplyPromoCode: (String) -> Boolean,
    onRemovePromoCode: () -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    var promoInput by remember { mutableStateOf("") }
    var promoError by remember { mutableStateOf(false) }

    val rawSubtotal = items.sumOf { it.totalPrice }
    val discountAmount = if (isDiscountApplied) (rawSubtotal * discountPercent / 100) else 0
    val finalTotal = (rawSubtotal - discountAmount).coerceAtLeast(0)
    val freeShippingThreshold = 1000
    val freeShippingProgress = (rawSubtotal.toFloat() / freeShippingThreshold).coerceIn(0f, 1f)

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
        modifier = modifier.testTag("cart_drawer_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = AuroraGold,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "YOUR SELECTIONS",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AuroraCream
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = CircleShape,
                        color = AuroraGold,
                        modifier = Modifier.size(22.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${items.sumOf { it.quantity }}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AuroraBackground
                            )
                        }
                    }
                }

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close cart",
                        tint = AuroraMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Complimentary Shipping Progress Bar
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AuroraCard,
                border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = AuroraCaramel,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (rawSubtotal >= freeShippingThreshold)
                                    "Complimentary Gold Tier Delivery Unlocked"
                                else
                                    "Add ₹${freeShippingThreshold - rawSubtotal} more for Free Priority Shipping",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                color = if (rawSubtotal >= freeShippingThreshold) AuroraGold else AuroraCream
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(AuroraBorder, RoundedCornerShape(2.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = freeShippingProgress)
                                .fillMaxHeight()
                                .background(AuroraGold, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Empty Cart State or List
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = AuroraBorderLight,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Your tasting cart is empty",
                            style = MaterialTheme.typography.titleMedium,
                            color = AuroraCream
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Explore our master roasts and single origins",
                            style = MaterialTheme.typography.bodySmall,
                            color = AuroraMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        CartItemRow(
                            item = item,
                            onUpdateQuantity = { delta -> onUpdateQuantity(item.id, delta) },
                            onRemove = { onRemoveItem(item.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Promo Code Input Box
            if (items.isNotEmpty()) {
                if (isDiscountApplied) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AuroraSuccess.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, AuroraSuccess.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = AuroraSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$discountCode applied ($discountPercent% OFF)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = AuroraSuccess
                                )
                            }
                            Text(
                                text = "REMOVE",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AuroraCream,
                                modifier = Modifier.clickable { onRemovePromoCode() }
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = promoInput,
                            onValueChange = {
                                promoInput = it
                                promoError = false
                            },
                            placeholder = { Text("Promo Code (try AURORA15)", fontSize = 11.sp, color = AuroraMuted) },
                            isError = promoError,
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = AuroraCard,
                                unfocusedContainerColor = AuroraCard,
                                focusedBorderColor = AuroraGold,
                                unfocusedBorderColor = AuroraBorder,
                                focusedTextColor = AuroraCream,
                                unfocusedTextColor = AuroraCream
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("promo_code_input")
                        )

                        Button(
                            onClick = {
                                val success = onApplyPromoCode(promoInput)
                                if (!success) promoError = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AuroraCardElevated,
                                contentColor = AuroraGold
                            ),
                            border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.5f)),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("APPLY", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Price Summary Breakdown
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", style = MaterialTheme.typography.bodySmall, color = AuroraMuted)
                        Text("₹$rawSubtotal", style = MaterialTheme.typography.bodySmall, color = AuroraCream)
                    }

                    if (isDiscountApplied) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Promotion ($discountCode)", style = MaterialTheme.typography.bodySmall, color = AuroraSuccess)
                            Text("-₹$discountAmount", style = MaterialTheme.typography.bodySmall, color = AuroraSuccess)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Estimated Shipping", style = MaterialTheme.typography.bodySmall, color = AuroraMuted)
                        Text(
                            if (rawSubtotal >= freeShippingThreshold) "FREE" else "₹99",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (rawSubtotal >= freeShippingThreshold) AuroraGold else AuroraCream
                        )
                    }

                    Divider(
                        color = AuroraBorder,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Investment",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AuroraCream
                        )
                        Text(
                            text = "₹${if (rawSubtotal >= freeShippingThreshold) finalTotal else finalTotal + 99}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AuroraGold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Checkout Button
                Button(
                    onClick = onCheckout,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuroraGold,
                        contentColor = AuroraBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("cart_checkout_button")
                ) {
                    Text(
                        text = "PROCEED TO CONNOISSEUR CHECKOUT",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraCard),
        border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.product.imageResId),
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = AuroraCream,
                    maxLines = 1
                )
                Text(
                    text = "${item.weight} • ${item.grind}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = AuroraMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₹${item.totalPrice}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AuroraGold
                    )
                )
            }

            // Quantity stepper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(AuroraSurfaceVariant, RoundedCornerShape(8.dp))
                    .border(1.dp, AuroraBorder, RoundedCornerShape(8.dp))
            ) {
                IconButton(
                    onClick = { onUpdateQuantity(-1) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = AuroraMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = AuroraCream,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                IconButton(
                    onClick = { onUpdateQuantity(1) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = AuroraGold,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
