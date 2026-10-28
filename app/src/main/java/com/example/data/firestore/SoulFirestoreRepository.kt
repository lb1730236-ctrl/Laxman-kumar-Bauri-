package com.example.data.firestore

import android.content.Context
import android.util.Log
import com.example.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class SoulFirestoreRepository(
    private val firestore: FirebaseFirestore
) {
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(context.getString(R.string.firestore_database_id))
    )

    private val auth = Firebase.auth

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Save or update user soul profile
    suspend fun saveUserProfile(profile: FirestoreUserProfile) {
        val uid = profile.userId.ifBlank { getCurrentUserId() } ?: return
        val docRef = firestore.collection("users").document(uid)
        try {
            docRef.set(profile.copy(userId = uid)).await()
            Log.d("SoulFirestore", "Profile saved successfully for $uid")
        } catch (e: Exception) {
            Log.e("SoulFirestore", "Error saving user profile: ${e.message}", e)
            throw e
        }
    }

    // Observe user profile in real-time
    fun observeUserProfile(userId: String): Flow<FirestoreUserProfile?> {
        return firestore.collection("users").document(userId)
            .snapshots()
            .map { snapshot ->
                snapshot.toObject(FirestoreUserProfile::class.java)
            }
            .catch { e ->
                Log.e("SoulFirestore", "Error observing profile: ${e.message}", e)
                emit(null)
            }
    }

    // Send a message under user's messages subcollection
    suspend fun sendChatMessage(message: FirestoreChatMessage) {
        val uid = message.userId.ifBlank { getCurrentUserId() } ?: return
        val collRef = firestore.collection("users").document(uid).collection("messages")
        try {
            collRef.add(message.copy(userId = uid)).await()
            Log.d("SoulFirestore", "Message sent to Firestore for match ${message.matchId}")
        } catch (e: Exception) {
            Log.e("SoulFirestore", "Error sending message to Firestore: ${e.message}", e)
            throw e
        }
    }

    // Observe messages for a specific match
    fun observeChatMessages(userId: String, matchId: String): Flow<List<FirestoreChatMessage>> {
        return firestore.collection("users").document(userId).collection("messages")
            .whereEqualTo("matchId", matchId)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(FirestoreChatMessage::class.java)
            }
            .catch { e ->
                Log.e("SoulFirestore", "Error observing messages: ${e.message}", e)
                emit(emptyList())
            }
    }
}
