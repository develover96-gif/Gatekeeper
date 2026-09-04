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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSwitch
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoGlow
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
  tactileFriction: Boolean,
  emergencyOverride: Boolean,
  isDarkGlassTheme: Boolean,
  isReopenGraceEnabled: Boolean = true,
  reopenGraceSeconds: Int = 60,
  isZenModeActive: Boolean = false,
  zenModeDurationMinutes: Int = 25,
  zenModeRemainingMinutes: Int = 0,
  isAdaptiveGatesEnabled: Boolean = false,
  adaptiveIntelligenceStatus: String = "Learning your patterns...",
  socialCategoryLimitMinutes: Int = 45,
  entertainmentCategoryLimitMinutes: Int = 60,
  onToggleTactileFriction: (Boolean) -> Unit,
  onToggleEmergencyOverride: (Boolean) -> Unit,
  onToggleTheme: (Boolean) -> Unit,
  onToggleReopenGrace: (Boolean) -> Unit = {},
  onSetReopenGraceSeconds: (Int) -> Unit = {},
  onToggleZenMode: (Boolean, Int) -> Unit = { _, _ -> },
  onSetZenModeDuration: (Int) -> Unit = {},
  onToggleAdaptiveGates: (Boolean) -> Unit = {},
  onSetCategoryDailyLimit: (String, Int) -> Unit = { _, _ -> },
  onNavigateToMathCalibration: () -> Unit
) {
  var showResetDialog by remember { mutableStateOf(false) }
  var showExportDialog by remember { mutableStateOf(false) }
  var graceWindowMinutes by remember { mutableIntStateOf(15) }
  var selectedZenMins by remember { mutableIntStateOf(zenModeDurationMinutes) }
  var selectedGraceSecs by remember { mutableIntStateOf(reopenGraceSeconds) }
  var selectedSocialLimit by remember { mutableIntStateOf(socialCategoryLimitMinutes) }
  var selectedEntLimit by remember { mutableIntStateOf(entertainmentCategoryLimitMinutes) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 600.dp)
        .align(Alignment.TopCenter)
        .padding(horizontal = 20.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Top Header
      item {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Settings",
              color = TextPrimary,
              fontSize = 22.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "Preferences & Privacy Controls",
              color = TextSecondary,
              fontSize = 12.sp
            )
          }

          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(Color(0xFF163224))
              .border(1.dp, CalmMoss.copy(alpha = 0.3f), RoundedCornerShape(9999.dp))
              .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            PulsingBeacon(color = CalmMoss, size = 5.dp)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Local Guardian Active", color = CalmMoss, fontSize = 10.sp, fontWeight = FontWeight.Medium)
          }
        }
      }

      // User Profile Card
      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
          cornerRadius = 18.dp
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(SurfaceContainerHigh)
                .border(1.dp, BorderHighlight, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text("AR", color = PrimaryIndigo, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Alex Rivera", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(PrimaryIndigo.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text("Pro", color = PrimaryIndigo, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text("alex.rivera@private.local", color = TextSecondary, fontSize = 12.sp)
              Text("Single-player sovereign device license", color = TextMuted, fontSize = 10.sp)
            }
          }
        }
      }

      // Appearance & Display Section
      item {
        Text(
          text = "APPEARANCE & DISPLAY",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow,
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Visual Canvas Theme
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text("Visual Canvas", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                  Text("Obsidian Dark Glass with subtle specular borders", color = TextSecondary, fontSize = 11.sp)
                }
              }

              // Segmented theme pills
              Row(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .padding(2.dp)
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isDarkGlassTheme) PrimaryIndigo else Color.Transparent)
                    .clickable { onToggleTheme(true) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(
                    "Dark Glass",
                    color = if (isDarkGlassTheme) CanvasBase else TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (!isDarkGlassTheme) PrimaryIndigo else Color.Transparent)
                    .clickable { onToggleTheme(false) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(
                    "Light",
                    color = if (!isDarkGlassTheme) CanvasBase else TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
            Spacer(modifier = Modifier.height(12.dp))

            // Tactile Friction
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Vibration, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text("Tactile Friction", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                  Text("Subtle haptic confirmation on gate trigger", color = TextSecondary, fontSize = 11.sp)
                }
              }
              GlassSwitch(checked = tactileFriction, onCheckedChange = onToggleTactileFriction)
            }
          }
        }
      }

      // Friction & Gate Dynamics Section
      item {
        Text(
          text = "FRICTION & GATE DYNAMICS",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow,
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Default Grace Window
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  graceWindowMinutes = if (graceWindowMinutes == 15) 30 else 15
                },
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Timer, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text("Default Grace Window", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                  Text("Time allowed after clearing gate before next pause", color = TextSecondary, fontSize = 11.sp)
                }
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(SurfaceContainerHigh)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text("${graceWindowMinutes}m", color = PrimaryIndigo, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
            Spacer(modifier = Modifier.height(12.dp))

            // Math Calibration Gateway
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToMathCalibration() },
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text("Math Challenge Calibration", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                  Text("Tiers, random equations, timers, operations", color = TextSecondary, fontSize = 11.sp)
                }
              }
              Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
            Spacer(modifier = Modifier.height(12.dp))

            // Emergency Override
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text("Emergency Bypass Passcode", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                  Text("Allows instant unlock if urgent situation arises", color = TextSecondary, fontSize = 11.sp)
                }
              }
              GlassSwitch(checked = emergencyOverride, onCheckedChange = onToggleEmergencyOverride)
            }
          }
        }
      }

      // Zen Mode Sanctuary Section
      item {
        Text(
          text = "ZEN MODE FOCUS SANCTUARY",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      item {
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("zen_mode_settings_card"),
          backgroundColor = if (isZenModeActive) Color(0xFF13281E) else SurfaceContainerLow,
          borderColor = if (isZenModeActive) CalmMoss.copy(alpha = 0.5f) else BorderDefault,
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CalmMoss.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    Icons.Default.SelfImprovement,
                    contentDescription = null,
                    tint = CalmMoss,
                    modifier = Modifier.size(20.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                      text = "Zen Mode Sanctuary",
                      color = TextPrimary,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                    if (isZenModeActive) {
                      Spacer(modifier = Modifier.width(8.dp))
                      Box(
                        modifier = Modifier
                          .clip(RoundedCornerShape(4.dp))
                          .background(CalmMoss.copy(alpha = 0.25f))
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                      ) {
                        Text(
                          text = "Active · ${zenModeRemainingMinutes}m left",
                          color = CalmMoss,
                          fontSize = 10.sp,
                          fontWeight = FontWeight.SemiBold
                        )
                      }
                    }
                  }
                  Text(
                    text = if (isZenModeActive)
                      "Distracting apps are completely blocked. Challenges temporarily disabled."
                    else
                      "Block all distracting apps entirely for a defined focus session",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                  )
                }
              }

              Spacer(modifier = Modifier.width(8.dp))
              GlassSwitch(
                checked = isZenModeActive,
                onCheckedChange = { active ->
                  onToggleZenMode(active, selectedZenMins)
                }
              )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Focus Session Duration:",
              color = TextSecondary,
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              listOf(15, 25, 45, 60, 120).forEach { mins ->
                val isSelected = selectedZenMins == mins
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) CalmMoss else SurfaceContainerHigh)
                    .clickable {
                      selectedZenMins = mins
                      onSetZenModeDuration(mins)
                      if (isZenModeActive) {
                        onToggleZenMode(true, mins)
                      }
                    },
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "${mins}m",
                    color = if (isSelected) CanvasBase else TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }
          }
        }
      }

      // Smart Gates Section
      item {
        Text(
          text = "SMART GATES (ADAPTIVE LEARNING)",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = if (isAdaptiveGatesEnabled) Color(0xFF1A1C2E) else SurfaceContainerLow,
          borderColor = if (isAdaptiveGatesEnabled) PrimaryIndigo.copy(alpha = 0.4f) else BorderDefault,
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryIndigo.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = PrimaryIndigoGlow,
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Smart Gate Intelligence",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                  Text(
                    text = "Dynamic friction adjusts based on your context",
                    color = TextSecondary,
                    fontSize = 11.sp
                  )
                }
              }
              GlassSwitch(
                checked = isAdaptiveGatesEnabled,
                onCheckedChange = onToggleAdaptiveGates
              )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerHigh.copy(alpha = 0.5f))
                .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                .padding(12.dp)
            ) {
              Row(verticalAlignment = Alignment.Top) {
                Icon(
                  Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = if (adaptiveIntelligenceStatus.contains("Active")) CalmMoss else TextMuted,
                  modifier = Modifier.size(14.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = adaptiveIntelligenceStatus,
                  color = if (adaptiveIntelligenceStatus.contains("Active")) TextPrimary else TextSecondary,
                  fontSize = 11.sp,
                  lineHeight = 16.sp
                )
              }
            }
          }
        }
      }

      // Re-Open Grace Period Section
      item {
        Text(
          text = "RE-OPEN GRACE PERIOD",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      item {
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("reopen_grace_settings_card"),
          backgroundColor = SurfaceContainerLow,
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryIndigo.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    Icons.Default.HourglassBottom,
                    contentDescription = null,
                    tint = PrimaryIndigoGlow,
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Immediate Re-Open Grace",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                  Text(
                    text = "Skip friction if returning to app within $selectedGraceSecs seconds of closing",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                  )
                }
              }
              Spacer(modifier = Modifier.width(8.dp))
              GlassSwitch(
                checked = isReopenGraceEnabled,
                onCheckedChange = onToggleReopenGrace
              )
            }

            if (isReopenGraceEnabled) {
              Spacer(modifier = Modifier.height(12.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                listOf(30, 60, 90, 120).forEach { secs ->
                  val isSelected = selectedGraceSecs == secs
                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .height(36.dp)
                      .clip(RoundedCornerShape(10.dp))
                      .background(if (isSelected) PrimaryIndigo else SurfaceContainerHigh)
                      .clickable {
                        selectedGraceSecs = secs
                        onSetReopenGraceSeconds(secs)
                      },
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "${secs}s",
                      color = if (isSelected) CanvasBase else TextPrimary,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }
              }
            }
          }
        }
      }

      // Category Daily Limits Section
      item {
        Text(
          text = "CATEGORY DAILY USAGE LIMITS",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      item {
        GlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("category_daily_limits_card"),
          backgroundColor = SurfaceContainerLow,
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Layers, contentDescription = null, tint = PrimaryIndigoGlow, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text("Category Daily Limits", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("Gate triggers for every launch once total daily time is reached", color = TextSecondary, fontSize = 11.sp)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Social Limit
            Text(text = "Social Category Daily Limit", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              listOf(0 to "Off", 30 to "30m", 45 to "45m", 60 to "60m", 90 to "90m").forEach { (mins, label) ->
                val isSelected = selectedSocialLimit == mins
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) PrimaryIndigo else SurfaceContainerHigh)
                    .clickable {
                      selectedSocialLimit = mins
                      onSetCategoryDailyLimit("Social", mins)
                    },
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = label,
                    color = if (isSelected) CanvasBase else TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Entertainment Limit
            Text(text = "Entertainment Category Daily Limit", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              listOf(0 to "Off", 30 to "30m", 60 to "60m", 90 to "90m", 120 to "120m").forEach { (mins, label) ->
                val isSelected = selectedEntLimit == mins
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) PrimaryIndigo else SurfaceContainerHigh)
                    .clickable {
                      selectedEntLimit = mins
                      onSetCategoryDailyLimit("Entertainment", mins)
                    },
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = label,
                    color = if (isSelected) CanvasBase else TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }
          }
        }
      }

      // Data Privacy & Safeguards Section
      item {
        Text(
          text = "DATA PRIVACY & SAFEGUARDS",
          color = TextMuted,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      item {
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow,
          cornerRadius = 16.dp
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Shield, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text("100% Local Air-Gapped Storage", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("All app counters, pause logs, and configurations exist solely on this hardware device in an encrypted Room SQLite database.", color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Export Audit Log Action
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerHigh)
                .clickable { showExportDialog = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Download, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export Encrypted Audit Log", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
              }
              Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clear Activity History
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SurfaceContainerHigh)
                .clickable { showResetDialog = true }
                .padding(horizontal = 12.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Clear Local Activity History", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
              }
              Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
            }
          }
        }
      }

      // App Version Footer
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "Gatekeeper Sovereign Edition · v1.4.2",
            color = TextMuted,
            fontSize = 11.sp
          )
          Text(
            text = "Zero Telemetry · Calm Mindful Architecture",
            color = TextMuted.copy(alpha = 0.7f),
            fontSize = 10.sp
          )
        }
        Spacer(modifier = Modifier.height(70.dp))
      }
    }
  }

  if (showExportDialog) {
    AlertDialog(
      onDismissRequest = { showExportDialog = false },
      title = { Text("Audit Log Export", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
      text = {
        Text("Your 7-day deliberate pause records (89 gate intercepts, 42m reclaimed) have been formatted as a private JSON dump stored in your local app sandbox.", color = TextSecondary, fontSize = 13.sp)
      },
      confirmButton = {
        TextButton(onClick = { showExportDialog = false }) {
          Text("Done", color = PrimaryIndigo)
        }
      },
      containerColor = SurfaceContainerHigh
    )
  }

  if (showResetDialog) {
    AlertDialog(
      onDismissRequest = { showResetDialog = false },
      title = { Text("Reset Activity Log?", color = TextPrimary, fontWeight = FontWeight.SemiBold) },
      text = {
        Text("This clears your weekly gate intercepts and reclaimed minutes metrics. Your app protection settings will be preserved.", color = TextSecondary, fontSize = 13.sp)
      },
      confirmButton = {
        TextButton(onClick = { showResetDialog = false }) {
          Text("Clear", color = PrimaryIndigo)
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetDialog = false }) {
          Text("Cancel", color = TextMuted)
        }
      },
      containerColor = SurfaceContainerHigh
    )
  }
}
