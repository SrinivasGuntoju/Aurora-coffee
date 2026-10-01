package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.CoffeeDataRepository
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun InteractiveCoffeeLab(
    config: CoffeeLabConfig,
    onSelectBean: (BeanOption) -> Unit,
    onSelectRoast: (RoastOption) -> Unit,
    onSelectGrind: (GrindOption) -> Unit,
    onSelectBrew: (BrewOption) -> Unit,
    onSelectSize: (SizeOption) -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeStep by remember { mutableStateOf("bean") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_coffee_lab_section")
    ) {
        // Section Header
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "INTERACTIVE BREW LABORATORY",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.5.sp),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "BUILD YOUR COFFEE",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = AuroraCream
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Engineer a bespoke cup from varietal genetics and roast chemistry to micron extraction. Watch your cup calibrate in real time.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraMuted
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Live 3D Coffee Cup Simulator Card
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
                // Live 3D Interactive Cup Canvas
                Interactive3DCupCanvas(
                    config = config,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Real-time Flavor Spectrum Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FlavorScorePill("ACIDITY", config.acidityScore, AuroraAmber)
                    FlavorScorePill("BODY", config.bodyScore, AuroraCaramel)
                    FlavorScorePill("SWEETNESS", config.sweetnessScore, AuroraGold)
                    FlavorScorePill("AROMA", config.aromaIntensity, AuroraCream)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Step Navigation Bar: BEAN -> ROAST -> GRIND -> BREW -> SIZE
        val steps = listOf(
            "bean" to "1. BEAN",
            "roast" to "2. ROAST",
            "grind" to "3. GRIND",
            "brew" to "4. BREW",
            "size" to "5. SIZE"
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(steps) { (key, label) ->
                val isSelected = activeStep == key
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) AuroraGold else AuroraCard,
                    border = BorderStroke(1.dp, if (isSelected) AuroraGold else AuroraBorder.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .clickable { activeStep = key }
                        .testTag("lab_step_$key")
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            letterSpacing = 1.sp
                        ),
                        color = if (isSelected) AuroraBackground else AuroraCream,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step Options Selector
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            when (activeStep) {
                "bean" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CoffeeDataRepository.beanOptions.forEach { bean ->
                            val isChosen = config.bean.id == bean.id
                            LabOptionRow(
                                title = bean.name,
                                subtitle = "${bean.region} • ${bean.elevation}",
                                detail = bean.notes,
                                isSelected = isChosen,
                                onClick = { onSelectBean(bean) }
                            )
                        }
                    }
                }
                "roast" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CoffeeDataRepository.roastOptions.forEach { roast ->
                            val isChosen = config.roast.id == roast.id
                            LabOptionRow(
                                title = roast.name,
                                subtitle = "Thermal Peak: ${roast.tempC}",
                                detail = roast.profile,
                                isSelected = isChosen,
                                onClick = { onSelectRoast(roast) }
                            )
                        }
                    }
                }
                "grind" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CoffeeDataRepository.grindOptions.forEach { grind ->
                            val isChosen = config.grind.id == grind.id
                            LabOptionRow(
                                title = grind.name,
                                subtitle = "Particle Size: ${grind.microns}",
                                detail = grind.bestFor,
                                isSelected = isChosen,
                                onClick = { onSelectGrind(grind) }
                            )
                        }
                    }
                }
                "brew" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CoffeeDataRepository.brewOptions.forEach { brew ->
                            val isChosen = config.brew.id == brew.id
                            LabOptionRow(
                                title = brew.name,
                                subtitle = "Extraction: ${brew.time} • ${brew.pressure}",
                                detail = "Crema: ${brew.cremaType}",
                                isSelected = isChosen,
                                onClick = { onSelectBrew(brew) }
                            )
                        }
                    }
                }
                "size" -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CoffeeDataRepository.sizeOptions.forEach { size ->
                            val isChosen = config.size.id == size.id
                            LabOptionRow(
                                title = size.name,
                                subtitle = size.volume,
                                detail = "Craft Ratio 1:16",
                                isSelected = isChosen,
                                onClick = { onSelectSize(size) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // "YOUR PERFECT CUP" Summary & Add to Cart Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AuroraSurfaceVariant),
            border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.6f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "YOUR PERFECT CUP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = AuroraGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${config.bean.name} (${config.roast.name})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = AuroraCream
                        )
                    }

                    Text(
                        text = "₹${config.estimatedPrice}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AuroraGold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Engineered with ${config.grind.name} for ${config.brew.name} in a ${config.size.name} format.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuroraMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuroraGold,
                        contentColor = AuroraBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("lab_add_to_cart_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ADD TO CART • ₹${config.estimatedPrice}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Interactive3DCupCanvas(
    config: CoffeeLabConfig,
    modifier: Modifier = Modifier
) {
    val liquidTargetColor = when (config.roast.id) {
        "light" -> Color(0xFF8A5D3B)
        "medium" -> Color(0xFF4E2C17)
        "dark" -> Color(0xFF28140A)
        else -> Color(0xFF140804)
    }

    val liquidColor by animateColorAsState(
        targetValue = liquidTargetColor,
        animationSpec = tween(500),
        label = "cupLiquidColor"
    )

    val fillRatioTarget = when (config.size.id) {
        "single" -> 0.45f
        "double" -> 0.70f
        "grande" -> 0.88f
        else -> 0.60f
    }
    val fillRatio by animateFloatAsState(
        targetValue = fillRatioTarget,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "cupFillRatio"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "cupSteam")
    val steamY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -35f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "steamY"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f + 15f

        // Ambient glow beneath cup
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(AuroraGold.copy(alpha = 0.2f), Color.Transparent),
                center = Offset(cx, cy + 60f),
                radius = 120f
            ),
            topLeft = Offset(cx - 90f, cy + 45f),
            size = Size(180f, 30f)
        )

        // Saucer plate
        drawOval(
            color = Color(0xFF18110B),
            topLeft = Offset(cx - 100f, cy + 50f),
            size = Size(200f, 26f)
        )
        drawOval(
            color = AuroraGold.copy(alpha = 0.6f),
            topLeft = Offset(cx - 98f, cy + 50f),
            size = Size(196f, 24f),
            style = Stroke(width = 1.5f)
        )

        // Cup Outer Body Geometry (Conical Modern Ceramic Mug)
        val cupTopY = cy - 50f
        val cupBottomY = cy + 45f
        val cupTopWidth = 140f
        val cupBottomWidth = 90f

        val cupPath = Path().apply {
            moveTo(cx - cupTopWidth / 2f, cupTopY)
            lineTo(cx - cupBottomWidth / 2f, cupBottomY)
            quadraticBezierTo(cx, cupBottomY + 12f, cx + cupBottomWidth / 2f, cupBottomY)
            lineTo(cx + cupTopWidth / 2f, cupTopY)
            close()
        }

        // Cup shading
        drawPath(
            path = cupPath,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF211710),
                    Color(0xFF38271C),
                    Color(0xFF160E08)
                ),
                startX = cx - cupTopWidth / 2f,
                endX = cx + cupTopWidth / 2f
            )
        )

        // Cup Handle
        val handlePath = Path().apply {
            moveTo(cx + cupTopWidth / 2f - 4f, cupTopY + 18f)
            cubicTo(
                cx + cupTopWidth / 2f + 42f, cupTopY + 18f,
                cx + cupTopWidth / 2f + 42f, cupBottomY - 14f,
                cx + cupBottomWidth / 2f + 5f, cupBottomY - 14f
            )
        }
        drawPath(
            path = handlePath,
            color = Color(0xFF38271C),
            style = Stroke(width = 10f, cap = StrokeCap.Round)
        )
        drawPath(
            path = handlePath,
            color = AuroraGold.copy(alpha = 0.5f),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )

        // Liquid Level inside Cup
        val liquidTopY = cupBottomY - (cupBottomY - cupTopY) * fillRatio
        val liquidWidthAtLevel = cupBottomWidth + (cupTopWidth - cupBottomWidth) * fillRatio - 8f

        // Coffee liquid body
        val liquidPath = Path().apply {
            moveTo(cx - liquidWidthAtLevel / 2f, liquidTopY)
            lineTo(cx - cupBottomWidth / 2f + 4f, cupBottomY)
            quadraticBezierTo(cx, cupBottomY + 8f, cx + cupBottomWidth / 2f - 4f, cupBottomY)
            lineTo(cx + liquidWidthAtLevel / 2f, liquidTopY)
            close()
        }
        drawPath(path = liquidPath, color = liquidColor)

        // Crema surface disc on top of liquid
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    AuroraAmber,
                    AuroraCaramel,
                    liquidColor
                ),
                center = Offset(cx, liquidTopY),
                radius = liquidWidthAtLevel / 2f
            ),
            topLeft = Offset(cx - liquidWidthAtLevel / 2f, liquidTopY - 8f),
            size = Size(liquidWidthAtLevel, 16f)
        )

        // Rosetta latte art / crema swirl if espresso or aeropress
        if (config.brew.id == "espresso") {
            drawCircle(
                color = AuroraCream.copy(alpha = 0.65f),
                radius = 8f,
                center = Offset(cx, liquidTopY)
            )
            drawCircle(
                color = AuroraAmber,
                radius = 4f,
                center = Offset(cx, liquidTopY)
            )
        }

        // Cup Rim Gold Trim
        drawOval(
            color = AuroraGold,
            topLeft = Offset(cx - cupTopWidth / 2f, cupTopY - 8f),
            size = Size(cupTopWidth, 16f),
            style = Stroke(width = 2f)
        )

        // Rising Steam Wisps
        for (i in -1..1) {
            val sx = cx + i * 22f + sin(steamY * 0.15f + i) * 12f
            val sy = cupTopY + steamY - 10f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AuroraCream.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(sx, sy),
                    radius = 22f
                ),
                radius = 22f,
                center = Offset(sx, sy)
            )
        }
    }
}

@Composable
private fun FlavorScorePill(name: String, score: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                letterSpacing = 1.sp
            ),
            color = AuroraMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(58.dp)
                .height(4.dp)
                .background(AuroraBorder, RoundedCornerShape(2.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = score.coerceIn(0.1f, 1f))
                    .fillMaxHeight()
                    .background(color, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
private fun LabOptionRow(
    title: String,
    subtitle: String,
    detail: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) AuroraCardElevated else AuroraCard,
        border = BorderStroke(
            1.dp,
            if (isSelected) AuroraGold else AuroraBorder.copy(alpha = 0.4f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = if (isSelected) AuroraGold else AuroraCream
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = AuroraMuted,
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = detail,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = AuroraAmber.copy(alpha = 0.9f),
                        fontSize = 10.sp
                    )
                )
            }

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = AuroraGold,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = AuroraBackground,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
