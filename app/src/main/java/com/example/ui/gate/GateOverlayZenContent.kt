package com.example.ui.gate

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GhostGlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GateOverlayZenContent(
  appName: String = "Instagram",
  remainingMinutes: Int = 25,
  onReturnHome: () -> Unit,
  onEmergencyOverride: (() -> Unit)? = null
) {
  val infiniteTransition = rememberInfiniteTransition(label = "zen_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
      .testTag("zen_overlay_container")
  ) {
    // Ambient calm background gradient
    Box(
      modifier = Modifier
        .fillMaxSize()
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
        .padding(horizontal = 24.dp, vertical = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top status pill
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(9999.dp))
          .background(SurfaceContainerHigh)
          .border(1.dp, CalmMoss.copy(alpha = 0.4f), RoundedCornerShape(9999.dp))
          .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.SelfImprovement,
          contentDescription = null,
          tint = CalmMoss,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "ZEN MODE ACTIVE",
          color = CalmMoss,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold,
          letterSpacing = 0.08.sp
        )
      }

      // Center Zen Focus Graphics & Messaging
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        // Concentric calm pulsating circles
        Box(
          modifier = Modifier
            .size(160.dp)
            .scale(pulseScale),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .size(160.dp)
              .clip(CircleShape)
              .background(CalmMoss.copy(alpha = 0.08f))
              .border(1.dp, CalmMoss.copy(alpha = 0.25f), CircleShape)
          )
          Box(
            modifier = Modifier
              .size(110.dp)
              .clip(CircleShape)
              .background(CalmMoss.copy(alpha = 0.16f))
              .border(1.dp, CalmMoss.copy(alpha = 0.4f), CircleShape)
          )
          Icon(
            imageVector = Icons.Default.SelfImprovement,
            contentDescription = null,
            tint = CalmMoss,
            modifier = Modifier.size(52.dp)
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
          text = "Deep Focus Sanctuary",
          color = TextPrimary,
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "All friction challenges are temporarily disabled.\nAccess to $appName is completely blocked until your Zen session ends.",
          color = TextSecondary,
          fontSize = 14.sp,
          lineHeight = 20.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Countdown Card
        GlassCard(
          modifier = Modifier.fillMaxWidth(0.85f),
          backgroundColor = SurfaceContainerLow.copy(alpha = 0.85f),
          borderColor = CalmMoss.copy(alpha = 0.35f),
          cornerRadius = 16.dp
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "TIME REMAINING IN ZEN MODE",
              color = TextMuted,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              letterSpacing = 0.06.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "${remainingMinutes.coerceAtLeast(1)} Minutes",
              color = CalmMoss,
              fontSize = 26.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Take a breath and return to the physical world.",
              color = TextSecondary,
              fontSize = 11.sp
            )
          }
        }
      }

      // Bottom Actions
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        PrimaryGlassButton(
          text = "Step Away (Return Home)",
          icon = Icons.AutoMirrored.Filled.ArrowBack,
          onClick = onReturnHome,
          modifier = Modifier.testTag("zen_return_home_btn")
        )

        if (onEmergencyOverride != null) {
          GhostGlassButton(
            text = "Emergency Passcode Override",
            icon = Icons.Default.Lock,
            onClick = onEmergencyOverride
          )
        }
      }
    }
  }
}
