package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SoulSampleData
import com.example.model.ActiveCallSession
import com.example.model.CallState
import com.example.model.CallType
import com.example.model.ChatMessage
import com.example.model.MessageType
import com.example.model.SoulMatchThread
import com.example.model.SoulUser
import com.example.model.VibeOption
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class SoulViewModel : ViewModel() {

    private val _discoveryUsers = MutableStateFlow<List<SoulUser>>(SoulSampleData.sampleUsers)
    val discoveryUsers: StateFlow<List<SoulUser>> = _discoveryUsers.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    private val _threads = MutableStateFlow<List<SoulMatchThread>>(SoulSampleData.sampleThreads)
    val threads: StateFlow<List<SoulMatchThread>> = _threads.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(SoulSampleData.initialMessages)
    val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

    private val _activeCall = MutableStateFlow<ActiveCallSession?>(null)
    val activeCall: StateFlow<ActiveCallSession?> = _activeCall.asStateFlow()

    private val _isPeerTyping = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val isPeerTyping: StateFlow<Map<String, Boolean>> = _isPeerTyping.asStateFlow()

    private val _userArchetype = MutableStateFlow("Cosmic Empath")
    val userArchetype: StateFlow<String> = _userArchetype.asStateFlow()

    private val _quizStep = MutableStateFlow(0)
    val quizStep: StateFlow<Int> = _quizStep.asStateFlow()

    private val _quizCompleted = MutableStateFlow(false)
    val quizCompleted: StateFlow<Boolean> = _quizCompleted.asStateFlow()

    private val _quizAnswers = MutableStateFlow<Map<Int, VibeOption>>(emptyMap())
    val quizAnswers: StateFlow<Map<Int, VibeOption>> = _quizAnswers.asStateFlow()

    private val _callBannerMessage = MutableStateFlow<String?>(null)
    val callBannerMessage: StateFlow<String?> = _callBannerMessage.asStateFlow()

    private var callTimerJob: Job? = null
    private var ringingJob: Job? = null

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun getFilteredUsers(): List<SoulUser> {
        return when (_selectedFilter.value) {
            ">90% Synergy" -> _discoveryUsers.value.filter { it.compatibilityScore >= 90 }
            "Online Now" -> _discoveryUsers.value.filter { it.isOnline }
            else -> _discoveryUsers.value
        }
    }

    // Call Management
    fun startCall(peer: SoulUser, callType: CallType) {
        ringingJob?.cancel()
        callTimerJob?.cancel()

        val session = ActiveCallSession(
            callId = UUID.randomUUID().toString(),
            peerUser = peer,
            callType = callType,
            state = CallState.OUTGOING_RINGING,
            durationSeconds = 0,
            isMuted = false,
            isVideoOn = callType == CallType.VIDEO,
            isFrontCamera = true,
            isSpeakerOn = true,
            currentIcebreakerIndex = 0
        )
        _activeCall.value = session

        // Simulate peer answering after 2.5 seconds
        ringingJob = viewModelScope.launch {
            delay(2500)
            if (_activeCall.value?.state == CallState.OUTGOING_RINGING) {
                _activeCall.value = _activeCall.value?.copy(state = CallState.CONNECTED)
                startCallTimer()
            }
        }
    }

    fun triggerSimulatedIncomingCall(peer: SoulUser = SoulSampleData.sampleUsers.first(), callType: CallType = CallType.VIDEO) {
        if (_activeCall.value != null) return
        ringingJob?.cancel()
        callTimerJob?.cancel()

        val session = ActiveCallSession(
            callId = UUID.randomUUID().toString(),
            peerUser = peer,
            callType = callType,
            state = CallState.INCOMING_RINGING,
            durationSeconds = 0,
            isMuted = false,
            isVideoOn = callType == CallType.VIDEO,
            isFrontCamera = true,
            isSpeakerOn = true,
            currentIcebreakerIndex = 0
        )
        _activeCall.value = session
    }

    fun acceptIncomingCall() {
        ringingJob?.cancel()
        _activeCall.value = _activeCall.value?.copy(state = CallState.CONNECTED)
        startCallTimer()
    }

    fun declineIncomingCall() {
        ringingJob?.cancel()
        callTimerJob?.cancel()
        _activeCall.value = null
        showCallBanner("Call declined")
    }

    private fun startCallTimer() {
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (_activeCall.value?.state == CallState.CONNECTED) {
                delay(1000)
                _activeCall.value = _activeCall.value?.let { current ->
                    current.copy(durationSeconds = current.durationSeconds + 1)
                }
            }
        }
    }

    fun toggleMute() {
        _activeCall.value = _activeCall.value?.let {
            it.copy(isMuted = !it.isMuted)
        }
    }

    fun toggleVideo() {
        _activeCall.value = _activeCall.value?.let {
            it.copy(isVideoOn = !it.isVideoOn)
        }
    }

    fun toggleCamera() {
        _activeCall.value = _activeCall.value?.let {
            it.copy(isFrontCamera = !it.isFrontCamera)
        }
    }

    fun toggleSpeaker() {
        _activeCall.value = _activeCall.value?.let {
            it.copy(isSpeakerOn = !it.isSpeakerOn)
        }
    }

    fun nextInCallIcebreaker() {
        _activeCall.value = _activeCall.value?.let {
            val nextIdx = (it.currentIcebreakerIndex + 1) % SoulSampleData.deepIcebreakers.size
            it.copy(currentIcebreakerIndex = nextIdx)
        }
    }

    fun endCall() {
        val duration = _activeCall.value?.durationSeconds ?: 0
        val peerName = _activeCall.value?.peerUser?.name ?: "Soulmate"
        ringingJob?.cancel()
        callTimerJob?.cancel()
        _activeCall.value = _activeCall.value?.copy(state = CallState.ENDED)

        viewModelScope.launch {
            delay(400)
            _activeCall.value = null
            val minutes = duration / 60
            val seconds = duration % 60
            val durationStr = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            showCallBanner("Call with $peerName ended ($durationStr)")
        }
    }

    private fun showCallBanner(msg: String) {
        viewModelScope.launch {
            _callBannerMessage.value = msg
            delay(3500)
            _callBannerMessage.value = null
        }
    }

    // Messaging & Chat
    fun sendMessage(matchId: String, text: String, type: MessageType = MessageType.TEXT, voiceDurationSec: Int = 0) {
        if (text.isBlank() && type != MessageType.VOICE_NOTE) return

        val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
        val newMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            matchId = matchId,
            senderId = "me",
            text = text,
            timestamp = currentTime,
            isFromMe = true,
            messageType = type,
            voiceDurationSec = voiceDurationSec
        )

        val currentList = _messages.value[matchId] ?: emptyList()
        _messages.value = _messages.value + (matchId to (currentList + newMessage))

        // Update threads
        _threads.value = _threads.value.map { thread ->
            if (thread.user.id == matchId) {
                thread.copy(
                    lastMessage = if (type == MessageType.VOICE_NOTE) "🎤 Voice note ($voiceDurationSec s)" else text,
                    lastMessageTime = currentTime
                )
            } else {
                thread
            }
        }

        // Simulate thoughtful peer response
        simulatePeerReply(matchId, text)
    }

    private fun simulatePeerReply(matchId: String, userText: String) {
        viewModelScope.launch {
            delay(800)
            _isPeerTyping.value = _isPeerTyping.value + (matchId to true)
            delay(2200)
            _isPeerTyping.value = _isPeerTyping.value + (matchId to false)

            val peer = _discoveryUsers.value.find { it.id == matchId } ?: return@launch
            val replyText = generateAuthenticReply(peer, userText)
            val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())

            val replyMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                matchId = matchId,
                senderId = matchId,
                text = replyText,
                timestamp = currentTime,
                isFromMe = false,
                messageType = MessageType.TEXT
            )

            val currentList = _messages.value[matchId] ?: emptyList()
            _messages.value = _messages.value + (matchId to (currentList + replyMsg))

            _threads.value = _threads.value.map { thread ->
                if (thread.user.id == matchId) {
                    thread.copy(
                        lastMessage = replyText,
                        lastMessageTime = currentTime
                    )
                } else {
                    thread
                }
            }
        }
    }

    private fun generateAuthenticReply(peer: SoulUser, userText: String): String {
        return when {
            userText.contains("call", ignoreCase = true) || userText.contains("video", ignoreCase = true) ->
                "I'd love that! Tap the call or video icon up top and let's hear each other's voices."
            userText.contains("mystery", ignoreCase = true) || userText.contains("star", ignoreCase = true) ->
                "That touches right on what I've been journaling about. It feels so rare to find someone who thinks about this."
            userText.contains("truth", ignoreCase = true) || userText.contains("secret", ignoreCase = true) ->
                "Thank you for being so open with me. Honesty is the most magnetic quality a soul can offer."
            else ->
                "I felt a real wave of resonance reading this. Let's explore this deeper — what do you feel your intuition is telling you?"
        }
    }

    fun connectWithSoulmate(user: SoulUser) {
        val existingThread = _threads.value.find { it.user.id == user.id }
        if (existingThread == null) {
            val welcomeThread = SoulMatchThread(
                user = user,
                lastMessage = "Connected with ${user.name}! Send an icebreaker or start a call.",
                lastMessageTime = "Just now",
                unreadCount = 0,
                isConnected = true
            )
            _threads.value = listOf(welcomeThread) + _threads.value
            showCallBanner("Soul connection sparked with ${user.name} (${user.compatibilityScore}% synergy)!")
        }
    }

    // Quiz Management
    fun selectQuizOption(questionId: Int, option: VibeOption) {
        _quizAnswers.value = _quizAnswers.value + (questionId to option)
        if (_quizStep.value < SoulSampleData.quizQuestions.size - 1) {
            _quizStep.value = _quizStep.value + 1
        } else {
            completeQuiz()
        }
    }

    fun resetQuiz() {
        _quizStep.value = 0
        _quizCompleted.value = false
        _quizAnswers.value = emptyMap()
    }

    private fun completeQuiz() {
        _quizCompleted.value = true
        val topArchetype = _quizAnswers.value.values
            .groupBy { it.archetypeTrait }
            .maxByOrNull { it.value.size }
            ?.key ?: "Cosmic Mystic"
        _userArchetype.value = topArchetype

        // Boost compatibility on candidates matching user vibe
        _discoveryUsers.value = _discoveryUsers.value.map { user ->
            if (user.soulArchetype.contains(topArchetype, ignoreCase = true)) {
                user.copy(compatibilityScore = (user.compatibilityScore + 3).coerceAtMost(100))
            } else {
                user
            }
        }
        showCallBanner("Soul Synergy Test Complete: You are a $topArchetype!")
    }
}
