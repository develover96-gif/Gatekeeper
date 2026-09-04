# Gatekeeper 🛡️
### Mindful Friction for Digital Wellbeing

Gatekeeper is a modern Android application that inserts a cognitive "speed bump" between your impulse to check distracting apps and the action itself. By requiring a mindful challenge—like a math problem, a breathing exercise, or even physical steps—Gatekeeper transforms digital habits from mindless reflexes into intentional choices.

## ✨ Key Features

- **Adaptive Gate Overlays**: Intercepts launches of apps like Instagram, TikTok, or Reddit with high-performance glassmorphism overlays.
- **5+ Mindful Challenges**:
    - **Math Calibration**: Multi-tier difficulty problems (Basic to Logic).
    - **Breathing Pacer**: 1-minute guided calm.
    - **Active Steps**: Physical motion required via step counter.
    - **Holding Pause**: Intentional 5-second hold.
    - **Reflective Prompt**: Mindful inquiry before entry.
- **Smart Logic**:
    - **Grace Period**: 60s window to re-open apps without friction.
    - **Zen Mode**: Total sanctuary mode that blocks distractions entirely.
    - **Daily/Session Limits**: Hard caps on usage before the "Security Gate" tightens.
- **Beautiful Dashboard**: Bento-style stats tracking your "Mindful Streak" and "Reclaimed Time".

## 🛠️ Technology Stack

- **Jetpack Compose**: 100% declarative UI with custom Material 3 theming.
- **Accessibility Service**: Real-time app interception (On-Device).
- **Room DB**: local persistence for stats and configurations.
- **Kotlin Flow**: Reactive state management.
- **Glassmorphism**: Aesthetic "Dark Glass" visual identity.

## 🚀 Getting Started

1. **Install the App**: Build and run the project in Android Studio.
2. **Enable Accessibility**: Go to Settings > Accessibility > Gatekeeper and turn it ON.
3. **Grant Permissions**: Allow "Display over other apps" and "Physical Activity" for step tracking.
4. **Choose your Apps**: Open the Gatekeeper Dashboard and pick which apps need a "Gate".
5. **Reclaim your Time**: Experience the pause. Decide if you *really* want to open that app.

## 🛡️ Privacy & Security

Gatekeeper operates **entirely on-device**.
- No data leaves your phone.
- No account required.
- Accessibility data is used *only* to detect app launches for gating purposes.

---
*Built with care for a more intentional digital world.*
