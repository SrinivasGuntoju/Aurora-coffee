package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubscriptionPlan
import com.example.data.repository.CoffeeDataRepository
import com.example.ui.theme.*

@Composable
fun SubscriptionSection(
    onSubscribe: (SubscriptionPlan) -> Unit,
    modifier: Modifier = Modifier
) {
    val plans = CoffeeDataRepository.subscriptionPlans
    var selectedPlanId by remember { mutableStateOf("monthly") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("subscription_section")
    ) {
        // Section Header
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "CONTINUOUS PERFECTION",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.5.sp),
                color = AuroraGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "NEVER RUN OUT\nOF GREAT COFFEE.",
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
                text = "Join our roaster subscription society. Micro-lots dispatched straight from the cooling tray to your door within 48 hours.",
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraMuted
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Subscription Plan Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            plans.forEach { plan ->
                val isSelected = selectedPlanId == plan.id

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) AuroraCardElevated else AuroraCard
                    ),
                    border = BorderStroke(
                        1.5.dp,
                        if (isSelected) AuroraGold else AuroraBorder.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPlanId = plan.id }
                        .testTag("subscription_plan_${plan.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Badge & Title row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = plan.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = if (isSelected) AuroraGold else AuroraCream
                            )

                            if (plan.isPopular) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = AuroraGold,
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text(
                                        text = "MOST POPULAR",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        ),
                                        color = AuroraBackground,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = plan.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = AuroraMuted
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Price
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "₹${plan.priceInr}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = AuroraGold
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = plan.period,
                                style = MaterialTheme.typography.labelSmall,
                                color = AuroraMuted,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Perks List
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            plan.perks.forEach { perk ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = AuroraGold,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = perk,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                        color = AuroraCream.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { onSubscribe(plan) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) AuroraGold else AuroraCardElevated,
                                contentColor = if (isSelected) AuroraBackground else AuroraCream
                            ),
                            border = if (!isSelected) BorderStroke(1.dp, AuroraBorderLight) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("start_subscription_${plan.id}")
                        ) {
                            Text(
                                text = if (isSelected) "START MY SUBSCRIPTION" else "SELECT THIS PLAN",
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
    }
}
