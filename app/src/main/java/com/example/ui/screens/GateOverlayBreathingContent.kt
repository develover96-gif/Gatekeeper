package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun GateOverlayBreathingContent(
  appName: String = "Instagram",
  totalSeconds: Int = 60, // 60-second guided breathing cycle
  onUnlock: () -> Unit,
  onDismiss: () -> Unit,
  onSwitchToSteps: (() -> Unit)? = null,
  onSwitchToMath: (() -> Unit)? = null
) {
  var remainingSeconds by remember { mutableIntStateOf(totalSeconds) }
  var isCompleted by remember { mutableStateOf(false) }

  // 14-second box-breathing cycle: Inhale 4s -> Hold 4s -> Exhale 4s -> Rest 2s
  val cycleDuration = 14
  val cycleSecondsElapsed = (totalSeconds - remainingSeconds) % cycleDuration
  val (phaseTitle, phaseInstruction, targetScale) = when (cycleSecondsElapsed) {
    in 0..3 -> Triple("Inhale gently...", "Breathe in deeply through your nose", 1.25f)
    in 4..7 -> Triple("Hold softly...", "Maintain quiet stillness at the peak", 1.25f)
    in 8..11 -> Triple("Exhale slowly...", "Release all tension through your mouth", 0.82f)
    else -> Triple("Rest and center...", "Notice the calm in your nervous system", 0.82f)
  }

  // Timer loop: 1 second ticks
  LaunchedEffect(isCompleted) {
    while (remainingSeconds > 0 && !isCompleted) {
      delay(1000)
      remainingSeconds--
      if (remainingSeconds <= 0) {
        isCompleted = true
      }
    }
  }

  // Auto-unlock with a gentle moment of celebration upon completing 60 seconds
  LaunchedEffect(isCompleted) {
    if (isCompleted) {
      delay(1000)
      onUnlock()
    }
  }

  // Smooth easing synchronized with cycle
  val animatedBreathScale by androidx.compose.animation.core.animateFloatAsState(
    targetValue = targetScale,
    animationSpec = tween(
      durationMillis = if (cycleSecondsElapsed in 12..13) 1500 else 3800,
      easing = FastOutSlowInEasing
    ),
    label = "synced_breath_scale"
  )

  val infiniteTransition = rememberInfiniteTransition(label = "ambient_glow")
  val ambientShimmer by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(2500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "ambient_shimmer"
  )

  val minutes = remainingSeconds / 60
  val seconds = remainingSeconds % 60
  val timerString = String.format("%02d:%02d", minutes, seconds)
  val progress = (totalSeconds - remainingSeconds).toFloat() / totalSeconds.toFloat()

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
              CalmMoss.copy(alpha = 0.14f),
              Color.Transparent
            )
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
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
          Icon(Icons.Default.Lock, contentDescription = null, tint = ClayLock, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "$appName · 1-Minute Mindful Breath",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Take a deliberate pause",
          color = TextPrimary,
          fontSize = 24.sp,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Simple guidance: 1 minute to reset your nervous system. Inhale deeply, hold gently, and exhale slowly.",
          color = TextSecondary,
          fontSize = 12.sp,
          textAlign = TextAlign.Center,
          lineHeight = 17.sp,
          modifier = Modifier.padding(horizontal = 16.dp)
        )
      }

      // Center Concentric Breathing Orb with 1-Minute Timer
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier.size(260.dp),
          contentAlignment = Alignment.Center
        ) {
          // Outer ripple ring
          Box(
            modifier = Modifier
              .size(240.dp)
              .scale(animatedBreathScale * ambientShimmer * 1.05f)
              .clip(CircleShape)
              .border(1.dp, CalmMoss.copy(alpha = 0.22f), CircleShape)
          )
          // Middle glow ring
          Box(
            modifier = Modifier
              .size(185.dp)
              .scale(animatedBreathScale)
              .clip(CircleShape)
              .background(CalmMoss.copy(alpha = 0.12f))
              .border(1.dp, CalmMoss.copy(alpha = 0.4f), CircleShape)
          )
          // Core orb with timer
          Box(
            modifier = Modifier
              .size(130.dp)
              .scale(animatedBreathScale * 0.95f)
              .clip(CircleShape)
              .background(
                Brush.radialGradient(
                  listOf(
                    CalmMoss.copy(alpha = 0.5f),
                    SurfaceContainerHigh
                  )
                )
              )
              .border(1.dp, BorderHighlight, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.Air,
                contentDescription = null,
                tint = CalmMoss,
                modifier = Modifier.size(28.dp)
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = timerString,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = if (isCompleted) "Mindful pause complete. Prefrontal calm restored." else phaseTitle,
          color = if (isCompleted) CalmMoss else PrimaryIndigo,
          fontSize = 17.sp,
          fontWeight = FontWeight.SemiBold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = if (isCompleted) "Unlocking $appName..." else phaseInstruction,
          color = TextSecondary,
          fontSize = 12.sp,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Progress bar for the 60-second exercise
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .width(200.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp)),
          color = CalmMoss,
          trackColor = SurfaceContainerHigh
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = if (isCompleted) "60s cycle completed (100%)" else "${(progress * 100).toInt()}% of 60-second cycle",
          color = TextMuted,
          fontSize = 11.sp
        )
      }

      // Actions at bottom
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        PrimaryGlassButton(
          text = if (isCompleted) "Unlock $appName ✓" else "Breathe to unlock (${remainingSeconds}s remaining)",
          enabled = isCompleted,
          onClick = onUnlock,
          modifier = Modifier.testTag("breathing_unlock_btn")
        )

        Spacer(modifier = Modifier.height(8.dp))

        GhostGlassButton(
          text = "Not now · Put phone down",
          onClick = onDismiss,
          modifier = Modifier.testTag("breathing_dismiss_btn")
        )

        // Instant emergency skip button for test/urgent needs
        if (!isCompleted) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Emergency skip (0m pause)",
            color = TextMuted,
            fontSize = 11.sp,
            modifier = Modifier
              .clickable { onUnlock() }
              .padding(4.dp)
          )
        }

        // Alternative challenge switchers
        if (onSwitchToSteps != null || onSwitchToMath != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
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
                Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = CalmMoss, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Switch to Steps", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
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
                Icon(Icons.Default.Air, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Switch to Math", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "A pause is not a wall, it is a moment to decide.",
          color = TextMuted,
          fontSize = 11.sp
        )
      }
    }
  }
}
