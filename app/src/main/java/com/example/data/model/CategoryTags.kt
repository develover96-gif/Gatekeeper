package com.example.data.model

object CategoryTags {
  const val SOCIAL = "Social"
  const val ENTERTAINMENT = "Entertainment"
  const val PRODUCTIVITY = "Productivity"
  const val NEWS = "News"
  const val GAMING = "Gaming"
  const val UTILITIES = "Utilities"

  val STANDARD_CATEGORIES = listOf(
    SOCIAL,
    ENTERTAINMENT,
    PRODUCTIVITY,
    NEWS,
    GAMING,
    UTILITIES
  )

  /**
   * Normalizes any raw category string into a recognized standard category tag,
   * or keeps the custom tag formatted cleanly.
   */
  fun normalize(raw: String): String {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return SOCIAL

    val lower = trimmed.lowercase()
    return when {
      lower.contains("social") || lower.contains("instagram") || lower.contains("facebook") ||
        lower.contains("chat") || lower.contains("snapchat") || lower.contains("forum") ||
        lower.contains("reddit") || lower.contains("discussion") || lower.contains("media") -> SOCIAL

      lower.contains("entertain") || lower.contains("short video") || lower.contains("stream") ||
        lower.contains("play") || lower.contains("music") || lower.contains("tiktok") ||
        lower.contains("youtube") || lower.contains("netflix") || lower.contains("spotify") ||
        lower.contains("twitch") || lower.contains("video") -> ENTERTAINMENT

      lower.contains("product") || lower.contains("work") || lower.contains("browser") ||
        lower.contains("chrome") || lower.contains("web") || lower.contains("doc") ||
        lower.contains("note") || lower.contains("mail") || lower.contains("slack") ||
        lower.contains("office") || lower.contains("sheet") || lower.contains("drive") ||
        lower.contains("task") -> PRODUCTIVITY

      lower.contains("news") || lower.contains("feed") || lower.contains("twitter") ||
        lower.contains("press") || lower.contains("article") -> NEWS

      lower.contains("game") || lower.contains("gaming") || lower.contains("arcade") -> GAMING

      lower.contains("util") || lower.contains("tool") || lower.contains("system") -> UTILITIES

      else -> trimmed.replaceFirstChar { it.uppercase() }
    }
  }

  fun getCategoryAccentHex(category: String): Long {
    return when (normalize(category)) {
      SOCIAL -> 0xFF6366F1 // Indigo / Violet
      ENTERTAINMENT -> 0xFFEC4899 // Pink / Magenta
      PRODUCTIVITY -> 0xFF10B981 // Emerald / Green
      NEWS -> 0xFFF59E0B // Amber / Orange
      GAMING -> 0xFF8B5CF6 // Purple
      UTILITIES -> 0xFF06B6D4 // Cyan
      else -> 0xFF6366F1
    }
  }
}
