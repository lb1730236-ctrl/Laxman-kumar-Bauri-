package com.example.data.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class FirestoreUserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val age: Int = 0,
    val zodiac: String = "",
    val location: String = "",
    val bio: String = "",
    val soulArchetype: String = "",
    val compatibilityScore: Int = 0,
    val loveLanguage: String = "",
    val interests: List<String> = emptyList(),
    val isOnline: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreChatMessage(
    @DocumentId val id: String = "",
    val userId: String = "",
    val matchId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val messageType: String = "TEXT",
    val voiceDurationSec: Int = 0,
    @ServerTimestamp val createdAt: Timestamp? = null
)
