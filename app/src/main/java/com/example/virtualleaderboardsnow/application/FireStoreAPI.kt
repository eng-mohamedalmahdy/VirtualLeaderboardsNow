package com.example.virtualleaderboardsnow.application

import android.util.Log
import com.example.virtualleaderboardsnow.application.model.FireStoreAnnouncement
import com.example.virtualleaderboardsnow.application.model.FireStoreHero
import com.example.virtualleaderboardsnow.application.model.FireStoreLeaderboard
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.createannouncementdialog.AnnouncementConfig
import com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes.Hero
import com.google.firebase.firestore.*
import com.squareup.okhttp.internal.DiskLruCache
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlin.math.log

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
            fireStoreDatabase.collection(FireStoreAPIPathsAndKeys.LEADERBOARDS)
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
        val board =
            fireStoreDatabase.collection(FireStoreAPIPathsAndKeys.LEADERBOARDS).document(boardId)
        board.collection("heroes").get().addOnSuccessListener { docs ->
            Log.d(TAG, "addHero: ${docs.toHashSet()}")
            val hero = Hero(name = heroName, id = "${docs.size() + 1}", score = 0)
            board.update("heroes", FieldValue.arrayUnion(hero))
        }

    }

    fun submitUpdates(boardId: String, title: String, configs: List<AnnouncementConfig>) {
        val fireStoreDatabase = FirebaseFirestore.getInstance()
        val configsIds = configs.map { it.heroId }
        CoroutineScope(Dispatchers.IO).launch {
            fireStoreDatabase.collection(FireStoreAPIPathsAndKeys.LEADERBOARDS)
                .document(boardId).get().addOnSuccessListener { doc ->

                    val heroes = doc.data?.get("heroes") as List<Map<String, Any>>
                    Log.d(TAG, "submitUpdates: $heroes")
                    val res = heroes.map { map ->
                        val fireStoreHero = FireStoreHero(map)
                        if (fireStoreHero.id in configsIds) {
                            val score = configs.first { it.heroId == fireStoreHero.id }.heroScore
                            Log.d(TAG, "submitUpdates: $score")
                            FireStoreHero(
                                fireStoreHero.id,
                                fireStoreHero.name,
                                score + fireStoreHero.score
                            )
                        } else {
                            fireStoreHero
                        }
                    }

                    val board =
                        fireStoreDatabase.collection(FireStoreAPIPathsAndKeys.LEADERBOARDS)
                            .document(boardId)
                    board.collection("heroes")
                        .get().addOnSuccessListener { s ->
                            if (heroes != res) {
                                board.update("heroes", FieldValue.delete())
                                    .addOnSuccessListener {
                                        board.update(
                                            "heroes",
                                            FieldValue.arrayUnion(*res.toTypedArray())
                                        )
                                    }.addOnSuccessListener {
                                        val conts = configs.map { "${it.heroName} ${it.heroScore}" }
                                        val announcement = FireStoreAnnouncement(title, conts)
                                        board.update(
                                            "announcements",
                                            FieldValue.arrayUnion(announcement)
                                        )
                                    }
                            }
                        }
                }
        }
    }


    private object FireStoreAPIPathsAndKeys {
        const val LEADERBOARDS = "leaderboards"
        const val ADMIN_ID = "adminId"
    }

}
