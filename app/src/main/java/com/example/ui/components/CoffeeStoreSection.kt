package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
import com.example.data.model.ProductCategory
import com.example.data.repository.CoffeeDataRepository
import com.example.ui.theme.*

@Composable
fun CoffeeStoreSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: ProductCategory?,
    onCategorySelect: (ProductCategory?) -> Unit,
    wishlistIds: Set<String>,
    onToggleWishlist: (String) -> Unit,
    onProductClick: (CoffeeProduct) -> Unit,
    onQuickAddToCart: (CoffeeProduct) -> Unit,
    modifier: Modifier = Modifier
) {
    val allProducts = CoffeeDataRepository.allProducts

    val filteredProducts = remember(searchQuery, selectedCategory) {
        allProducts.filter { product ->
            val matchesCategory = selectedCategory == null || product.category == selectedCategory
            val matchesQuery = searchQuery.isBlank() ||
                    product.name.contains(searchQuery, ignoreCase = true) ||
                    product.description.contains(searchQuery, ignoreCase = true) ||
                    product.origin.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("coffee_store_section")
    ) {
        // Section Header
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "AURORA EMPORIUM",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.5.sp),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "COFFEE & EQUIPMENT STORE",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = AuroraCream
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Discover award-winning whole beans, precision brewing gear, artisanal ceramics, and bespoke tasting chests.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraMuted
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    text = "Search roasts, origins, equipment...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuroraMuted
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = AuroraGold
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = AuroraMuted
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = AuroraCard,
                unfocusedContainerColor = AuroraCard,
                focusedBorderColor = AuroraGold,
                unfocusedBorderColor = AuroraBorder,
                focusedTextColor = AuroraCream,
                unfocusedTextColor = AuroraCream
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .testTag("store_search_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { onCategorySelect(null) },
                    label = { Text("ALL COLLECTIONS") },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AuroraGold,
                        selectedLabelColor = AuroraBackground,
                        containerColor = AuroraCard,
                        labelColor = AuroraCream
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (selectedCategory == null) AuroraGold else AuroraBorder
                    ),
                    modifier = Modifier.testTag("filter_all")
                )
            }
            items(ProductCategory.entries.toTypedArray()) { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(if (isSelected) null else category) },
                    label = { Text(category.displayName.uppercase()) },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AuroraGold,
                        selectedLabelColor = AuroraBackground,
                        containerColor = AuroraCard,
                        labelColor = AuroraCream
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) AuroraGold else AuroraBorder
                    ),
                    modifier = Modifier.testTag("filter_${category.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Results Grid / List
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No products match \"$searchQuery\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AuroraMuted
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                filteredProducts.forEach { product ->
                    StoreProductRowCard(
                        product = product,
                        isWishlisted = wishlistIds.contains(product.id),
                        onToggleWishlist = { onToggleWishlist(product.id) },
                        onClick = { onProductClick(product) },
                        onAddToCart = { onQuickAddToCart(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun StoreProductRowCard(
    product: CoffeeProduct,
    isWishlisted: Boolean,
    onToggleWishlist: () -> Unit,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraCard),
        border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("store_card_${product.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image Thumbnail
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AuroraSurfaceVariant)
            ) {
                Image(
                    painter = painterResource(id = product.imageResId),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Category pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = AuroraBackground.copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                ) {
                    Text(
                        text = product.category.displayName.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuroraGold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = AuroraCream,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onToggleWishlist,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) AuroraAccentRed else AuroraMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.origin,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = AuroraMuted
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = AuroraGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${product.rating}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AuroraCream
                        )
                    }

                    Text(
                        text = "₹${product.price}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AuroraGold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuroraGold,
                        contentColor = AuroraBackground
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ADD TO CART",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }
}
