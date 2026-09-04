package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MathChallengeConfig
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassSwitch
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.OnPrimaryIndigo
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoGlow
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun MathChallengeSettingsScreen(
  currentConfig: MathChallengeConfig,
  onSaveConfig: (MathChallengeConfig) -> Unit,
  onBack: () -> Unit
) {
  var tier by remember { mutableStateOf(currentConfig.difficultyTier) }
  var predefinedLevel by remember { mutableStateOf(currentConfig.predefinedDifficultyLevel) }
  var problemType by remember { mutableStateOf(currentConfig.problemSolvingType) }
  var randomize by remember { mutableStateOf(currentConfig.randomizeOnFailedEntry) }
  var timeout by remember { mutableStateOf(currentConfig.inputTimeoutCountdown) }
  var escalate by remember { mutableStateOf(currentConfig.progressiveEscalation) }
  var opAddSub by remember { mutableStateOf(currentConfig.permittedAdditionSubtraction) }
  var opMult by remember { mutableStateOf(currentConfig.permittedMultiplication) }
  var opSeq by remember { mutableStateOf(currentConfig.permittedSequencePatterns) }
  var overrideCode by remember { mutableStateOf(currentConfig.emergencyOverrideCode) }

  var simAnswer by remember { mutableStateOf("") }
  var isSimSolved by remember { mutableStateOf(false) }
  var savedConfirmation by remember { mutableStateOf(false) }

  val coroutineScope = rememberCoroutineScope()
  val scrollState = rememberScrollState()

  val simFormula = when (predefinedLevel) {
    1 -> "7 + 8"
    3 -> "(14 × 6) - 15"
    4 -> "Sequence: 3, 7, 15, ?"
    else -> "17 + 28"
  }
  val targetAnswer = when (predefinedLevel) {
    1 -> "15"
    3 -> "69"
    4 -> "31"
    else -> "45"
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      // Top Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.size(36.dp)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = TextSecondary
          )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "FRICTION GATE 02",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )
          Text(
            text = "Math Calibration",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        TextButton(
          onClick = {
            val updated = MathChallengeConfig(
              difficultyTier = tier,
              randomizeOnFailedEntry = randomize,
              inputTimeoutCountdown = timeout,
              progressiveEscalation = escalate,
              permittedAdditionSubtraction = opAddSub,
              permittedMultiplication = opMult,
              permittedSequencePatterns = opSeq,
              emergencyOverrideCode = overrideCode,
              predefinedDifficultyLevel = predefinedLevel,
              problemSolvingType = problemType
            )
            onSaveConfig(updated)
            savedConfirmation = true
          }
        ) {
          Text(
            text = if (savedConfirmation) "Saved ✓" else "Apply",
            color = if (savedConfirmation) CalmMoss else PrimaryIndigo,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Live Friction Simulation Box (Matching mockup 2)
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow.copy(alpha = 0.9f),
        cornerRadius = 20.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              PulsingBeacon(color = PrimaryIndigo, size = 6.dp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "LIVE FRICTION SIMULATION",
                color = PrimaryIndigo,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.08.sp
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(9999.dp))
                .background(SurfaceContainerHigh)
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text("Active Preview", color = TextSecondary, fontSize = 10.sp)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Timer & Target
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            Column {
              Text(
                text = "00:07.4",
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text("Calibrated solve latency", color = TextMuted, fontSize = 11.sp)
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(PrimaryIndigo.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text("Target: 6–10s", color = PrimaryIndigo, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Formula Display Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(SurfaceContainer)
              .border(1.dp, if (isSimSolved) CalmMoss else BorderSubtle, RoundedCornerShape(12.dp))
              .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Text(
                text = "$simFormula = ",
                color = TextSecondary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = if (simAnswer.isEmpty()) "?" else simAnswer,
                color = if (isSimSolved) CalmMoss else PrimaryIndigo,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
              )
              if (!isSimSolved) {
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.size(2.dp, 20.dp).background(PrimaryIndigo))
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Linear Progress
          LinearProgressIndicator(
            progress = { if (isSimSolved) 1f else 0.45f },
            modifier = Modifier
              .fillMaxWidth()
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp)),
            color = if (isSimSolved) CalmMoss else PrimaryIndigo,
            trackColor = SurfaceContainerHighest
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Mini Dialpad
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf("1", "2", "3", "4", "5", "6").forEach { digit ->
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(38.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                  .clickable {
                    simAnswer += digit
                    if (simAnswer == targetAnswer) {
                      isSimSolved = true
                    }
                  },
                contentAlignment = Alignment.Center
              ) {
                Text(text = digit, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (isSimSolved) "✓ Correct. Gate cleared." else "Deliberate math forces prefrontal pause.",
              color = if (isSimSolved) CalmMoss else TextMuted,
              fontSize = 11.sp
            )
            if (simAnswer.isNotEmpty()) {
              Text(
                text = "Reset",
                color = PrimaryIndigo,
                fontSize = 11.sp,
                modifier = Modifier.clickable {
                  simAnswer = ""
                  isSimSolved = false
                }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Cognitive Load Tier
      Text(
        text = "COGNITIVE LOAD TIER",
        color = TextMuted,
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.08.sp
      )

      Spacer(modifier = Modifier.height(8.dp))

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TierCard(
          title = "Level 1: Gentle",
          formulaExample = "Single/dual-digit addition & subtraction (e.g. 7 + 8)",
          estimatedTime = "~3s pause",
          selected = predefinedLevel == 1,
          onClick = {
            predefinedLevel = 1
            tier = "GENTLE"
            simAnswer = ""
            isSimSolved = false
          }
        )

        TierCard(
          title = "Level 2: Standard (Recommended)",
          formulaExample = "Two-digit addition & subtraction (e.g. 17 + 28)",
          estimatedTime = "~7s pause",
          selected = predefinedLevel == 2,
          onClick = {
            predefinedLevel = 2
            tier = "STANDARD"
            simAnswer = ""
            isSimSolved = false
          }
        )

        TierCard(
          title = "Level 3: Fortified",
          formulaExample = "Multi-step arithmetic with parentheses (e.g. (14 × 6) - 15)",
          estimatedTime = "~12s pause",
          selected = predefinedLevel == 3,
          onClick = {
            predefinedLevel = 3
            tier = "ADVANCED"
            simAnswer = ""
            isSimSolved = false
          }
        )

        TierCard(
          title = "Level 4: Problem Solving & Logic",
          formulaExample = "Sequences, balance equations & logic riddles (e.g. 3, 7, 15, ?)",
          estimatedTime = "~15s pause",
          selected = predefinedLevel == 4,
          onClick = {
            predefinedLevel = 4
            tier = "PROBLEM_SOLVING"
            simAnswer = ""
            isSimSolved = false
          }
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Dynamic Rules
      Text(
        text = "DYNAMIC RULES",
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
          SettingSwitchRow(
            title = "Randomize on failed entry",
            description = "Changes equation immediately if mistyped to prevent spamming",
            checked = randomize,
            onChecked = { randomize = it }
          )

          Spacer(modifier = Modifier.height(12.dp))
          Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
          Spacer(modifier = Modifier.height(12.dp))

          SettingSwitchRow(
            title = "Input timeout countdown",
            description = "Clears problem if no keypad input detected within 12 seconds",
            checked = timeout,
            onChecked = { timeout = it }
          )

          Spacer(modifier = Modifier.height(12.dp))
          Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderSubtle))
          Spacer(modifier = Modifier.height(12.dp))

          SettingSwitchRow(
            title = "Progressive escalation",
            description = "+2s calculation complexity if same app re-opened repeatedly",
            checked = escalate,
            onChecked = { escalate = it }
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Permitted Operations
      Text(
        text = "PERMITTED OPERATIONS",
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
          OperationCheckboxRow(
            title = "Addition & Subtraction",
            checked = opAddSub,
            onChecked = { opAddSub = it }
          )
          Spacer(modifier = Modifier.height(8.dp))
          OperationCheckboxRow(
            title = "Multiplication Tables (2–12)",
            checked = opMult,
            onChecked = { opMult = it }
          )
          Spacer(modifier = Modifier.height(8.dp))
          OperationCheckboxRow(
            title = "Sequence Pattern Completion (e.g. 3, 7, 11, ?)",
            checked = opSeq,
            onChecked = { opSeq = it }
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Emergency Override
      Text(
        text = "EMERGENCY OVERRIDE",
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
          Column {
            Text(
              text = "Emergency 4-Digit Passcode",
              color = TextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Code: •••• (Instant bypass if urgent)",
              color = TextSecondary,
              fontSize = 11.sp
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(SurfaceContainerHigh)
              .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text("Enforced Active", color = CalmMoss, fontSize = 11.sp, fontWeight = FontWeight.Medium)
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      PrimaryGlassButton(
        text = "Apply Calibration",
        onClick = {
          val updated = MathChallengeConfig(
            difficultyTier = tier,
            randomizeOnFailedEntry = randomize,
            inputTimeoutCountdown = timeout,
            progressiveEscalation = escalate,
            permittedAdditionSubtraction = opAddSub,
            permittedMultiplication = opMult,
            permittedSequencePatterns = opSeq,
            emergencyOverrideCode = overrideCode,
            predefinedDifficultyLevel = predefinedLevel,
            problemSolvingType = problemType
          )
          onSaveConfig(updated)
          onBack()
        },
        modifier = Modifier.testTag("apply_math_calibration_btn")
      )

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun TierCard(
  title: String,
  formulaExample: String,
  estimatedTime: String,
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
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = title,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = estimatedTime,
            color = if (selected) PrimaryIndigo else TextMuted,
            fontSize = 11.sp
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = formulaExample,
          color = TextSecondary,
          fontSize = 12.sp
        )
      }

      RadioButton(
        selected = selected,
        onClick = onClick,
        colors = RadioButtonDefaults.colors(
          selectedColor = PrimaryIndigo,
          unselectedColor = TextMuted
        )
      )
    }
  }
}

@Composable
private fun SettingSwitchRow(
  title: String,
  description: String,
  checked: Boolean,
  onChecked: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        color = TextPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = description,
        color = TextSecondary,
        fontSize = 11.sp,
        lineHeight = 15.sp
      )
    }
    Spacer(modifier = Modifier.width(12.dp))
    GlassSwitch(checked = checked, onCheckedChange = onChecked)
  }
}

@Composable
private fun OperationCheckboxRow(
  title: String,
  checked: Boolean,
  onChecked: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onChecked(!checked) }
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Checkbox(
      checked = checked,
      onCheckedChange = onChecked,
      colors = CheckboxDefaults.colors(
        checkedColor = PrimaryIndigo,
        uncheckedColor = TextMuted,
        checkmarkColor = OnPrimaryIndigo
      )
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = title,
      color = TextPrimary,
      fontSize = 13.sp
    )
  }
}
