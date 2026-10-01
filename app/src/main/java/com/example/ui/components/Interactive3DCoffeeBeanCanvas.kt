package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.util.Bean3DTransform
import com.example.ui.util.Scroll3DTracker
import kotlin.math.*

@Composable
fun Interactive3DCoffeeBeanCanvas(
    isSplit: Boolean,
    onToggleSplit: () -> Unit,
    transform: Bean3DTransform = Scroll3DTracker.calculateTransform(0),
    modifier: Modifier = Modifier
) {
    var rotX by remember { mutableFloatStateOf(12f) }
    var rotY by remember { mutableFloatStateOf(-15f) }
    var selectedNoteIndex by remember { mutableIntStateOf(0) }

    // Combine manual split toggle with scroll-based expansion
    val baseSplitTarget = if (isSplit) 1f else transform.expansionProgress
    val splitProgress by animateFloatAsState(
        targetValue = baseSplitTarget,
        animationSpec = spring(dampingRatio = 0.72f, stiffness = Spring.StiffnessLow),
        label = "splitSeparation"
    )

    // Ambient floating and breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "beanIdle")
    val idleFloat by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleFloat"
    )
    val idleGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "idleGlow"
    )
    val vaporOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vaporOffset"
    )

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraCard),
        border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.45f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_3d_coffee_bean_section")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Section Eyebrow & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "3D SENSORY EXPLORER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 2.5.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuroraGold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "The Anatomy of a Bean",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold
                        ),
                        color = AuroraCream
                    )
                }

                // Interactive control buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = onToggleSplit,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isSplit) AuroraGold else AuroraCardElevated,
                            contentColor = if (isSplit) AuroraBackground else AuroraCream
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("split_bean_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallSplit,
                            contentDescription = "Split Bean",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isSplit) "MERGE BEAN" else "SPLIT BEAN",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    IconButton(
                        onClick = {
                            rotX = 12f
                            rotY = (rotY + 90f) % 360f
                        },
                        modifier = Modifier
                            .background(AuroraCardElevated, CircleShape)
                            .border(1.dp, AuroraBorder, CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RotateRight,
                            contentDescription = "Rotate",
                            tint = AuroraGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3D Canvas Box with touch gestures
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            rotY += dragAmount.x * 0.45f
                            rotX = (rotX - dragAmount.y * 0.35f).coerceIn(-40f, 40f)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2f
                    val cy = h / 2f + idleFloat

                    // Ambient golden background glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                AuroraCaramel.copy(alpha = 0.3f * idleGlow),
                                AuroraGold.copy(alpha = 0.08f * idleGlow),
                                Color.Transparent
                            ),
                            center = Offset(cx, cy),
                            radius = w * 0.52f
                        )
                    )

                    // Ambient floating micro-dust particles
                    val particleCount = 24
                    for (i in 0 until particleCount) {
                        val angle = (i * 15f + rotY * 0.4f) * (PI.toFloat() / 180f)
                        val dist = w * 0.38f + sin(i * 1.7f + idleFloat * 0.1f) * 24f
                        val px = cx + cos(angle) * dist
                        val py = cy + sin(angle) * (dist * 0.5f)
                        val alpha = (0.2f + sin(i + rotY * 0.05f) * 0.3f).coerceIn(0.1f, 0.85f)
                        drawCircle(
                            color = AuroraGold.copy(alpha = alpha),
                            radius = 2.0f,
                            center = Offset(px, py)
                        )
                    }

                    val effectiveRotY = rotY + (transform.rotationYDegrees * 0.4f)
                    val effectiveRotX = (rotX + transform.rotationXDegrees).coerceIn(-45f, 45f)
                    val splitDistance = splitProgress * (w * 0.23f)
                    val beanWidth = w * 0.38f
                    val beanHeight = h * 0.72f

                    // Perspective squash based on rotX
                    val pitchScaleY = cos(effectiveRotX * (PI.toFloat() / 180f)).coerceIn(0.65f, 1f)
                    val effectiveHeight = beanHeight * pitchScaleY

                    // LEFT HALF OF BEAN
                    rotate(effectiveRotY * 0.12f, pivot = Offset(cx - splitDistance, cy)) {
                        val leftCx = cx - splitDistance
                        // Base gradient
                        drawOval(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF1B0B04),
                                    Color(0xFF381F0F),
                                    Color(0xFF5B341B)
                                )
                            ),
                            topLeft = Offset(leftCx - beanWidth * 0.5f, cy - effectiveHeight * 0.5f),
                            size = Size(beanWidth * 0.52f, effectiveHeight)
                        )

                        // Specular highlight sheen
                        drawOval(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    AuroraCaramel.copy(alpha = 0.4f),
                                    Color.Transparent
                                ),
                                center = Offset(leftCx - beanWidth * 0.22f, cy - effectiveHeight * 0.2f),
                                radius = beanWidth * 0.32f
                            ),
                            topLeft = Offset(leftCx - beanWidth * 0.45f, cy - effectiveHeight * 0.4f),
                            size = Size(beanWidth * 0.4f, effectiveHeight * 0.8f)
                        )

                        // If split, draw interior cellular roasting microstructure
                        if (splitProgress > 0.05f) {
                            drawOval(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AuroraAmber.copy(alpha = 0.85f * splitProgress),
                                        Color(0xFF965B32).copy(alpha = 0.9f * splitProgress),
                                        Color(0xFF432311)
                                    )
                                ),
                                topLeft = Offset(leftCx - beanWidth * 0.06f, cy - effectiveHeight * 0.45f),
                                size = Size(beanWidth * 0.14f, effectiveHeight * 0.9f)
                            )

                            // Cellular striation micro-lines
                            for (line in 0..6) {
                                val ly = cy - effectiveHeight * 0.35f + line * (effectiveHeight * 0.11f)
                                drawLine(
                                    color = AuroraGold.copy(alpha = 0.5f * splitProgress),
                                    start = Offset(leftCx - beanWidth * 0.04f, ly),
                                    end = Offset(leftCx + beanWidth * 0.06f, ly + 4f),
                                    strokeWidth = 1.5f
                                )
                            }
                        }
                    }

                    // RIGHT HALF OF BEAN
                    rotate(-effectiveRotY * 0.12f, pivot = Offset(cx + splitDistance, cy)) {
                        val rightCx = cx + splitDistance
                        // Base gradient
                        drawOval(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF5B341B),
                                    Color(0xFF381F0F),
                                    Color(0xFF1B0B04)
                                )
                            ),
                            topLeft = Offset(rightCx, cy - effectiveHeight * 0.5f),
                            size = Size(beanWidth * 0.52f, effectiveHeight)
                        )

                        // Specular highlight sheen
                        drawOval(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    AuroraGold.copy(alpha = 0.35f),
                                    Color.Transparent
                                ),
                                center = Offset(rightCx + beanWidth * 0.28f, cy - effectiveHeight * 0.15f),
                                radius = beanWidth * 0.32f
                            ),
                            topLeft = Offset(rightCx + beanWidth * 0.1f, cy - effectiveHeight * 0.4f),
                            size = Size(beanWidth * 0.4f, effectiveHeight * 0.8f)
                        )

                        // If split, draw right internal roasting core
                        if (splitProgress > 0.05f) {
                            drawOval(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF432311),
                                        Color(0xFF965B32).copy(alpha = 0.9f * splitProgress),
                                        AuroraAmber.copy(alpha = 0.85f * splitProgress)
                                    )
                                ),
                                topLeft = Offset(rightCx - beanWidth * 0.08f, cy - effectiveHeight * 0.45f),
                                size = Size(beanWidth * 0.14f, effectiveHeight * 0.9f)
                            )

                            // Cellular striation micro-lines
                            for (line in 0..6) {
                                val ly = cy - effectiveHeight * 0.35f + line * (effectiveHeight * 0.11f)
                                drawLine(
                                    color = AuroraGold.copy(alpha = 0.5f * splitProgress),
                                    start = Offset(rightCx - beanWidth * 0.06f, ly + 4f),
                                    end = Offset(rightCx + beanWidth * 0.04f, ly),
                                    strokeWidth = 1.5f
                                )
                            }
                        }
                    }

                    // Center Crease Fissure Line when merged
                    if (splitProgress < 0.25f) {
                        val creaseAlpha = (1f - splitProgress * 4f).coerceIn(0f, 1f)
                        val creasePath = Path().apply {
                            moveTo(cx, cy - effectiveHeight * 0.46f)
                            cubicTo(
                                cx - beanWidth * 0.12f, cy - effectiveHeight * 0.15f,
                                cx + beanWidth * 0.12f, cy + effectiveHeight * 0.15f,
                                cx, cy + effectiveHeight * 0.46f
                            )
                        }
                        drawPath(
                            path = creasePath,
                            color = Color(0xFF0C0502).copy(alpha = creaseAlpha),
                            style = Stroke(width = 5.0f, cap = StrokeCap.Round)
                        )
                        drawPath(
                            path = creasePath,
                            color = AuroraCaramel.copy(alpha = 0.4f * creaseAlpha),
                            style = Stroke(width = 1.8f, cap = StrokeCap.Round)
                        )
                    }

                    // Escaping aroma vapor if bean is split open
                    if (splitProgress > 0.2f) {
                        for (v in 0..4) {
                            val vx = cx + sin(v * 1.5f + vaporOffset * 0.1f) * 18f
                            val vy = cy - 20f + vaporOffset - v * 14f
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        AuroraAmber.copy(alpha = 0.25f * splitProgress),
                                        Color.Transparent
                                    ),
                                    center = Offset(vx, vy),
                                    radius = 28f
                                ),
                                radius = 28f,
                                center = Offset(vx, vy)
                            )
                        }
                    }

                    // Floating Callout Pin Lines pointing to Bean
                    // Left pin: Origin
                    drawLine(
                        color = AuroraGold.copy(alpha = 0.5f),
                        start = Offset(cx - beanWidth * 0.4f - splitDistance, cy - 30f),
                        end = Offset(30f, 40f),
                        strokeWidth = 1.2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                    )
                    drawCircle(color = AuroraGold, radius = 3f, center = Offset(cx - beanWidth * 0.4f - splitDistance, cy - 30f))

                    // Right pin: Roast
                    drawLine(
                        color = AuroraAmber.copy(alpha = 0.5f),
                        start = Offset(cx + beanWidth * 0.4f + splitDistance, cy - 10f),
                        end = Offset(w - 30f, 40f),
                        strokeWidth = 1.2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                    )
                    drawCircle(color = AuroraAmber, radius = 3f, center = Offset(cx + beanWidth * 0.4f + splitDistance, cy - 10f))
                }

                // Interactive Hint Overlay
                Text(
                    text = "DRAG TO ROTATE IN 3D SPACE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = AuroraMuted.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Floating Information Badges (Strictly matching prompt: ORIGIN (ETHIOPIA), ROAST (MEDIUM), NOTES (CHOCOLATE, CARAMEL, BERRY))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BeanSpecBadge(
                    label = "ORIGIN",
                    value = "ETHIOPIA",
                    subValue = "Yirgacheffe • 2,150m",
                    modifier = Modifier.weight(1f)
                )
                BeanSpecBadge(
                    label = "ROAST",
                    value = "MEDIUM",
                    subValue = "Caramelized 208°C",
                    modifier = Modifier.weight(1f)
                )
                BeanSpecBadge(
                    label = "NOTES",
                    value = "CHOCOLATE",
                    subValue = "Caramel • Berry",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tasting Notes Spectrum Chips (CHOCOLATE, CARAMEL, BERRY)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = AuroraSurfaceVariant.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FLAVOR PROFILE:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AuroraGold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NotePill("CHOCOLATE", AuroraCaramel)
                        NotePill("CARAMEL", AuroraGold)
                        NotePill("BERRY", AuroraAmber)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotePill(name: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            ),
            color = color,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun BeanSpecBadge(
    label: String,
    value: String,
    subValue: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = AuroraSurfaceVariant.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 1.5.sp,
                    fontSize = 9.sp
                ),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = AuroraCream
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subValue,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 9.sp,
                    color = AuroraMuted
                ),
                maxLines = 1
            )
        }
    }
}
