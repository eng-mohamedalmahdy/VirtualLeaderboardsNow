package com.example.virtualleaderboardsnow.application

import android.util.Log
import com.example.virtualleaderboardsnow.application.model.FireStoreAnnouncement
import com.example.virtualleaderboardsnow.application.model.FireStoreHero
import com.example.virtualleaderboardsnow.application.model.FireStoreLeaderboard
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes.Hero
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val TAG = "FireStoreAPI"

class FireStoreAPI {
    @ExperimentalCoroutinesApi
    fun getUserLeaderboardsList(): Flow<List<FireStoreLeaderboard>> {
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        return callbackFlow {
            fireStoreDatabase.collection(FireStoreAPIPathsAndKeys.LEADERBOARDS)
//                .whereArrayContains(FireStoreAPIPathsAndKeys.ADMIN_ID, " ")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        CoroutineScope(Dispatchers.IO).launch {
                            this@callbackFlow.awaitClose {
                                cancel()
                            }
                        }
                        return@addSnapshotListener

                    } else {
                        val res =
                            snapshot?.documents?.mapNotNull { it.toObject(FireStoreLeaderboard::class.java) }
                                ?: listOf()
                        trySend(res)
                    }
                }
            this@callbackFlow.awaitClose {
                cancel()
            }
        }
    }

    fun getLeaderboardAnnouncements(boardId: String): Flow<List<FireStoreAnnouncement>> {
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        return callbackFlow {
            fireStoreDatabase.collection("${FireStoreAPIPathsAndKeys.LEADERBOARDS}")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        CoroutineScope(Dispatchers.IO).launch {
                            this@callbackFlow.awaitClose {
                                cancel()
                            }
                        }
                        return@addSnapshotListener

                    } else {
                        try {
                            val leaderboard = snapshot?.documents?.first { boardId == it["id"] }
                                ?.toObject(FireStoreLeaderboard::class.java)
                            Log.d(TAG, "getLeaderboardAnnouncements: $leaderboard")
                            trySend((leaderboard?.announcements ?: listOf()).filterNotNull())
                        } catch (ex: NoSuchElementException) {

                        }
                    }
                }
            this@callbackFlow.awaitClose {
                cancel()
            }
        }
    }

    fun getLeaderBoardHeroes(boardId: String): Flow<List<FireStoreHero>> {
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        return callbackFlow {
            fireStoreDatabase.collection("${FireStoreAPIPathsAndKeys.LEADERBOARDS}")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        CoroutineScope(Dispatchers.IO).launch {
                            this@callbackFlow.awaitClose {
                                cancel()
                            }
                        }
                        return@addSnapshotListener

                    } else {
                        try {
                            val leaderboard = snapshot?.documents?.first { boardId == it["id"] }
                                ?.toObject(FireStoreLeaderboard::class.java)
                            trySend(
                                (leaderboard?.Heroes ?: listOf<FireStoreHero>()).filterNotNull()
                            )
                        } catch (ex: NoSuchElementException) {

                        }

                    }
                }
            this@callbackFlow.awaitClose {
                cancel()
            }
        }
    }

    fun addLeaderboard(boardName: String) {
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        val key = fireStoreDatabase.collection(FireStoreAPIPathsAndKeys.LEADERBOARDS).document().id
        val leaderboard = FireStoreLeaderboard(name = boardName, id = key)
        fireStoreDatabase.collection(FireStoreAPIPathsAndKeys.LEADERBOARDS).document(key)
            .set(leaderboard)
    }

    fun addHero(boardId: String, heroName: String) {
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        val hero = Hero(name = heroName, id = "", score = 0)

        fireStoreDatabase.collection(FireStoreAPIPathsAndKeys.LEADERBOARDS).document(boardId)
            .update("heroes", FieldValue.arrayUnion(hero))

    }

    

    private object FireStoreAPIPathsAndKeys {
        const val LEADERBOARDS = "leaderboards"
        const val ADMIN_ID = "adminId"
    }

}
