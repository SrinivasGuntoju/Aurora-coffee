package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun HeroSection(
    onExploreClick: () -> Unit,
    onShopNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heroAtmosphere")
    val steamOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "steamOffset"
    )
    val goldenPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "goldenPulse"
    )
    val bounceY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(AuroraBackground)
            .testTag("hero_section")
    ) {
        // High-end cinematic visual background
        Image(
            painter = painterResource(id = R.drawable.img_hero_coffee_cube),
            contentDescription = "Aurora Coffee Glass Espresso Cube",
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp),
            contentScale = ContentScale.Crop
        )

        // Multi-layered cinematic gradient scrim for ultra-luxury look & perfect text legibility
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            AuroraBackground.copy(alpha = 0.85f),
                            Color.Transparent,
                            AuroraBackground.copy(alpha = 0.75f),
                            AuroraBackground
                        ),
                        startY = 0f,
                        endY = 1600f
                    )
                )
        )

        // Ambient Floating Coffee Particles & Steam Canvas Overlay
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(580.dp)
        ) {
            val w = size.width
            val h = size.height

            // Floating Golden Amber Micro-particles
            val count = 30
            for (i in 0 until count) {
                val seed = i * 47.3f
                val px = (sin(seed + steamOffset * 0.05f) * 0.5f + 0.5f) * w
                val py = ((cos(seed * 1.3f) * 0.5f + 0.5f) * h + steamOffset * 3f + h) % h
                val rad = 1.2f + (i % 4) * 0.8f
                val alpha = (0.25f + sin(i + goldenPulse) * 0.35f).coerceIn(0.1f, 0.9f)
                drawCircle(
                    color = AuroraGold.copy(alpha = alpha),
                    radius = rad,
                    center = Offset(px, py)
                )
            }

            // Rising aromatic steam wisps above the glass cube center
            val centerX = w * 0.5f
            val centerY = h * 0.42f
            for (s in 0..4) {
                val sOffX = sin(s * 1.2f + steamOffset * 0.1f) * 24f
                val sOffY = steamOffset * 2f - s * 18f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AuroraCream.copy(alpha = 0.08f * goldenPulse),
                            Color.Transparent
                        ),
                        center = Offset(centerX + sOffX, centerY + sOffY),
                        radius = 45f + s * 12f
                    ),
                    radius = 45f + s * 12f,
                    center = Offset(centerX + sOffX, centerY + sOffY)
                )
            }
        }

        // Foreground Editorial Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Sub-brand Eyebrow
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AuroraSurfaceVariant.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(AuroraGold, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "THE REVERIE OF EXTRACTION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.5.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = AuroraGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Headline
            Text(
                text = "COFFEE,\nCRAFTED LIKE ART.",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    lineHeight = 44.sp
                ),
                color = AuroraCream,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subheading
            Text(
                text = "From carefully selected beans to the final pour, experience coffee beyond the ordinary.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.5.sp
                ),
                color = AuroraMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // EXPLORE COFFEE Button
                Button(
                    onClick = onExploreClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuroraGold,
                        contentColor = AuroraBackground
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("explore_coffee_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalCafe,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EXPLORE COFFEE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                // SHOP NOW Button
                OutlinedButton(
                    onClick = onShopNowClick,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AuroraCream.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AuroraCard.copy(alpha = 0.6f),
                        contentColor = AuroraCream
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("shop_now_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        tint = AuroraCream,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SHOP NOW",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            // Scroll indicator: "SCROLL TO DISCOVER ↓"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = bounceY.dp)
            ) {
                Text(
                    text = "SCROLL TO DISCOVER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = AuroraGold.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Scroll down",
                    tint = AuroraGold.copy(alpha = 0.85f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
