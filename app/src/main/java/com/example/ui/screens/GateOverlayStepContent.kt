package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GhostGlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.ClayLock
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoGlow
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.StepGateViewModel

@Composable
fun GateOverlayStepContent(
  appName: String = "Instagram",
  targetSteps: Int = 20,
  onUnlock: () -> Unit,
  onDismiss: () -> Unit,
  onSwitchToBreathing: () -> Unit,
  onSwitchToMath: () -> Unit
) {
  val context = LocalContext.current
  val viewModel = remember { StepGateViewModel(context, targetSteps) }
  val stepsSoFar by viewModel.stepsSoFar.collectAsState()
  val hasSensor by viewModel.hasHardwareSensor.collectAsState()
  val isCleared by viewModel.isCleared.collectAsState()
  val scrollState = rememberScrollState()

  val progress = (stepsSoFar.toFloat() / targetSteps.toFloat()).coerceIn(0f, 1f)
  val animatedProgress by animateFloatAsState(targetValue = progress, label = "step_progress")

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Ambient aura
    Box(
      modifier = Modifier
        .size(420.dp)
        .align(Alignment.Center)
        .background(
          Brush.radialGradient(
            colors = listOf(
              PrimaryIndigoGlow.copy(alpha = 0.14f),
              Color.Transparent
            )
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top Context Pill
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(SurfaceContainerHigh.copy(alpha = 0.85f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(9999.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, tint = ClayLock, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "$appName · Physical Step Gate",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Take $targetSteps deliberate steps",
          color = TextPrimary,
          fontSize = 24.sp,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Step away from your desk or bed. Real physical movement breaks unconscious habit loops.",
          color = TextSecondary,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          lineHeight = 17.sp,
          modifier = Modifier.padding(horizontal = 12.dp)
        )
      }

      // Center Step Progress Ring
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 20.dp)
      ) {
        Box(
          modifier = Modifier.size(230.dp),
          contentAlignment = Alignment.Center
        ) {
          // Circular Progress indicator
          CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = if (isCleared) CalmMoss else PrimaryIndigo,
            trackColor = SurfaceContainerHigh,
            strokeWidth = 12.dp
          )

          // Inner Glass Core
          Box(
            modifier = Modifier
              .size(170.dp)
              .clip(CircleShape)
              .background(SurfaceContainerLow)
              .border(1.dp, BorderSubtle, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = if (isCleared) Icons.Default.Check else Icons.Default.DirectionsWalk,
                contentDescription = null,
                tint = if (isCleared) CalmMoss else PrimaryIndigo,
                modifier = Modifier.size(36.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "$stepsSoFar",
                color = TextPrimary,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = "of $targetSteps steps",
                color = TextMuted,
                fontSize = 12.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hardware sensor or emulator simulation note
        if (!hasSensor) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(SurfaceContainerHigh)
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = "No hardware pedometer · Tap button below to simulate steps",
              color = TextMuted,
              fontSize = 11.sp
            )
          }
        } else {
          Text(
            text = if (isCleared) "✓ $targetSteps steps reached! Mindful friction cleared." else "Live hardware step counter active",
            color = if (isCleared) CalmMoss else PrimaryIndigo,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Manual Step Pulse Button (useful for testing or indoor stationary mode)
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceContainerHigh)
            .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
            .clickable { viewModel.recordManualStep() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.TouchApp, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "+1 Step (Test / Mobility Pulse)",
            color = TextSecondary,
            fontSize = 11.sp
          )
        }
      }

      // Bottom Actions & Accessibility Fallbacks
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        PrimaryGlassButton(
          text = if (isCleared) "Unlock $appName" else "Unlock $appName ($stepsSoFar/$targetSteps steps)",
          enabled = isCleared,
          onClick = onUnlock,
          modifier = Modifier.testTag("step_unlock_btn")
        )

        Spacer(modifier = Modifier.height(10.dp))

        GhostGlassButton(
          text = "Not now · Put phone down",
          onClick = onDismiss,
          modifier = Modifier.testTag("step_dismiss_btn")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Accessibility Fallback options (Crucial requirement from prompt)
        GlassCard(
          modifier = Modifier.fillMaxWidth(),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
          cornerRadius = 14.dp
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "ACCESSIBILITY & INDOOR ALTERNATIVE",
              color = TextMuted,
              fontSize = 9.sp,
              fontWeight = FontWeight.SemiBold,
              letterSpacing = 0.08.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Row(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .clickable { onSwitchToBreathing() }
                  .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Air, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Switch to Breathing", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }

              Row(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .clickable { onSwitchToMath() }
                  .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Switch to Math", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
      }
    }
  }
}
