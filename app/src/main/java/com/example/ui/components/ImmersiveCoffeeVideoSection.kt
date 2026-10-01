package com.example.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay
import kotlin.math.*

data class CinemaStoryScene(
    val title: String,
    val subtitle: String,
    val imageResId: Int,
    val timestamp: String,
    val slowMoDesc: String
)

@Composable
fun ImmersiveCoffeeVideoSection(
    modifier: Modifier = Modifier
) {
    val scenes = remember {
        listOf(
            CinemaStoryScene(
                title = "HARVEST GRAVITY",
                subtitle = "Hand-picked high-elevation Arabica cherries descending in 120 FPS slow motion.",
                imageResId = R.drawable.img_coffee_roast_craft,
                timestamp = "00:08",
                slowMoDesc = "120 FPS Slow-Motion"
            ),
            CinemaStoryScene(
                title = "HYDRAULIC ESSENCE",
                subtitle = "The first drops of 9-bar espresso emulsifying into dark honey crema.",
                imageResId = R.drawable.img_hero_coffee_cube,
                timestamp = "00:24",
                slowMoDesc = "Macro 4K Lens"
            ),
            CinemaStoryScene(
                title = "MICROFOAM VELVET",
                subtitle = "Silky textured microfoam folding into dark roast to sculpt golden Rosetta art.",
                imageResId = R.drawable.img_espresso_crema_cup,
                timestamp = "00:42",
                slowMoDesc = "Fluid Dynamics"
            ),
            CinemaStoryScene(
                title = "THE DEGASSING SEAL",
                subtitle = "Nitrogen-flushed foil packaging preserving aromatic volatiles at peak freshness.",
                imageResId = R.drawable.img_coffee_bag_pack,
                timestamp = "01:05",
                slowMoDesc = "Preservation Chamber"
            )
        )
    }

    var activeSceneIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(true) }

    // Auto-advance scenes when playing
    LaunchedEffect(isPlaying, activeSceneIndex) {
        if (isPlaying) {
            delay(4500)
            activeSceneIndex = (activeSceneIndex + 1) % scenes.size
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "videoAtmosphere")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("immersive_coffee_video_section")
    ) {
        // Section Header
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "CINEMATIC CAPTURE",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.5.sp),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "EVERY CUP\nHAS A STORY.",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    lineHeight = 36.sp
                ),
                color = AuroraCream
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Slow-motion cine frames capturing the thermodynamics, microfoam fluidics, and pure sensory theatre of Aurora coffee.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraMuted
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Cinematic Video Player Box
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AuroraCard),
            border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.8f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    Crossfade(
                        targetState = scenes[activeSceneIndex],
                        animationSpec = tween(900),
                        label = "cinemaScene"
                    ) { scene ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = scene.imageResId),
                                contentDescription = scene.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Cine letterbox gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.65f),
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.85f)
                                            )
                                        )
                                    )
                            )
                        }
                    }

                    // Floating Particle Canvas Simulation overlay
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        for (i in 0..15) {
                            val seed = i * 29.3f
                            val px = (sin(seed + pulse * 2f) * 0.5f + 0.5f) * w
                            val py = (cos(seed * 1.7f + pulse * 1.5f) * 0.5f + 0.5f) * h
                            drawCircle(
                                color = AuroraGold.copy(alpha = 0.25f * pulse),
                                radius = 2f,
                                center = Offset(px, py)
                            )
                        }
                    }

                    // Top Player Bar with badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.75f),
                            border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(AuroraAccentRed, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AURORA CINEMA • ${scenes[activeSceneIndex].slowMoDesc}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        letterSpacing = 1.sp
                                    ),
                                    color = AuroraCream
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.Black.copy(alpha = 0.65f)
                        ) {
                            Text(
                                text = scenes[activeSceneIndex].timestamp,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = AuroraGold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Center Play/Pause Overlay Button
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(54.dp)
                            .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                            .border(1.dp, AuroraGold, CircleShape)
                            .testTag("toggle_video_play_pause")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = AuroraGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Bottom Scene Title in Video Frame
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = scenes[activeSceneIndex].title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = AuroraCream
                        )
                        Text(
                            text = scenes[activeSceneIndex].subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp
                            ),
                            color = AuroraCream.copy(alpha = 0.85f),
                            maxLines = 2
                        )
                    }
                }

                // Scene Selector Carousel Pills
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    scenes.forEachIndexed { idx, s ->
                        val isCurr = idx == activeSceneIndex
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (isCurr) AuroraGold else AuroraBorder)
                                .clickable {
                                    activeSceneIndex = idx
                                    isPlaying = true
                                }
                        )
                    }
                }
            }
        }
    }
}
