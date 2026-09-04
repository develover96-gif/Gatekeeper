# MASTER PRD: Gatekeeper - Mindful Friction System

## 1. Vision & Core Value Proposition
**Gatekeeper** is a "calm-tech" Android application designed to insert a mindful pause between impulse and action. It addresses the "doomscrolling" reflex by intercepting distracting app launches and requiring a cognitive or physical challenge to proceed.

Unlike aggressive blockers, Gatekeeper focuses on **friction**, not force. It empowers users to reclaim their attention through intentional decision-making.

---

## 2. Target Audience
- Users struggling with social media addiction or "zombie" app-switching.
- Professionals seeking deep focus periods.
- Individuals wanting to build better digital hygiene habits.

---

## 3. Functional Requirements

### 3.1. Accessibility-Based Interception
- **Accessibility Service**: Monitors window state changes to detect when a "Gated App" is moved to the foreground.
- **Seamless Overlay**: Launches a full-screen "Gate Overlay" (via `GateActivity`) over the target app before it can be fully interacted with.

### 3.2. Mindful Challenges (The Gates)
- **Math Challenge**: Solves a randomized math problem based on 4 difficulty tiers (Level 1 to Level 4/Logic).
- **Breathing Pacer**: A 60-second guided breathing loop with visual haptic-like cues.
- **Physical Step Counter**: Requires taking a specific number of steps (e.g., 20) before the gate clears.
- **Holding Pause**: Requires holding a button for 5 seconds to demonstrate intent.
- **Reflective Prompt**: A prompt that requires a brief moment of thought before dismissal.

### 3.3. Advanced Mindful Logic
- **Grace Period**: A configurable setting (e.g., 60 seconds) that allows re-opening an app without re-triggering the gate if it was closed recently.
- **Zen Mode**: A temporary state that disables challenges but blocks "Distracting" apps entirely for a user-defined duration.
- **Daily Limits**: Users can set a total duration (e.g., 30 minutes) for specific apps or categories. Once exceeded, the gate triggers for every launch.
- **Session Limits**: Limits individual session duration before automatically re-locking the app.

### 3.4. Dashboard & Analytics
- **Bento-style Dashboard**: Visual summary of total gates cleared and skipped.
- **Reclaimed Time**: Calculation of time saved by choosing *not* to open distracting apps.
- **Mindful Streak**: Encouragement for consistent mindful decisions over consecutive days.
- **App Management**: Easily toggle protection for installed apps or entire categories (Social, Entertainment, etc.).

---

## 4. Technical Architecture
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3 with Custom Glassmorphism Theme)
- **Persistence**: Room Database (SQLite) for stats and app configurations.
- **Navigation**: Navigation Compose with type-safe routes.
- **State Management**: ViewModel + Kotlin Flow.
- **Interception**: Android AccessibilityService API.

---

## 5. Visual Identity & Design
- **Theme**: "Dark Glass" aesthetic.
- **Palette**: Deep Navy (#0F1115), Indigo Accent (#6366F1), Calm Moss Green (#81C784).
- **Typography**: Clean, high-contrast sans-serif (system defaults with custom weighting).
- **UX**: Soft entry animations, subtle haptic feedback (simulated via UI), and generous negative space to promote calm.

---

## 6. Success Metrics
- Reduction in total "impulse" app launches.
- Increased "Gate Skipped" rate (indicating successful mindful abandonment).
- High user retention through non-punitive, aesthetic design.
