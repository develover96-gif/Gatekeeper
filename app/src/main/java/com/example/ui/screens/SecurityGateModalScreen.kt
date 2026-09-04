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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryTags
import com.example.data.model.GatedAppEntity
import com.example.ui.components.GhostGlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSwitch
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SecurityGateModalScreen(
  app: GatedAppEntity,
  onSave: (GatedAppEntity) -> Unit,
  onDelete: (String) -> Unit,
  onTestGate: (GatedAppEntity) -> Unit,
  onClose: () -> Unit
) {
  var isGated by remember { mutableStateOf(app.isGated) }
  var selectedCategory by remember { mutableStateOf(CategoryTags.normalize(app.category)) }
  var challengeType by remember { mutableStateOf(app.challengeType) }
  var graceMinutes by remember { mutableIntStateOf(app.graceWindowMinutes) }
  var sessionMinutes by remember { mutableIntStateOf(if (app.sessionLimitMinutes > 0) app.sessionLimitMinutes else 10) }
  var dailyLimitMinutes by remember { mutableIntStateOf(app.dailyLimitMinutes) }
  var isDistracting by remember { mutableStateOf(app.isDistracting) }
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 600.dp)
        .align(Alignment.TopCenter)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      // Top Navigation
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onClose, modifier = Modifier.size(36.dp)) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextSecondary)
        }

        Text(
          text = "App Gate Details",
          color = TextPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold
        )

        TextButton(
          onClick = {
            onSave(
              app.copy(
                isGated = isGated,
                category = selectedCategory,
                challengeType = challengeType,
                graceWindowMinutes = graceMinutes,
                sessionLimitMinutes = sessionMinutes,
                dailyLimitMinutes = dailyLimitMinutes,
                isDistracting = isDistracting
              )
            )
            onClose()
          }
        ) {
          Text("Done", color = PrimaryIndigo, fontWeight = FontWeight.SemiBold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // App Identity Card
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow.copy(alpha = 0.9f),
        cornerRadius = 18.dp
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
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(PrimaryIndigo.copy(alpha = 0.18f))
                .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
              contentAlignment = Alignment.Center
            ) {
              val icon = when (app.iconType) {
                "camera" -> Icons.Default.CameraAlt
                "play" -> Icons.Default.PlayArrow
                "at" -> Icons.Default.AlternateEmail
                "forum" -> Icons.Default.Forum
                "video" -> Icons.Default.SmartDisplay
                else -> Icons.Default.Language
              }
              Icon(imageVector = icon, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(app.appName, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
              Text("$selectedCategory • ${if (isGated) "Active Shield" else "Protection Paused"}", color = TextSecondary, fontSize = 12.sp)
            }
          }

          GlassSwitch(checked = isGated, onCheckedChange = { isGated = it })
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Weekly Metrics Card
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 16.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${app.gatesClearedCount}", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Gates Cleared", color = TextMuted, fontSize = 10.sp)
          }
          Box(modifier = Modifier.size(1.dp, 28.dp).background(BorderSubtle))
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("82%", color = PrimaryIndigo, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Mindful Opens", color = TextMuted, fontSize = 10.sp)
          }
          Box(modifier = Modifier.size(1.dp, 28.dp).background(BorderSubtle))
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${app.skipsCount}", color = CalmMoss, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Deflected Away", color = TextMuted, fontSize = 10.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Category Tag Selector Section
      Text(
        text = "CATEGORY TAG",
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.08.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CategoryTags.STANDARD_CATEGORIES.take(3).forEach { cat ->
          val isSelected = selectedCategory == cat
          val catColor = Color(CategoryTags.getCategoryAccentHex(cat))
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) catColor.copy(alpha = 0.22f) else SurfaceContainerLow)
              .border(1.dp, if (isSelected) catColor else BorderSubtle, RoundedCornerShape(10.dp))
              .clickable { selectedCategory = cat }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = cat,
              color = if (isSelected) catColor else TextSecondary,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CategoryTags.STANDARD_CATEGORIES.drop(3).forEach { cat ->
          val isSelected = selectedCategory == cat
          val catColor = Color(CategoryTags.getCategoryAccentHex(cat))
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) catColor.copy(alpha = 0.22f) else SurfaceContainerLow)
              .border(1.dp, if (isSelected) catColor else BorderSubtle, RoundedCornerShape(10.dp))
              .clickable { selectedCategory = cat }
              .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = cat,
              color = if (isSelected) catColor else TextSecondary,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Challenge Type Selector
      Text(
        text = "FRICTION CHALLENGE TYPE",
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.08.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ChallengeTypeCard(
          icon = Icons.Default.Calculate,
          title = "Math Challenge",
          description = "Deliberate calculation engages conscious prefrontal cortex.",
          selected = challengeType == "MATH",
          onClick = { challengeType = "MATH" }
        )

        ChallengeTypeCard(
          icon = Icons.Default.Air,
          title = "Mindful Breathing",
          description = "Concentric breathing rings pace 1-minute conscious deep breathing.",
          selected = challengeType == "BREATHING",
          onClick = { challengeType = "BREATHING" }
        )

        ChallengeTypeCard(
          icon = Icons.Default.DirectionsWalk,
          title = "Physical Step Gate",
          description = "Take 20 deliberate steps. Hardware pedometer breaks physical inertia.",
          selected = challengeType == "STEPS",
          onClick = { challengeType = "STEPS" }
        )

        ChallengeTypeCard(
          icon = Icons.Default.TouchApp,
          title = "Holding Pause",
          description = "Unlocks after holding down continuous tactile touch for 5 seconds.",
          selected = challengeType == "HOLDING",
          onClick = { challengeType = "HOLDING" }
        )

        ChallengeTypeCard(
          icon = Icons.Default.EditNote,
          title = "Reflective Prompt",
          description = "Presents \"What is your specific intention for this session?\"",
          selected = challengeType == "PROMPT",
          onClick = { challengeType = "PROMPT" }
        )

        ChallengeTypeCard(
          icon = Icons.Default.Psychology,
          title = "Problem Solving Challenge",
          description = "Sequences, equation balancing & logic deduction challenges.",
          selected = challengeType == "PROBLEM_SOLVING",
          onClick = { challengeType = "PROBLEM_SOLVING" }
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Session Limit
      Text(
        text = "SESSION LIMIT PER UNLOCK",
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.08.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Duration allowed before the gate automatically re-locks:",
            color = TextSecondary,
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf(5, 10, 15, 20, 30).forEach { mins ->
              val isSelected = sessionMinutes == mins
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(38.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) PrimaryIndigo else SurfaceContainerHigh)
                  .clickable { sessionMinutes = mins },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${mins}m",
                  color = if (isSelected) CanvasBase else TextPrimary,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Daily Limit
      Text(
        text = "DAILY USAGE LIMIT",
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.08.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Total daily duration before gate triggers for every subsequent launch:",
            color = TextSecondary,
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf(0 to "Off", 15 to "15m", 30 to "30m", 45 to "45m", 60 to "60m", 90 to "90m").forEach { (mins, label) ->
              val isSelected = dailyLimitMinutes == mins
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(38.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) PrimaryIndigo else SurfaceContainerHigh)
                  .clickable { dailyLimitMinutes = mins },
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

      Spacer(modifier = Modifier.height(20.dp))

      // Zen Mode Designation
      Text(
        text = "ZEN MODE FOCUS BEHAVIOR",
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.08.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 16.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Mark as Distracting App",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Completely blocks launch without challenges during active Zen Mode sessions.",
              color = TextSecondary,
              fontSize = 11.sp,
              lineHeight = 16.sp
            )
          }

          GlassSwitch(
            checked = isDistracting,
            onCheckedChange = { isDistracting = it },
            modifier = Modifier.testTag("distracting_switch")
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Grace Window Duration
      Text(
        text = "GRACE WINDOW DURATION",
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.08.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 16.dp
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "Time before friction triggers again after clearing:",
            color = TextSecondary,
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf(5, 15, 30, 60).forEach { mins ->
              val isSelected = graceMinutes == mins
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(38.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(if (isSelected) PrimaryIndigo else SurfaceContainerHigh)
                  .clickable { graceMinutes = mins },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${mins}m",
                  color = if (isSelected) CanvasBase else TextPrimary,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Test Gate Action
      PrimaryGlassButton(
        text = "Test Gate Challenge Now",
        icon = Icons.Default.Shield,
        onClick = {
          onTestGate(
            app.copy(
              isGated = isGated,
              challengeType = challengeType,
              graceWindowMinutes = graceMinutes,
              sessionLimitMinutes = sessionMinutes,
              dailyLimitMinutes = dailyLimitMinutes,
              isDistracting = isDistracting
            )
          )
        },
        modifier = Modifier.testTag("test_gate_challenge_btn")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Remove App Action
      GhostGlassButton(
        text = "Remove from Gated Apps",
        icon = Icons.Default.Delete,
        color = ErrorRed,
        onClick = {
          onDelete(app.packageName)
          onClose()
        }
      )

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun ChallengeTypeCard(
  icon: ImageVector,
  title: String,
  description: String,
  selected: Boolean,
  onClick: () -> Unit
) {
  GlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    backgroundColor = if (selected) SurfaceContainerHigh else SurfaceContainerLow,
    borderColor = if (selected) PrimaryIndigo else BorderSubtle,
    cornerRadius = 14.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(if (selected) PrimaryIndigo.copy(alpha = 0.2f) else SurfaceContainer)
          .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = if (selected) PrimaryIndigo else TextMuted,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(description, color = TextSecondary, fontSize = 11.sp, lineHeight = 15.sp)
      }

      if (selected) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Selected",
          tint = PrimaryIndigo,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}
