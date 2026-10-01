package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
fun ProductShowcase3D(
    modifier: Modifier = Modifier
) {
    var rotationY by remember { mutableFloatStateOf(0f) }
    var valveAromaReleased by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "packagingFloat")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "packFloat"
    )
    val goldSheenOffset by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "goldSheen"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_showcase_3d_section")
    ) {
        // Section Header
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "SCULPTURAL PACKAGING",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.5.sp),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "3D VESSEL OF FRESHNESS",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = AuroraCream
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Constructed with multi-layered matte black barrier foil and one-way degassing valves to lock in volatile aromatic compounds.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraMuted
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3D Packaging Interactive Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AuroraCard),
            border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.75f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Interactive 3D Package Stage
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                rotationY += dragAmount.x * 0.5f
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Ambient Light Glow Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f + floatOffset
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    AuroraGold.copy(alpha = 0.22f),
                                    Color.Transparent
                                ),
                                center = Offset(cx, cy),
                                radius = size.width * 0.45f
                            )
                        )

                        // Orbiting Coffee Beans / Golden flecks
                        for (i in 0..12) {
                            val angle = (i * 28f + rotationY * 0.8f) * (PI.toFloat() / 180f)
                            val dist = size.width * 0.38f
                            val px = cx + cos(angle) * dist
                            val py = cy + sin(angle) * (dist * 0.4f)
                            drawCircle(
                                color = AuroraGold.copy(alpha = 0.4f + sin(i + rotationY * 0.05f) * 0.3f),
                                radius = 2.5f,
                                center = Offset(px, py)
                            )
                        }
                    }

                    // Floating Packaging Image with 3D Y-axis perspective rotation
                    Box(
                        modifier = Modifier
                            .size(240.dp, 280.dp)
                            .graphicsLayer {
                                this.rotationY = (rotationY % 360f)
                                translationY = floatOffset
                                cameraDistance = 14f * density
                            }
                            .clip(RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_coffee_bag_pack),
                            contentDescription = "Aurora Coffee 3D Packaging",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Specular Light Gleam overlay
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        AuroraGold.copy(alpha = 0.25f),
                                        Color.Transparent
                                    ),
                                    start = Offset(goldSheenOffset, 0f),
                                    end = Offset(goldSheenOffset + 80f, size.height)
                                )
                            )
                        }
                    }

                    // Rotate hint
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AuroraBackground.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, AuroraBorder),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.RotateRight,
                                contentDescription = null,
                                tint = AuroraGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SWIPE TO ROTATE 360°",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    letterSpacing = 1.2.sp,
                                    fontSize = 9.sp
                                ),
                                color = AuroraCream
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Interactive Degassing Aroma Valve
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = AuroraSurfaceVariant.copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "FRESHNESS VALVE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        letterSpacing = 1.5.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = AuroraGold
                                )
                                if (valveAromaReleased) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "• AROMA RELEASED",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AuroraSuccess,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (valveAromaReleased)
                                    "Fragrance: Sweet Cacao, Roasted Almond, Orange Blossom"
                                else
                                    "Tap to test the aroma degassing valve",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (valveAromaReleased) AuroraCream else AuroraMuted
                                )
                            )
                        }

                        Button(
                            onClick = { valveAromaReleased = !valveAromaReleased },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (valveAromaReleased) AuroraGold else AuroraCardElevated,
                                contentColor = if (valveAromaReleased) AuroraBackground else AuroraCream
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "Test Aroma",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (valveAromaReleased) "RESET" else "SMELL AROMA",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}
