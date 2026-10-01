package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun BrandStorySection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("brand_story_section")
    ) {
        // Editorial Hero Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_coffee_roast_craft),
                contentDescription = "Aurora Coffee Craft Roasting",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AuroraBackground.copy(alpha = 0.5f),
                                Color.Transparent,
                                AuroraBackground.copy(alpha = 0.95f),
                                AuroraBackground
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    text = "THE AURORA ETHOS",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.5.sp),
                    color = AuroraGold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "WE DON'T JUST ROAST COFFEE.\nWE CREATE MOMENTS.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        lineHeight = 32.sp
                    ),
                    color = AuroraCream
                )
            }
        }

        // Philosophy Pillars
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StoryPillarCard(
                number = "01",
                title = "ETHICAL DIRECT TRADE",
                description = "We bypass multi-tiered commodity brokers, paying our partner coffee farmers 300% above Fair Trade minimums. We fund soil microbiology research in Yirgacheffe and Huila."
            )

            StoryPillarCard(
                number = "02",
                title = "SMALL-BATCH THERMODYNAMICS",
                description = "Every roast lot never exceeds 12 kilograms. We employ dual-wall conductive drum roasters equipped with infrared telemetry that samples bean-core heat every 200 milliseconds."
            )

            StoryPillarCard(
                number = "03",
                title = "CERTIFIED Q-GRADER RIGOR",
                description = "Only harvests scoring 88 points or higher on the Specialty Coffee Association (SCA) cupping protocol earn the Aurora gold foil insignia."
            )

            StoryPillarCard(
                number = "04",
                title = "CARBON-NEUTRAL RECOVERY",
                description = "Our roasters use recirculating thermal clean burners that capture 98% of exhaust emissions. All packaging is 100% commercially compostable barrier Kraft paper."
            )
        }
    }
}

@Composable
private fun StoryPillarCard(
    number: String,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AuroraCard),
        border = BorderStroke(1.dp, AuroraBorder.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                ),
                color = AuroraGold
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    color = AuroraCream
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = AuroraMuted
                )
            }
        }
    }
}
