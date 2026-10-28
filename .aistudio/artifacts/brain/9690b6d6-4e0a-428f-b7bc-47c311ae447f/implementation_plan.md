# SoulSync — Soulmate Social & Real-Time Calling App

SoulSync is an immersive social platform designed to help users discover true soulmates through deep compatibility matching, meaningful real-time messaging, and interactive 1-on-1 voice and video calling.

### User Review & Critical Decisions

> [!IMPORTANT]
> **Key Architecture Decisions for Review:**
> - **Visual Direction**: Implemented the confirmed **Romantic Dark Mode** featuring obsidian surfaces, radiant amethyst violet, deep wine crimson, and glowing rose-gold accents.
> - **Real-Time Voice & Video Calling Architecture**: Call sessions feature full interactive call management (live elapsed duration, microphone mute/unmute, front/rear camera toggle, audio output routing, in-call icebreaker prompts, and simulated remote video/audio stream rendering) backed by reactive Kotlin Coroutine StateFlow pipelines.
> - **Compatibility Engine & Matching**: A multi-dimensional "Soul Score" calculates real-time affinity based on emotional wavelength, values, communication rhythm, and astrological harmony.
> - **Audio & Camera Permissions**: Camera and microphone permissions will be declared in the manifest and gracefully requested with interactive Jetpack Compose permission sheets when launching video or audio calls.

---

### 1. Overview & Core Concept

- **What It Does**: SoulSync pairs users based on deep psychological and emotional compatibility, provides a cozy real-time chat interface with instant icebreakers, and enables seamless 1-on-1 voice and video calls with real-time controls.
- **Target Audience / Persona**: People seeking authentic, emotionally resonant relationships rather than superficial surface-level swipe apps.
- **Key Value**: Bridges the gap between matching and genuine emotional intimacy by offering guided vibe checks, shared questions, and low-pressure voice/video call experiences.

---

### 2. User Experience & Visual Design

#### Key User Flows
1. **Soul Discovery & Radar**: Users browse potential soulmates presented as rich multi-layered cards showing compatibility breakdown percentages (Emotional, Intellectual, Lifestyle, Energy), personal values, audio voice intros, and bio prompts.
2. **Interactive Soulmate Vibe Quiz**: A quick 4-step vibe test that updates the user's Soul Profile and unlocks dynamic match suggestions.
3. **Real-Time Chat & Connection Lounge**: Smooth messaging with match status, typing indicators, photo/audio note simulators, and one-tap "Deep Question" icebreaker chips.
4. **Voice & Video Calling Hub**: 
   - Instant call initiation (Voice Call or Video Call) from any chat header.
   - Incoming call notification banner with Accept / Decline controls.
   - Full-screen Video Call UI with local picture-in-picture camera feed, remote partner view, live call timer, floating control dock (Mute, Video On/Off, Switch Camera, Speakerphone, End Call), and interactive "Soul Icebreaker" cards to keep conversations flowing naturally.
   - Full-screen Voice Call UI with pulsing audio frequency wave animations and ambient background glow.
5. **My Soul Profile**: View and edit personal traits, zodiac sign, love languages, voice intro clip, and privacy settings.

#### Visual Identity & Theme
- **Aesthetic Direction**: Ethereal Romantic Dark Mode. Deep interstellar luxury that feels intimate, premium, and safe.
- **Color Palette**:
  - `Background`: Deep Obsidian (`#0B0B12`) and Midnight Slate (`#141324`)
  - `Primary / Accent`: Radiant Amethyst Violet (`#9D4EDD`) and Electric Magenta (`#E056FD`)
  - `Secondary Accent`: Warm Rose Gold (`#F7A8B8`) and Celestial Amber (`#FFB703`)
  - `Surface Containers`: Translucent Glassmorphism (`#1F1D36` with 80% opacity and subtle borders)
  - `Text Hierarchy`: High-contrast Crisp Off-White (`#F8F9FA`), Muted Lilac (`#C8B6FF`), and Subdued Lavender (`#8A817C`)
- **Typography & Hierarchy**:
  - Expressive headlines for soulmate match percentages and names.
  - Clean, legible body text for messages and thoughtful conversation prompts.
- **Interactive Feedback & Motion**:
  - Pulsing aura around high-compatibility match cards (>90% Soul Score).
  - Smooth slide and crossfade transitions between Discovery, Chats, and Calling views.
  - Interactive audio ripple effects during active voice calls.

---

### 3. Key Product Decisions & Trade-Offs

- **Decision 1: Calling Engine & Media Handling**
  - *Chosen Approach*: Comprehensive Android Calling UI & Simulation Engine powered by Android CameraX/Telecom-ready state management, supporting real-time local camera previews, active audio feedback, permission requests, and call state machines (Connecting -> Ringing -> Connected -> Ended).
  - *Why*: Provides an immediate, production-grade, zero-latency calling experience with full user control over audio, camera, and in-call interaction tools.
  - *Alternatives Considered*: Heavy third-party proprietary SDKs that require external paid accounts or break in sandboxed environments.
- **Decision 2: Local & Cloud Persistence Strategy**
  - *Chosen Approach*: Fast, reactive local Room database with reactive Kotlin Flow state, pre-populated with diverse, realistic soulmate profiles, realistic conversation histories, and live messaging updates.
  - *Why*: Instant launch, zero login friction for immediate exploration, fully offline-capable, and smooth 60fps animations.
- **Decision 3: In-Call Icebreaker Overlay**
  - *Chosen Approach*: A collapsible, unobtrusive card at the bottom of the video/voice call containing curated soul-probing questions (e.g., *"What is a dream you haven't told anyone?"* or *"What made you smile today?"*).
  - *Why*: Addresses the #1 user anxiety during first-time video/voice dates: awkward silences.

---

### 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Jetpack Compose UI Layer                        │
├─────────────────┬──────────────────┬─────────────────┬─────────────────┤
│ Soul Discovery  │ Real-Time Chat   │ 1-on-1 Video    │ 1-on-1 Voice    │
│  & Vibe Radar   │  & Deep Prompts  │ Calling Screen  │ Calling Screen  │
└────────┬────────┴────────┬─────────┴────────┬────────┴────────┬────────┘
         │                 │                  │                 │
┌────────▼─────────────────▼──────────────────▼─────────────────▼────────┐
│                     ViewModel & State Management                       │
│  - SoulMatchViewModel: Discovery, Filtering & Vibe Quiz                │
│  - ChatViewModel: Real-time Message Flows, Typing, Icebreakers        │
│  - CallViewModel: Call State Machine (Offer, Ring, Live, Controls)     │
└──────────────────────────────┬─────────────────────────────────────────┘
                               │
┌──────────────────────────────▼─────────────────────────────────────────┐
│                      Repository & Hardware Layer                       │
│  - SoulRepository: Room DB (Profiles, Chats, Messages, User Traits)    │
│  - CallManager: Audio routing, Camera preview states & Call duration  │
│  - PermissionManager: CAMERA & RECORD_AUDIO runtime request flows      │
└────────────────────────────────────────────────────────────────────────┘
```

#### Data Entities & State Models
- **`SoulUser`**: ID, Name, Age, Occupation, Location, Bio, CompatibilityScore, SharedInterests, Zodiac, LoveLanguage, AvatarUrl, IsOnline, VoiceIntroUrl, PersonalityTraits.
- **`ChatMessage`**: ID, SenderId, ReceiverId, Content, Timestamp, MessageType (Text, VoiceNote, DeepQuestion, SystemAlert), IsRead.
- **`CallSession`**: CallId, PeerUser, CallType (AUDIO, VIDEO), CallState (IDLE, RINGING, CONNECTED, DISCONNECTED), DurationSeconds, IsMuted, IsVideoEnabled, IsFrontCamera, IsSpeakerOn.
- **`CompatibilityQuiz`**: Questions, selected answers, and resulting synergy dimensions (Heart, Mind, Spirit, Rhythm).

#### Key Sequences & State Transitions
1. **Calling Flow**:
   `Initiate Call` -> `Permission Check (Mic/Camera)` -> `CallState.RINGING (with outgoing chime)` -> `CallState.CONNECTED (timer starts, video/audio tracks activate)` -> `In-Call Interactions (Mute, Flip Camera, Prompt)` -> `CallState.DISCONNECTED (summary toast)`.
2. **Chat Flow**:
   `Select Soulmate` -> `Load Thread` -> `Send Message / Icebreaker` -> `Simulated Real-time Peer Response with Typing Indicator` -> `Optionally elevate directly to Voice or Video Call`.
