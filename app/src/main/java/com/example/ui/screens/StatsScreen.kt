package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyStatEntity
import com.example.ui.components.GlassCard
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.GlassBase
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoGlow
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun StatsScreen(
  weeklyStats: List<DailyStatEntity>,
  streakDays: Int = 4,
  milestoneMessage: String = "You've cleared gates for 4 days in a row! Mindful reflex engaged.",
  onOpenAppDetails: (String) -> Unit
) {
  var selectedDay by remember { mutableStateOf("Thu") }
  val activeStat = weeklyStats.find { it.dayOfWeek == selectedDay } ?: weeklyStats.firstOrNull()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Ambient lighting
    Box(
      modifier = Modifier
        .size(360.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.radialGradient(
            colors = listOf(PrimaryIndigoGlow.copy(alpha = 0.12f), Color.Transparent)
          )
        )
    )

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Header
      item {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Weekly Insights",
              color = TextPrimary,
              fontSize = 22.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Deliberate pause telemetry",
              color = TextSecondary,
              fontSize = 12.sp
            )
          }

          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(SurfaceContainerHigh)
              .border(1.dp, BorderSubtle, RoundedCornerShape(9999.dp))
              .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Encrypted Log",
              color = CalmMoss,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // Streak Counter & Milestone Card
      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.9f),
          borderColor = CalmMoss.copy(alpha = 0.4f),
          cornerRadius = 18.dp
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CalmMoss.copy(alpha = 0.15f))
                    .border(1.dp, CalmMoss.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = null,
                    tint = CalmMoss,
                    modifier = Modifier.size(22.dp)
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                  Text(
                    text = "CONSECUTIVE MINDFUL DAYS",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.08.sp
                  )
                  Text(
                    text = "$streakDays Days Streak",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(9999.dp))
                  .background(Color(0xFF163224))
                  .border(1.dp, CalmMoss.copy(alpha = 0.3f), RoundedCornerShape(9999.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "Active Streak",
                  color = CalmMoss,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Encouraging Milestone Message Box
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerHigh)
                .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = PrimaryIndigo,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = milestoneMessage.ifEmpty { "You've cleared gates for $streakDays days in a row! Prefrontal habit control is strengthening." },
                  color = TextPrimary,
                  fontSize = 12.sp,
                  lineHeight = 16.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Consecutive Day Checkpoint Dots
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEachIndexed { i, day ->
                val reached = i < streakDays
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(if (reached) CalmMoss else SurfaceContainerHighest)
                      .border(1.dp, if (reached) CalmMoss else BorderSubtle, CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    if (reached) {
                      Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CanvasBase, modifier = Modifier.size(14.dp))
                    } else {
                      Text("${i + 1}", color = TextMuted, fontSize = 9.sp)
                    }
                  }
                  Spacer(modifier = Modifier.height(3.dp))
                  Text(day, color = if (reached) CalmMoss else TextMuted, fontSize = 9.sp)
                }
              }
            }
          }
        }
      }

      // Hero Pillar Chart Card
      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
          cornerRadius = 20.dp
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "GATES CLEARED (LAST 7 DAYS)",
                  color = TextMuted,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold,
                  letterSpacing = 0.08.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "89 total gates",
                  color = TextPrimary,
                  fontSize = 24.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(CalmMoss.copy(alpha = 0.15f))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text("+18% Reclaimed", color = CalmMoss, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 7-Day Glass Pillars
            val maxVal = 25
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              weeklyStats.forEach { stat ->
                val isSelected = stat.dayOfWeek == selectedDay
                val heightFraction = (stat.gatesCleared.toFloat() / maxVal).coerceIn(0.15f, 1f)

                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier
                    .weight(1f)
                    .clickable { selectedDay = stat.dayOfWeek }
                ) {
                  if (stat.isPeak) {
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PrimaryIndigo)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = "${stat.gatesCleared}",
                        color = CanvasBase,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                      )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                  } else {
                    Text(
                      text = "${stat.gatesCleared}",
                      color = if (isSelected) TextPrimary else TextMuted,
                      fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                  }

                  // Pillar Bar
                  Box(
                    modifier = Modifier
                      .width(28.dp)
                      .height((heightFraction * 110).dp)
                      .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                      .background(
                        if (stat.isPeak) Brush.verticalGradient(listOf(PrimaryIndigo, PrimaryIndigoGlow.copy(alpha = 0.6f)))
                        else if (isSelected) Brush.verticalGradient(listOf(CalmMoss, CalmMoss.copy(alpha = 0.5f)))
                        else Brush.verticalGradient(listOf(SurfaceContainerHighest, SurfaceContainerHigh))
                      )
                      .border(
                        1.dp,
                        if (isSelected || stat.isPeak) BorderHighlight else BorderSubtle,
                        RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                      )
                  )

                  Spacer(modifier = Modifier.height(8.dp))

                  Text(
                    text = stat.dayOfWeek,
                    color = if (isSelected) PrimaryIndigo else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Baseline guidance line note
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Baseline pace: 12.7 gates/day",
                color = TextMuted,
                fontSize = 11.sp
              )
              if (activeStat != null) {
                Text(
                  text = "${activeStat.dayOfWeek}: ${activeStat.minutesSaved}m reclaimed",
                  color = CalmMoss,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              }
            }
          }
        }
      }

      // Most Gated App Card
      item {
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenAppDetails("com.instagram.android") },
          backgroundColor = SurfaceContainerLow,
          cornerRadius = 16.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(PrimaryIndigo.copy(alpha = 0.15f))
                  .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CameraAlt,
                  contentDescription = null,
                  tint = PrimaryIndigo,
                  modifier = Modifier.size(24.dp)
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column {
                Text(
                  text = "Most Gated App",
                  color = TextMuted,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold,
                  letterSpacing = 0.08.sp
                )
                Text(
                  text = "Instagram",
                  color = TextPrimary,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = "38 gated entries • avg 18s deliberate pause",
                  color = TextSecondary,
                  fontSize = 11.sp
                )
              }
            }

            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = null,
              tint = TextMuted,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      // Pause Sentiment Card
      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow,
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "PAUSE SENTIMENT",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.08.sp
              )
              Text(
                text = "16 Deflections",
                color = CalmMoss,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Split Ratio Bar
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
            ) {
              Box(
                modifier = Modifier
                  .weight(0.82f)
                  .fillMaxSize()
                  .background(PrimaryIndigo)
              )
              Spacer(modifier = Modifier.width(2.dp))
              Box(
                modifier = Modifier
                  .weight(0.18f)
                  .fillMaxSize()
                  .background(CalmMoss)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(PrimaryIndigo, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text("82% Deliberate Unlock", color = TextSecondary, fontSize = 11.sp)
              }

              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(CalmMoss, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text("18% Deflected Away", color = CalmMoss, fontSize = 11.sp)
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "16 times this week, encountering the friction pause helped you realize you didn't actually need to open the app, immediately saving 42+ minutes.",
              color = TextSecondary,
              fontSize = 11.sp,
              lineHeight = 16.sp
            )
          }
        }
      }

      // Local Telemetry Guarantee Banner
      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLowest.copy(alpha = 0.6f),
          cornerRadius = 14.dp
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = "Telemetry stored strictly client-side. Zero cloud sync or trackers.",
              color = TextMuted,
              fontSize = 11.sp
            )
          }
        }
        Spacer(modifier = Modifier.height(70.dp))
      }
    }
  }
}
