package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShutterSpeed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.GlassBase
import com.example.ui.theme.GlassElevated
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
fun OnboardingFramingScreen(
  onContinue: () -> Unit
) {
  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Atmospheric ambient background gradients
    Box(
      modifier = Modifier
        .size(320.dp)
        .align(Alignment.TopCenter)
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
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Stepper Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Step capsule
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(SurfaceContainerHigh.copy(alpha = 0.7f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(9999.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          PulsingBeacon(color = PrimaryIndigo, size = 6.dp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "STEP 1 OF 3",
            color = PrimaryIndigo,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )
        }

        // Stepper dashes
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
          Box(
            modifier = Modifier
              .width(28.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(PrimaryIndigo)
          )
          Box(
            modifier = Modifier
              .width(8.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(SurfaceContainerHighest)
          )
          Box(
            modifier = Modifier
              .width(8.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(SurfaceContainerHighest)
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Beacon Circle & Shield Tag
      Box(
        modifier = Modifier.size(110.dp),
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(PrimaryIndigo.copy(alpha = 0.12f))
        )
        Box(
          modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(SurfaceContainerHigh)
            .border(1.dp, BorderHighlight, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.ShutterSpeed,
            contentDescription = "Intentional Shield",
            tint = PrimaryIndigo,
            modifier = Modifier.size(34.dp)
          )
        }

        // Pill Tag at bottom
        Box(
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .clip(RoundedCornerShape(9999.dp))
            .background(Color(0xFF1E382A))
            .border(1.dp, CalmMoss.copy(alpha = 0.3f), RoundedCornerShape(9999.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(5.dp)
                .background(CalmMoss, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Intentional Shield",
              color = CalmMoss,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "A pause between\nimpulse and open.",
        color = TextPrimary,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        lineHeight = 34.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "Gatekeeper doesn't block you forever or guilt you into quitting. It simply gives your conscious mind a few seconds to decide.",
        color = TextSecondary,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        lineHeight = 22.sp,
        modifier = Modifier.padding(horizontal = 12.dp)
      )

      Spacer(modifier = Modifier.height(26.dp))

      // 3 Information Cards
      FramingInfoCard(
        icon = Icons.Default.Tune,
        iconTint = PrimaryIndigo,
        title = "Two essential permissions",
        badge = "System",
        badgeColor = TextMuted,
        description = "Android requires Accessibility and Overlay access so Gatekeeper can detect when a habit app opens and render a brief, gentle pause."
      )

      Spacer(modifier = Modifier.height(12.dp))

      FramingInfoCard(
        icon = Icons.Default.Shield,
        iconTint = CalmMoss,
        title = "100% On-Device & Private",
        badge = "No Cloud",
        badgeColor = CalmMoss,
        description = "Zero telemetry, no remote servers, and absolutely zero surveillance of personal messages, inputs, or screen content."
      )

      Spacer(modifier = Modifier.height(12.dp))

      FramingInfoCard(
        icon = Icons.Default.Bolt,
        iconTint = TextSecondary,
        title = "Under 90 seconds",
        badge = "Quick",
        badgeColor = TextMuted,
        description = "Two standard Android toggles to grant authority. No account creation, passwords, or complex configuration required."
      )

      Spacer(modifier = Modifier.height(32.dp))

      // CTA Button
      PrimaryGlassButton(
        text = "Continue to Setup",
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        onClick = onContinue,
        modifier = Modifier.testTag("framing_continue_btn")
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Footer
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .size(5.dp)
            .background(CalmMoss, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Single-player private architecture · Local only",
          color = TextMuted,
          fontSize = 11.sp
        )
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun FramingInfoCard(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  title: String,
  badge: String,
  badgeColor: Color,
  description: String
) {
  GlassCard(
    modifier = Modifier.fillMaxWidth(),
    backgroundColor = SurfaceContainerLow.copy(alpha = 0.8f),
    cornerRadius = 16.dp
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(SurfaceContainerHigh)
          .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconTint,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = title,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(badgeColor.copy(alpha = 0.15f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = badge,
              color = badgeColor,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = description,
          color = TextSecondary,
          fontSize = 12.sp,
          lineHeight = 17.sp
        )
      }
    }
  }
}
