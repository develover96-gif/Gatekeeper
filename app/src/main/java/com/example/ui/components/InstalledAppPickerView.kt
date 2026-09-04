package com.example.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.example.data.model.CategoryTags
import com.example.ui.theme.BorderDefault
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CalmMoss
import com.example.ui.theme.CanvasBase
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppPickerViewModel
import com.example.ui.viewmodel.InstalledAppItem

@Composable
fun InstalledAppPickerView(
  viewModel: AppPickerViewModel,
  modifier: Modifier = Modifier,
  onAppClicked: ((InstalledAppItem) -> Unit)? = null
) {
  val apps by viewModel.displayApps.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val filterGatedOnly by viewModel.filterGatedOnly.collectAsState()
  val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsState()
  val isLoading by viewModel.isLoading.collectAsState()

  val activeCount = apps.count { it.isGated }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(CanvasBase)
  ) {
    // Search and filter row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.onSearchQueryChanged(it) },
        modifier = Modifier
          .weight(1f)
          .height(50.dp)
          .testTag("app_search_field"),
        placeholder = { Text("Search installed packages...", color = TextMuted, fontSize = 12.sp) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = SurfaceContainerHigh,
          unfocusedContainerColor = SurfaceContainerLow,
          focusedBorderColor = PrimaryIndigo,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        )
      )

      // Filter Pill
      Box(
        modifier = Modifier
          .height(48.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(if (filterGatedOnly) PrimaryIndigo.copy(alpha = 0.2f) else SurfaceContainerLow)
          .border(1.dp, if (filterGatedOnly) PrimaryIndigo else BorderSubtle, RoundedCornerShape(12.dp))
          .clickable { viewModel.toggleFilterGatedOnly() }
          .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = if (filterGatedOnly) PrimaryIndigo else TextMuted,
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (filterGatedOnly) "Gated" else "All",
            color = if (filterGatedOnly) PrimaryIndigo else TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Refresh Button
      IconButton(
        onClick = { viewModel.loadInstalledApps() },
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(SurfaceContainerLow)
          .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
      ) {
        Icon(Icons.Default.Refresh, contentDescription = "Refresh installed apps", tint = TextSecondary, modifier = Modifier.size(18.dp))
      }
    }

    // Category Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // "All" tag
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(9999.dp))
          .background(if (selectedCategoryFilter == null) PrimaryIndigo else SurfaceContainerLow)
          .border(1.dp, if (selectedCategoryFilter == null) PrimaryIndigo else BorderSubtle, RoundedCornerShape(9999.dp))
          .clickable { viewModel.setCategoryFilter(null) }
          .padding(horizontal = 10.dp, vertical = 5.dp)
      ) {
        Text(
          text = "All",
          color = if (selectedCategoryFilter == null) Color.White else TextSecondary,
          fontSize = 11.sp,
          fontWeight = if (selectedCategoryFilter == null) FontWeight.SemiBold else FontWeight.Normal
        )
      }

      listOf(CategoryTags.SOCIAL, CategoryTags.ENTERTAINMENT, CategoryTags.PRODUCTIVITY, CategoryTags.NEWS).forEach { cat ->
        val isSelected = selectedCategoryFilter == cat
        val catColor = Color(CategoryTags.getCategoryAccentHex(cat))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(if (isSelected) catColor.copy(alpha = 0.22f) else SurfaceContainerLow)
            .border(1.dp, if (isSelected) catColor else BorderSubtle, RoundedCornerShape(9999.dp))
            .clickable {
              viewModel.setCategoryFilter(if (selectedCategoryFilter == cat) null else cat)
            }
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = cat,
            color = if (isSelected) catColor else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
          )
        }
      }
    }

    // Category Batch Restriction Action Bar (when category is selected)
    if (selectedCategoryFilter != null) {
      val catName = selectedCategoryFilter!!
      val isAllRestricted = apps.isNotEmpty() && apps.all { it.isGated }
      val catColor = Color(CategoryTags.getCategoryAccentHex(catName))

      GlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp, vertical = 4.dp),
        backgroundColor = SurfaceContainerLow.copy(alpha = 0.75f),
        borderColor = catColor.copy(alpha = 0.35f),
        cornerRadius = 10.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "$catName Apps (${apps.size})",
              color = TextPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
            Text(
              text = "${apps.count { it.isGated }} currently restricted",
              color = TextMuted,
              fontSize = 10.sp
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = if (isAllRestricted) "Unrestrict All" else "Restrict All",
              color = catColor,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.clickable {
                viewModel.toggleCategoryGated(catName, !isAllRestricted)
              }
            )
            Spacer(modifier = Modifier.width(8.dp))
            GlassSwitch(
              checked = isAllRestricted,
              onCheckedChange = { shouldGateAll ->
                viewModel.toggleCategoryGated(catName, shouldGateAll)
              }
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(4.dp))

    if (isLoading) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(140.dp),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(color = PrimaryIndigo, modifier = Modifier.size(28.dp))
      }
    } else if (apps.isEmpty()) {
      GlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        backgroundColor = SurfaceContainerLow,
        cornerRadius = 14.dp
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = if (searchQuery.isNotEmpty()) "No apps found matching \"$searchQuery\"" else "No apps found",
            color = TextSecondary,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Try searching for a different keyword or reset filters.",
            color = TextMuted,
            fontSize = 11.sp
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(apps, key = { it.packageName }) { appItem ->
          InstalledAppRowItem(
            item = appItem,
            onToggle = { isChecked ->
              viewModel.toggleAppGated(
                appItem.packageName,
                isChecked,
                appItem.appName,
                appItem.category
              )
            },
            onClick = { onAppClicked?.invoke(appItem) }
          )
        }
      }
    }
  }
}

@Composable
fun InstalledAppRowItem(
  item: InstalledAppItem,
  onToggle: (Boolean) -> Unit,
  onClick: () -> Unit
) {
  GlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("app_row_${item.packageName}"),
    backgroundColor = if (item.isGated) SurfaceContainerHigh else SurfaceContainerLow.copy(alpha = 0.6f),
    borderColor = if (item.isGated) PrimaryIndigo.copy(alpha = 0.35f) else BorderSubtle,
    cornerRadius = 14.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (item.isGated) PrimaryIndigo.copy(alpha = 0.2f) else SurfaceContainer)
            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp)),
          contentAlignment = Alignment.Center
        ) {
          val imageBitmap = remember(item.icon) {
            try {
              item.icon?.toBitmap(64, 64)?.asImageBitmap()
            } catch (t: Throwable) {
              null
            }
          }
          if (imageBitmap != null) {
            Image(
              bitmap = imageBitmap,
              contentDescription = item.appName,
              modifier = Modifier.size(24.dp)
            )
          } else {
            FallbackIcon(item)
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = item.appName,
              color = TextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(6.dp))
            val catAccent = Color(CategoryTags.getCategoryAccentHex(item.category))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(catAccent.copy(alpha = 0.15f))
                .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
              Text(
                text = CategoryTags.normalize(item.category),
                color = catAccent,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
            if (item.isGated) {
              Spacer(modifier = Modifier.width(4.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(PrimaryIndigo.copy(alpha = 0.15f))
                  .padding(horizontal = 5.dp, vertical = 1.dp)
              ) {
                Text(
                  text = when (item.defaultChallengeType) {
                    "BREATHING" -> "Breathing"
                    "STEPS" -> "Walking"
                    else -> "Math"
                  },
                  color = PrimaryIndigo,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
          Text(
            text = "${CategoryTags.normalize(item.category)} • ${item.packageName.substringAfterLast('.')}",
            color = TextSecondary,
            fontSize = 11.sp
          )
        }
      }

      GlassSwitch(
        checked = item.isGated,
        onCheckedChange = onToggle
      )
    }
  }
}

@Composable
private fun FallbackIcon(item: InstalledAppItem) {
  val icon = when {
    item.defaultChallengeType == "BREATHING" -> Icons.Default.Air
    item.defaultChallengeType == "STEPS" -> Icons.Default.DirectionsWalk
    item.packageName.contains("instagram") -> Icons.Default.CameraAlt
    item.packageName.contains("tiktok") -> Icons.Default.PlayArrow
    item.packageName.contains("twitter") -> Icons.Default.AlternateEmail
    item.packageName.contains("reddit") -> Icons.Default.Forum
    item.packageName.contains("youtube") -> Icons.Default.SmartDisplay
    item.packageName.contains("chrome") -> Icons.Default.Language
    else -> Icons.Default.Calculate
  }
  Icon(
    imageVector = icon,
    contentDescription = null,
    tint = if (item.isGated) PrimaryIndigo else TextMuted,
    modifier = Modifier.size(20.dp)
  )
}
