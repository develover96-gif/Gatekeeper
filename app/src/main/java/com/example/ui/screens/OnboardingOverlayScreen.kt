package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GhostGlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.PrimaryGlassButton
import com.example.ui.components.PulsingBeacon
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.GlassBase
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoGlow
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingOverlayScreen(
  onBack: () -> Unit,
  onGranted: () -> Unit,
  onSkip: () -> Unit
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()
  var isOverlayGranted by remember { mutableStateOf(false) }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(CanvasBase)
  ) {
    // Ambient back illumination
    Box(
      modifier = Modifier
        .size(320.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.radialGradient(
            colors = listOf(PrimaryIndigoGlow.copy(alpha = 0.15f), Color.Transparent)
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 600.dp)
        .align(Alignment.TopCenter)
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Stepper Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
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
            text = "STEP 3 OF 3",
            color = PrimaryIndigo,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
          Box(
            modifier = Modifier
              .width(16.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(PrimaryIndigo)
          )
          Box(
            modifier = Modifier
              .width(16.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(PrimaryIndigo)
          )
          Box(
            modifier = Modifier
              .width(28.dp)
              .height(4.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(PrimaryIndigo)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Allow Display Over\nOther Apps",
        color = TextPrimary,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        lineHeight = 30.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "This permission allows Gatekeeper to project the conscious friction barrier over distracting apps before you mindlessly scroll.",
        color = TextSecondary,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        lineHeight = 19.sp,
        modifier = Modifier.padding(horizontal = 10.dp)
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Spatial Diagram / Simulated Intercept Sheet (Matching mockup 4)
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 20.dp
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Mock Underneath App
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(170.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF14151B))
              .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
          ) {
            // Simulated social feed background
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("Feed", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                  Box(modifier = Modifier.size(6.dp).background(TextMuted.copy(alpha = 0.4f), CircleShape))
                  Box(modifier = Modifier.size(6.dp).background(TextMuted.copy(alpha = 0.4f), CircleShape))
                }
              }
              Spacer(modifier = Modifier.height(8.dp))
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(60.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFF22242D))
              )
            }

            // Gatekeeper Overlaid Frosted Sheet
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color(0xD90D0E12))
                .border(1.dp, PrimaryIndigo.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(12.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                  modifier = Modifier
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color(0x33BCC2FF))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  PulsingBeacon(color = PrimaryIndigo, size = 5.dp)
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "MINDFUL BREATH (3s)",
                    color = PrimaryIndigo,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PrimaryIndigo.copy(alpha = 0.2f)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Air,
                    contentDescription = null,
                    tint = PrimaryIndigo,
                    modifier = Modifier.size(24.dp)
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = "Take a deliberate pause",
                  color = TextPrimary,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = "Is this what you meant to open?",
                  color = TextSecondary,
                  fontSize = 10.sp
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Feature explanations
          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Default.Speed,
              contentDescription = null,
              tint = PrimaryIndigo,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Instant Speed Bump",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Presents a friction surface the exact millisecond a distracting habit triggers.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(verticalAlignment = Alignment.Top) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = null,
              tint = CalmMoss,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Seamless Dismiss",
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "Once solved or paused, the barrier clears cleanly and hands full control back to you.",
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // System Navigation Path Breadcrumbs
      GlassCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 14.dp
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text(
            text = "SYSTEM SETTINGS PATH",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.08.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(text = "Settings", color = TextSecondary, fontSize = 11.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
            Text(text = "Apps", color = TextSecondary, fontSize = 11.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
            Text(text = "Display Over Apps", color = TextSecondary, fontSize = 11.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
            Text(text = "Allow", color = PrimaryIndigo, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Grant Button
      PrimaryGlassButton(
        text = "Grant Overlay Permission",
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        onClick = {
          try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
              )
              context.startActivity(intent)
            }
          } catch (e: Exception) {
            // Safe fallback
          }
          isOverlayGranted = true
          onGranted()
        },
        modifier = Modifier.testTag("overlay_grant_btn")
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Skip Button
      GhostGlassButton(
        text = "Skip for now (Shielding cannot trigger)",
        onClick = onSkip,
        modifier = Modifier.testTag("overlay_skip_btn")
      )

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
