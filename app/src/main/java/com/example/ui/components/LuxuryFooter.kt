package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LuxuryFooter(
    emailInput: String,
    onEmailChange: (String) -> Unit,
    isSubscribed: Boolean,
    onSubscribeClick: () -> Unit,
    onNavClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF070403))
            .padding(horizontal = 24.dp, vertical = 40.dp)
            .testTag("luxury_footer")
    ) {
        // Brand Title
        Text(
            text = "AURORA COFFEE",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            ),
            color = AuroraCream
        )
        Text(
            text = "ROASTERS OF EXCEPTIONAL HARVESTS",
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            ),
            color = AuroraGold
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Newsletter Section
        Text(
            text = "GET COFFEE STORIES IN YOUR INBOX.",
            style = MaterialTheme.typography.labelMedium.copy(
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            ),
            color = AuroraCream
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Receive rare micro-lot release notifications, origin documentaries, and masterclass recipes.",
            style = MaterialTheme.typography.bodySmall,
            color = AuroraMuted
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (isSubscribed) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AuroraSuccess.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, AuroraSuccess.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = AuroraSuccess,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Welcome to the Aurora Reserve. First chronicle dispatched.",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = AuroraSuccess
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = onEmailChange,
                    placeholder = { Text("Enter your email address", fontSize = 12.sp, color = AuroraMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = AuroraCard,
                        unfocusedContainerColor = AuroraCard,
                        focusedBorderColor = AuroraGold,
                        unfocusedBorderColor = AuroraBorder,
                        focusedTextColor = AuroraCream,
                        unfocusedTextColor = AuroraCream
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("newsletter_email_input")
                )

                Button(
                    onClick = onSubscribeClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AuroraGold,
                        contentColor = AuroraBackground
                    ),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("newsletter_subscribe_button")
                ) {
                    Text(
                        text = "SUBSCRIBE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Divider(color = AuroraBorder.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(24.dp))

        // Navigation Links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "EXPLORE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = AuroraGold
                )
                Text(
                    text = "Single Origins",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuroraCream,
                    modifier = Modifier.clickable { onNavClick("collection") }
                )
                Text(
                    text = "Brew Laboratory",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuroraCream,
                    modifier = Modifier.clickable { onNavClick("lab") }
                )
                Text(
                    text = "Subscriptions",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuroraCream,
                    modifier = Modifier.clickable { onNavClick("subscription") }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "COMMUNITY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.5.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = AuroraGold
                )
                Text(
                    text = "Instagram @auroracoffee",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuroraCream
                )
                Text(
                    text = "YouTube / AuroraRoasters",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuroraCream
                )
                Text(
                    text = "Facebook / AuroraCoffeeClub",
                    style = MaterialTheme.typography.bodySmall,
                    color = AuroraCream
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Copyright and Certifications
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "© 2026 AURORA COFFEE ROASTERS • ALL RIGHTS RESERVED",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp
                ),
                color = AuroraMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Specialty Coffee Association Certified • Carbon Neutral Roastery",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    letterSpacing = 0.5.sp
                ),
                color = AuroraGold.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
