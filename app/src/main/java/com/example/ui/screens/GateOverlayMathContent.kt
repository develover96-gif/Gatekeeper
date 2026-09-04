package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MathProblem
import com.example.data.model.ProblemSolvingChallengeGenerator
import com.example.ui.components.GhostGlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.ClayLock
import com.example.ui.theme.ErrorRed
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
fun GateOverlayMathContent(
  appName: String = "Instagram",
  equation: String? = null,
  solution: String? = null,
  initialDifficultyLevel: Int = 2, // 1 = Gentle, 2 = Standard, 3 = Advanced, 4 = Problem Solving
  sessionMinutes: Int = 10,
  dailyLimitExceeded: Boolean = false,
  usedMinutesToday: Int = 18,
  dailyLimitMinutes: Int = 30,
  onUnlock: () -> Unit,
  onDismiss: () -> Unit,
  onSwitchToBreathing: (() -> Unit)? = null,
  onSwitchToSteps: (() -> Unit)? = null
) {
  var currentLevel by remember { mutableIntStateOf(initialDifficultyLevel) }
  var currentProblem by remember(currentLevel) {
    mutableStateOf(
      if (equation != null && solution != null && currentLevel == initialDifficultyLevel) {
        MathProblem(
          prompt = equation,
          solution = solution,
          difficultyLabel = "Standard (Level 2)",
          problemType = "ARITHMETIC",
          hint = "Solve the calculation: $equation",
          explanation = "$equation = $solution"
        )
      } else {
        ProblemSolvingChallengeGenerator.generateByLevel(currentLevel)
      }
    )
  }

  var userInput by remember { mutableStateOf("") }
  var isSolved by remember { mutableStateOf(false) }
  var isWrong by remember { mutableStateOf(false) }
  var hintShown by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  fun refreshProblem() {
    currentProblem = ProblemSolvingChallengeGenerator.generateByLevel(currentLevel)
    userInput = ""
    isSolved = false
    isWrong = false
    hintShown = false
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
      .testTag("gate_overlay_math_container")
  ) {
    // Ambient back radial gradient
    Box(
      modifier = Modifier
        .size(380.dp)
        .align(Alignment.Center)
        .background(
          Brush.radialGradient(
            colors = listOf(
              PrimaryIndigoGlow.copy(alpha = 0.16f),
              Color.Transparent
            )
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 18.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top Context Header
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(SurfaceContainerHigh.copy(alpha = 0.85f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(9999.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = ClayLock,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "$appName · Mindful Friction Gate",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = if (currentLevel == 4) "Solve Problem to Proceed" else "Calculate to Proceed",
          color = TextPrimary,
          fontSize = 22.sp,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Engaging deliberate prefrontal focus before app launch.",
          color = TextSecondary,
          fontSize = 12.sp,
          textAlign = TextAlign.Center
        )

        // Status Badges (Session limit & Daily limit)
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Session allowance badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(PrimaryIndigo.copy(alpha = 0.15f))
              .border(1.dp, PrimaryIndigoGlow.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.HourglassTop, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(12.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Grants ${sessionMinutes}m Session", color = PrimaryIndigo, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
          }

          // Daily limit warning badge if reached
          if (dailyLimitExceeded) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFFFA726).copy(alpha = 0.18f))
                .border(1.dp, Color(0xFFFFA726).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFFA726), modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Daily Limit ($dailyLimitMinutes m) Met", color = Color(0xFFFFA726), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
              }
            }
          }
        }
      }

      // Predefined Difficulty Level Selector Pill
      Spacer(modifier = Modifier.height(14.dp))
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow.copy(alpha = 0.8f),
        cornerRadius = 14.dp
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "DIFFICULTY LEVEL",
              color = TextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              letterSpacing = 0.08.sp
            )
            IconButton(
              onClick = { refreshProblem() },
              modifier = Modifier.size(24.dp)
            ) {
              Icon(Icons.Default.Refresh, contentDescription = "New Problem", tint = TextSecondary, modifier = Modifier.size(16.dp))
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            val levels = listOf(
              1 to "L1 Gentle",
              2 to "L2 Standard",
              3 to "L3 Fortified",
              4 to "L4 Problem Solving"
            )
            levels.forEach { (lvl, label) ->
              val isSelected = currentLevel == lvl
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) PrimaryIndigo else SurfaceContainerHigh)
                  .clickable {
                    currentLevel = lvl
                    refreshProblem()
                  }
                  .padding(vertical = 7.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = label,
                  color = if (isSelected) Color.White else TextSecondary,
                  fontSize = 10.sp,
                  fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                  maxLines = 1
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Challenge Card
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow.copy(alpha = 0.95f),
        cornerRadius = 20.dp
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Difficulty badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(9999.dp))
              .background(SurfaceContainerHigh)
              .padding(horizontal = 10.dp, vertical = 3.dp)
          ) {
            Text(
              text = currentProblem.difficultyLabel,
              color = PrimaryIndigo,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Prompt or Equation Display
          Text(
            text = if (currentProblem.problemType == "ARITHMETIC") {
              "${currentProblem.prompt} = ?"
            } else {
              currentProblem.prompt
            },
            color = TextPrimary,
            fontSize = if (currentProblem.problemType == "ARITHMETIC") 30.sp else 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = if (currentProblem.problemType == "ARITHMETIC") FontFamily.Monospace else FontFamily.Default,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Answer Display Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(SurfaceContainer)
              .border(
                1.dp,
                if (isSolved) CalmMoss else if (isWrong) Color(0xFFFF6B6B) else BorderHighlight,
                RoundedCornerShape(12.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (userInput.isEmpty()) "Enter answer..." else userInput,
                color = if (userInput.isEmpty()) TextMuted else if (isSolved) CalmMoss else PrimaryIndigo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              if (userInput.isNotEmpty() && !isSolved) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(2.dp, 18.dp).background(PrimaryIndigo))
              }
            }
          }

          if (hintShown) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceContainerHigh)
                .padding(8.dp)
            ) {
              Text(
                text = "Hint: ${currentProblem.hint}",
                color = PrimaryIndigo,
                fontSize = 11.sp,
                lineHeight = 15.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Numeric Keypad Grid (1 to 9, Hint, 0, Backspace)
          Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            val rows = listOf(
              listOf("1", "2", "3"),
              listOf("4", "5", "6"),
              listOf("7", "8", "9"),
              listOf("hint", "0", "back")
            )

            for (row in rows) {
              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                for (key in row) {
                  Box(
                    modifier = Modifier
                      .weight(1f)
                      .height(46.dp)
                      .clip(RoundedCornerShape(10.dp))
                      .background(SurfaceContainerHigh)
                      .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                      .clickable {
                        when (key) {
                          "back" -> {
                            if (userInput.isNotEmpty()) {
                              userInput = userInput.dropLast(1)
                              isSolved = false
                              isWrong = false
                            }
                          }
                          "hint" -> {
                            hintShown = true
                          }
                          else -> {
                            if (userInput.length < 5) {
                              userInput += key
                              isWrong = false
                              if (userInput.trim() == currentProblem.solution.trim()) {
                                isSolved = true
                              }
                            }
                          }
                        }
                      },
                    contentAlignment = Alignment.Center
                  ) {
                    when (key) {
                      "back" -> Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "Delete", tint = TextSecondary, modifier = Modifier.size(18.dp))
                      "hint" -> Icon(Icons.Default.Lightbulb, contentDescription = "Hint", tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                      else -> Text(text = key, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                    }
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = if (isSolved) "✓ Correct. Conscious pause cleared." else "Prefrontal cortex engaged.",
            color = if (isSolved) CalmMoss else TextMuted,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
          )
        }
      }

      // Actions at bottom
      Spacer(modifier = Modifier.height(14.dp))
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        PrimaryGlassButton(
          text = "Unlock $appName (${sessionMinutes}m Session)",
          enabled = isSolved || userInput.trim() == currentProblem.solution.trim(),
          onClick = onUnlock,
          modifier = Modifier.testTag("gate_unlock_btn")
        )

        Spacer(modifier = Modifier.height(10.dp))

        GhostGlassButton(
          text = "Not now, step away",
          onClick = onDismiss,
          modifier = Modifier.testTag("gate_dismiss_btn")
        )

        // Emergency bypass
        if (!isSolved && userInput.trim() != currentProblem.solution.trim()) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Emergency skip (bypass calculation)",
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier
              .clickable { onUnlock() }
              .padding(4.dp)
          )
        }

        // Alternative challenge switchers
        if (onSwitchToBreathing != null || onSwitchToSteps != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            if (onSwitchToBreathing != null) {
              Row(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .clickable { onSwitchToBreathing() }
                  .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Air, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Breathing", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
            if (onSwitchToSteps != null) {
              Row(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .clickable { onSwitchToSteps() }
                  .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.DirectionsWalk, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Steps", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          PulsingBeacon(color = CalmMoss, size = 5.dp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Today: ${usedMinutesToday}m used • ${if (dailyLimitMinutes > 0) "${dailyLimitMinutes}m limit" else "Unlimited"}",
            color = TextMuted,
            fontSize = 11.sp
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
      }
    }
  }
}
