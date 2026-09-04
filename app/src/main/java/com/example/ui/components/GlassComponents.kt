package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.ClayLock
import com.example.ui.theme.GlassBase
import com.example.ui.theme.GlassElevated
import com.example.ui.theme.OnPrimaryIndigo
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.PrimaryIndigoDeep
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GlassCard(
  modifier: Modifier = Modifier,
  backgroundColor: Color = GlassBase,
  borderColor: Color = BorderDefault,
  cornerRadius: Dp = 20.dp,
  content: @Composable BoxScope.() -> Unit
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(cornerRadius))
      .background(backgroundColor)
      .border(1.dp, borderColor, RoundedCornerShape(cornerRadius))
  ) {
    // Subtle top specular highlight line
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(
          Brush.horizontalGradient(
            listOf(
              Color.Transparent,
              BorderHighlight,
              Color.Transparent
            )
          )
        )
    )
    content()
  }
}

@Composable
fun PulsingBeacon(
  color: Color = CalmMoss,
  size: Dp = 8.dp,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val scale by infiniteTransition.animateFloat(
    initialValue = 0.9f,
    targetValue = 1.4f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale"
  )

  Box(
    modifier = modifier.size(size * 2),
    contentAlignment = Alignment.Center
  ) {
    Box(
      modifier = Modifier
        .size(size)
        .scale(scale)
        .background(color.copy(alpha = 0.35f), CircleShape)
    )
    Box(
      modifier = Modifier
        .size(size)
        .background(color, CircleShape)
    )
  }
}

@Composable
fun GlassSwitch(
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  Switch(
    checked = checked,
    onCheckedChange = onCheckedChange,
    modifier = modifier,
    colors = SwitchDefaults.colors(
      checkedThumbColor = OnPrimaryIndigo,
      checkedTrackColor = PrimaryIndigo,
      uncheckedThumbColor = TextSecondary,
      uncheckedTrackColor = SurfaceContainerHigh
    )
  )
}

@Composable
fun PrimaryGlassButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  enabled: Boolean = true
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier
      .fillMaxWidth()
      .height(54.dp),
    shape = RoundedCornerShape(16.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = PrimaryIndigo,
      contentColor = OnPrimaryIndigo,
      disabledContainerColor = SurfaceContainerHigh,
      disabledContentColor = TextSecondary.copy(alpha = 0.5f)
    )
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = text,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.01.sp
      )
      if (icon != null) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
          imageVector = icon,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
fun GhostGlassButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  color: Color = TextSecondary
) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(16.dp),
    color = Color.Transparent,
    contentColor = color,
    modifier = modifier
      .fillMaxWidth()
      .height(48.dp)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
      if (icon != null) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          modifier = Modifier.size(16.dp),
          tint = color
        )
        Spacer(modifier = Modifier.width(6.dp))
      }
      Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = color
      )
    }
  }
}
