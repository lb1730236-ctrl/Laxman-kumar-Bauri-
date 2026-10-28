package com.example.model

enum class CallType {
    VOICE,
    VIDEO
}

enum class CallState {
    IDLE,
    OUTGOING_RINGING,
    INCOMING_RINGING,
    CONNECTED,
    ENDED
}

enum class MessageType {
    TEXT,
    VOICE_NOTE,
    ICEBREAKER,
    SYSTEM
}

data class CompatibilityBreakdown(
    val emotional: Int = 92,
    val intellectual: Int = 88,
    val spiritual: Int = 95,
    val lifestyle: Int = 85
)

data class SoulUser(
    val id: String,
    val name: String,
    val age: Int,
    val zodiac: String,
    val location: String,
    val bio: String,
    val soulArchetype: String,
    val compatibilityScore: Int,
    val breakdown: CompatibilityBreakdown = CompatibilityBreakdown(),
    val loveLanguage: String,
    val interests: List<String>,
    val deepPromptQuestion: String,
    val deepPromptAnswer: String,
    val avatarGradient: List<Long>,
    val voiceIntroDurationSec: Int = 18,
    val isOnline: Boolean = true,
    val verifiedBadge: Boolean = true
)

data class ChatMessage(
    val id: String,
    val matchId: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val messageType: MessageType = MessageType.TEXT,
    val voiceDurationSec: Int = 0,
    val isRead: Boolean = true
)

data class SoulMatchThread(
    val user: SoulUser,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int = 0,
    val isConnected: Boolean = true
)

data class ActiveCallSession(
    val callId: String = "",
    val peerUser: SoulUser,
    val callType: CallType = CallType.VIDEO,
    val state: CallState = CallState.IDLE,
    val durationSeconds: Int = 0,
    val isMuted: Boolean = false,
    val isVideoOn: Boolean = true,
    val isFrontCamera: Boolean = true,
    val isSpeakerOn: Boolean = true,
    val currentIcebreakerIndex: Int = 0
)

data class VibeQuizQuestion(
    val id: Int,
    val question: String,
    val options: List<VibeOption>
)

data class VibeOption(
    val text: String,
    val archetypeTrait: String,
    val synergyBoost: String
)
