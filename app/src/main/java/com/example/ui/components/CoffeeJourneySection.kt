package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Speed
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
import com.example.data.model.JourneyStage
import com.example.data.repository.CoffeeDataRepository
import com.example.ui.theme.*

@Composable
fun CoffeeJourneySection(
    activeStageIndex: Int,
    onStageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val stages = CoffeeDataRepository.journeyStages
    val currentStage = stages[activeStageIndex.coerceIn(0, stages.size - 1)]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("coffee_journey_section")
    ) {
        // Section Header
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "THE METICULOUS JOURNEY",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.5.sp),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "FROM BEAN TO CUP",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = AuroraCream
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Witness the transformation of highland Arabica cherries through five chapters of scientific precision and artisanal devotion.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraMuted
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Horizontal Timeline Tabs (01 SELECT -> 02 ROAST -> 03 GRIND -> 04 BREW -> 05 ENJOY)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(stages) { index, stage ->
                val isSelected = index == activeStageIndex
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) AuroraGold else AuroraCard,
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) AuroraGold else AuroraBorder.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .clickable { onStageSelected(index) }
                        .testTag("journey_stage_${stage.number}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stage.number,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = if (isSelected) AuroraBackground else AuroraAmber
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stage.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                letterSpacing = 1.2.sp
                            ),
                            color = if (isSelected) AuroraBackground else AuroraCream
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Stage Visual Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AuroraCard),
            border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.6f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column {
                // Stage Visual
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = currentStage.imageResId),
                        contentDescription = currentStage.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        AuroraCard.copy(alpha = 0.7f),
                                        AuroraCard
                                    )
                                )
                            )
                    )

                    // Stage Number Pill on Image
                    Surface(
                        shape = CircleShape,
                        color = AuroraBackground.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, AuroraGold.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "STAGE ${currentStage.number}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = AuroraGold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    // Key Metric Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AuroraBackground.copy(alpha = 0.9f),
                        border = BorderStroke(1.dp, AuroraCaramel.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = AuroraCaramel,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = currentStage.keyMetric,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = AuroraCream
                                )
                                Text(
                                    text = currentStage.metricLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 8.sp
                                    ),
                                    color = AuroraMuted
                                )
                            }
                        }
                    }
                }

                // Stage Description Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = currentStage.subtitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Serif,
                            color = AuroraGold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentStage.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp
                        ),
                        color = AuroraCream.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sensory notes
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = AuroraSurfaceVariant.copy(alpha = 0.8f),
                        border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PROFILE: ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AuroraAmber
                                )
                            )
                            Text(
                                text = currentStage.sensoryProfile,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AuroraCream,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Next / Previous buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { onStageSelected((activeStageIndex - 1 + stages.size) % stages.size) },
                            enabled = activeStageIndex > 0,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, AuroraBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AuroraCream)
                        ) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous")
                            Text("PREV", style = MaterialTheme.typography.labelSmall)
                        }

                        Text(
                            text = "${activeStageIndex + 1} / ${stages.size}",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
                            color = AuroraMuted
                        )

                        Button(
                            onClick = { onStageSelected((activeStageIndex + 1) % stages.size) },
                            enabled = activeStageIndex < stages.size - 1,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AuroraGold,
                                contentColor = AuroraBackground
                            )
                        ) {
                            Text("NEXT STAGE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            Icon(Icons.Default.ChevronRight, contentDescription = "Next")
                        }
                    }
                }
            }
        }
    }
}
