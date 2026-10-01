package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun CinematicLoadingScreen(
    progress: Float,
    onEnterClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "beanRotation"
    )
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beanFloat"
    )
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AuroraBackground)
            .testTag("cinematic_loading_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Ambient background glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        AuroraGold.copy(alpha = 0.15f * pulseGlow),
                        AuroraCaramel.copy(alpha = 0.05f * pulseGlow),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.width * 0.7f
                )
            )

            // Subtle floating coffee particles
            val particleCount = 28
            for (i in 0 until particleCount) {
                val seed = i * 137.5f
                val pX = (sin(seed + rotation * 0.02f) * 0.5f + 0.5f) * size.width
                val pY = ((cos(seed * 0.7f + rotation * 0.015f) * 0.5f + 0.5f) * size.height + floatOffset * 2f) % size.height
                val pRadius = 1.2f + (i % 3) * 1.0f
                val pAlpha = (0.2f + (sin(rotation * 0.05f + i) + 1f) * 0.35f) * pulseGlow
                drawCircle(
                    color = AuroraAmber.copy(alpha = pAlpha),
                    radius = pRadius,
                    center = Offset(pX, pY)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            // Rotating 3D Coffee Bean
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h / 2f + floatOffset

                    // 3D Outer Halo
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AuroraCaramel.copy(alpha = 0.35f * pulseGlow),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy),
                            radius = w * 0.55f
                        )
                    )

                    // 3D Coffee Bean
                    rotate(rotation, pivot = Offset(cx, cy)) {
                        // Shadow
                        drawOval(
                            color = Color.Black.copy(alpha = 0.5f),
                            topLeft = Offset(cx - w * 0.28f, cy - h * 0.42f + 8f),
                            size = Size(w * 0.56f, h * 0.84f)
                        )

                        // Main Bean Body with 3D gradient
                        drawOval(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF5A361D),
                                    Color(0xFF2C180C),
                                    Color(0xFF160A04)
                                ),
                                start = Offset(cx - w * 0.3f, cy - h * 0.4f),
                                end = Offset(cx + w * 0.3f, cy + h * 0.4f)
                            ),
                            topLeft = Offset(cx - w * 0.28f, cy - h * 0.42f),
                            size = Size(w * 0.56f, h * 0.84f)
                        )

                        // Specular Highlight
                        drawOval(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    AuroraCaramel.copy(alpha = 0.45f),
                                    Color.Transparent
                                ),
                                center = Offset(cx - w * 0.1f, cy - h * 0.15f),
                                radius = w * 0.25f
                            ),
                            topLeft = Offset(cx - w * 0.22f, cy - h * 0.35f),
                            size = Size(w * 0.44f, h * 0.65f)
                        )

                        // Signature Center Crease S-Curve
                        val path = Path().apply {
                            moveTo(cx, cy - h * 0.38f)
                            cubicTo(
                                cx - w * 0.12f, cy - h * 0.15f,
                                cx + w * 0.12f, cy + h * 0.15f,
                                cx, cy + h * 0.38f
                            )
                        }
                        drawPath(
                            path = path,
                            color = Color(0xFF0F0602),
                            style = Stroke(width = 4.5f, cap = StrokeCap.Round)
                        )
                        // Crease inner glow
                        drawPath(
                            path = path,
                            color = AuroraCaramel.copy(alpha = 0.35f),
                            style = Stroke(width = 1.5f, cap = StrokeCap.Round)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Brand Titles
            Text(
                text = "A U R O R A",
                style = MaterialTheme.typography.displayMedium.copy(
                    letterSpacing = 10.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Light
                ),
                color = AuroraCream
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "COFFEE ROASTERS",
                style = MaterialTheme.typography.labelLarge.copy(
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = AuroraGold
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Thin progress indicator
            Column(
                modifier = Modifier.widthIn(max = 240.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(AuroraBorder, RoundedCornerShape(1.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(AuroraGold, AuroraAmber, AuroraCream)
                                ),
                                RoundedCornerShape(1.dp)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "BREWING EXPERIENCE",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
                        color = AuroraMuted
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                        color = AuroraGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Instant enter button if user doesn't want to wait
            if (progress >= 0.85f) {
                OutlinedButton(
                    onClick = onEnterClick,
                    border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = AuroraCard.copy(alpha = 0.8f),
                        contentColor = AuroraCream
                    ),
                    modifier = Modifier.testTag("enter_experience_button")
                ) {
                    Text(
                        text = "ENTER EXPERIENCE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            letterSpacing = 3.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
