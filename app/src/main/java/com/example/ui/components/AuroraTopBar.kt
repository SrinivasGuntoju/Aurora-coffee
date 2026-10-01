package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AuroraTopBar(
    cartCount: Int,
    wishlistCount: Int,
    isAmbientAudioActive: Boolean,
    onToggleAmbientAudio: () -> Unit,
    onCartClick: () -> Unit,
    activeTab: String,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navTabs = listOf(
        "home" to "DISCOVER",
        "bean" to "3D BEAN",
        "journey" to "JOURNEY",
        "collection" to "ROASTS",
        "video" to "CINEMA",
        "lab" to "BREW LAB",
        "packaging" to "VESSEL",
        "store" to "STORE",
        "subscription" to "SUBSCRIPTION",
        "story" to "OUR STORY"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AuroraBackground.copy(alpha = 0.95f))
            .statusBarsPadding()
            .border(
                BorderStroke(
                    0.5.dp,
                    Brush.verticalGradient(
                        listOf(Color.Transparent, AuroraBorder.copy(alpha = 0.4f))
                    )
                )
            )
            .testTag("aurora_top_bar")
    ) {
        // Main Row: Brand Monogram + Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo & Monogram
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onTabClick("home") }
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AuroraCard,
                    border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.6f)),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "A",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            ),
                            color = AuroraGold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "AURORA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.5.sp
                        ),
                        color = AuroraCream
                    )
                    Text(
                        text = "COFFEE ROASTERS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp,
                            letterSpacing = 1.8.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = AuroraGold
                    )
                }
            }

            // Right Actions: Ambience Audio + Wishlist + Cart
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ambience Sound Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isAmbientAudioActive) AuroraGold.copy(alpha = 0.15f) else AuroraCard,
                    border = BorderStroke(
                        1.dp,
                        if (isAmbientAudioActive) AuroraGold else AuroraBorder.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.clickable { onToggleAmbientAudio() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isAmbientAudioActive) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                            contentDescription = "Atmosphere Audio",
                            tint = if (isAmbientAudioActive) AuroraGold else AuroraMuted,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAmbientAudioActive) "CAFE AMBIENCE" else "MUTE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = if (isAmbientAudioActive) AuroraGold else AuroraMuted
                        )
                    }
                }

                // Cart Button with Badge
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier
                        .size(38.dp)
                        .background(AuroraCard, CircleShape)
                        .border(1.dp, AuroraBorder, CircleShape)
                        .testTag("open_cart_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Cart",
                            tint = AuroraCream,
                            modifier = Modifier.size(18.dp)
                        )

                        if (cartCount > 0) {
                            Surface(
                                shape = CircleShape,
                                color = AuroraGold,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 6.dp, y = (-4).dp)
                                    .size(16.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "$cartCount",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = AuroraBackground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Horizontal Navigation Bar
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(navTabs) { (key, label) ->
                val isSelected = activeTab == key
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) AuroraGold else Color.Transparent,
                    border = if (!isSelected) BorderStroke(0.8.dp, AuroraBorder.copy(alpha = 0.4f)) else null,
                    modifier = Modifier
                        .clickable { onTabClick(key) }
                        .testTag("nav_tab_$key")
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = 1.sp,
                            fontSize = 10.sp
                        ),
                        color = if (isSelected) AuroraBackground else AuroraCream.copy(alpha = 0.85f),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
