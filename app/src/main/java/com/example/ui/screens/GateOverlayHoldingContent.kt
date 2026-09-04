package com.example.ui.screens

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun GateOverlayHoldingContent(
  appName: String = "Instagram",
  targetHoldSeconds: Float = 5.0f,
  onUnlock: () -> Unit,
  onDismiss: () -> Unit,
  onSwitchToBreathing: (() -> Unit)? = null,
  onSwitchToMath: (() -> Unit)? = null
) {
  val context = LocalContext.current
  var isPressed by remember { mutableStateOf(false) }
  var holdProgressSeconds by remember { mutableFloatStateOf(0f) }
  var isCompleted by remember { mutableStateOf(false) }
  var earlyReleaseWarning by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  // Holding progression loop
  LaunchedEffect(isPressed, isCompleted) {
    if (isPressed && !isCompleted) {
      val startTime = System.currentTimeMillis() - (holdProgressSeconds * 1000).toLong()
      while (isPressed && !isCompleted) {
        val elapsed = (System.currentTimeMillis() - startTime) / 1000f
        holdProgressSeconds = elapsed.coerceAtMost(targetHoldSeconds)
        if (holdProgressSeconds >= targetHoldSeconds) {
          isCompleted = true
          try {
            val vibrator = context.getSystemService(Vibrator::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              vibrator?.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
            }
          } catch (_: Throwable) {}
          break
        }
        delay(20)
      }
    } else if (!isPressed && !isCompleted && holdProgressSeconds > 0f) {
      earlyReleaseWarning = true
      // Smooth decay if released early
      while (holdProgressSeconds > 0f && !isPressed) {
        holdProgressSeconds = (holdProgressSeconds - 0.2f).coerceAtLeast(0f)
        delay(16)
      }
    }
  }

  // Auto-unlock upon completing the hold
  LaunchedEffect(isCompleted) {
    if (isCompleted) {
      delay(700)
      onUnlock()
    }
  }

  val progressFraction = (holdProgressSeconds / targetHoldSeconds).coerceIn(0f, 1f)
  val remainingSeconds = (targetHoldSeconds - holdProgressSeconds).coerceAtLeast(0f)
  val animatedScale by animateFloatAsState(
    targetValue = if (isCompleted) 1.15f else if (isPressed) 1.08f else 1.0f,
    animationSpec = tween(200, easing = FastOutSlowInEasing),
    label = "holding_scale"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Ambient radial glow behind touch area
    Box(
      modifier = Modifier
        .size(420.dp)
        .align(Alignment.Center)
        .background(
          Brush.radialGradient(
            colors = listOf(
              if (isCompleted) CalmMoss.copy(alpha = 0.22f)
              else if (isPressed) PrimaryIndigoGlow.copy(alpha = 0.25f)
              else ClayLock.copy(alpha = 0.12f),
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
      // Top Context Pill & Instructions
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
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = ClayLock,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "$appName · Continuous Hold Gate",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Hold firmly for ${targetHoldSeconds.toInt()}s",
          color = TextPrimary,
          fontSize = 24.sp,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Keep your thumb or finger pressed continuously. Physical tactile contact arrests the twitch-reaction.",
          color = TextSecondary,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          lineHeight = 17.sp,
          modifier = Modifier.padding(horizontal = 12.dp)
        )
      }

      // Center Touch Target Zone
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 24.dp)
      ) {
        Box(
          modifier = Modifier.size(250.dp),
          contentAlignment = Alignment.Center
        ) {
          // Circular Progress Indicator
          CircularProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier.fillMaxSize(),
            color = if (isCompleted) CalmMoss else PrimaryIndigo,
            trackColor = SurfaceContainerHigh,
            strokeWidth = 10.dp
          )

          // Outer pulsating ring
          Box(
            modifier = Modifier
              .size(210.dp)
              .scale(animatedScale)
              .clip(CircleShape)
              .background(
                if (isCompleted) CalmMoss.copy(alpha = 0.15f)
                else if (isPressed) PrimaryIndigo.copy(alpha = 0.15f)
                else SurfaceContainerLow
              )
              .border(
                1.dp,
                if (isCompleted) CalmMoss.copy(alpha = 0.5f)
                else if (isPressed) PrimaryIndigo.copy(alpha = 0.6f)
                else BorderSubtle,
                CircleShape
              )
          )

          // Interactive Touch Target Core
          Box(
            modifier = Modifier
              .size(150.dp)
              .scale(animatedScale)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  if (isCompleted) listOf(CalmMoss.copy(alpha = 0.6f), SurfaceContainerHigh)
                  else if (isPressed) listOf(PrimaryIndigo.copy(alpha = 0.5f), SurfaceContainerHigh)
                  else listOf(SurfaceContainerHigh, SurfaceContainerLow)
                )
              )
              .border(
                2.dp,
                if (isCompleted) CalmMoss else if (isPressed) PrimaryIndigo else BorderHighlight,
                CircleShape
              )
              .testTag("hold_touch_target")
              .pointerInput(isCompleted) {
                if (!isCompleted) {
                  awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    isPressed = true
                    waitForUpOrCancellation()
                    isPressed = false
                  }
                }
              },
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.TouchApp,
                contentDescription = "Hold Target",
                tint = if (isCompleted) CalmMoss else if (isPressed) PrimaryIndigo else TextSecondary,
                modifier = Modifier.size(40.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (isCompleted) "Cleared" else String.format("%.1fs", remainingSeconds),
                color = if (isCompleted) CalmMoss else TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
              Text(
                text = if (isCompleted) "✓ Unlocked" else if (isPressed) "Keep holding..." else "Press & Hold",
                color = if (isCompleted) CalmMoss else if (isPressed) PrimaryIndigo else TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
          text = if (isCompleted) {
            "✓ 5-second mindful pause completed. Prefrontal awareness restored."
          } else if (isPressed) {
            "Holding steady... ${(progressFraction * 100).toInt()}%"
          } else if (earlyReleaseWarning) {
            "Finger lifted early. Please maintain touch for full 5 seconds."
          } else {
            "Rest finger on the pad to begin tactile countdown."
          },
          color = if (isCompleted) CalmMoss else if (isPressed) PrimaryIndigo else if (earlyReleaseWarning) ClayLock else TextSecondary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center
        )
      }

      // Bottom Actions & Alternative Challenges
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        PrimaryGlassButton(
          text = if (isCompleted) "Unlock $appName ✓" else "Hold center pad to unlock $appName",
          enabled = isCompleted,
          onClick = onUnlock,
          modifier = Modifier.testTag("holding_unlock_btn")
        )

        Spacer(modifier = Modifier.height(8.dp))

        GhostGlassButton(
          text = "Not now · Put phone down",
          onClick = onDismiss,
          modifier = Modifier.testTag("holding_dismiss_btn")
        )

        // Instant emergency skip button for test/urgent needs
        if (!isCompleted) {
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Emergency instant unlock (0s pause)",
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier
              .clickable { onUnlock() }
              .padding(4.dp)
          )
        }

        // Alternative challenge switchers
        if (onSwitchToBreathing != null || onSwitchToMath != null) {
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
                Text("Switch to Breathing", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
            if (onSwitchToMath != null) {
              Row(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(SurfaceContainerHigh)
                  .clickable { onSwitchToMath() }
                  .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Switch to Math", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "A conscious pause replaces automatic reaction with intention.",
          color = TextMuted,
          fontSize = 11.sp
        )
      }
    }
  }
}
