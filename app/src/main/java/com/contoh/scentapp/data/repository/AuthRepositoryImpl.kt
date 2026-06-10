package com.contoh.scentapp.data.repository

import com.contoh.scentapp.data.model.User
import com.contoh.scentapp.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val currentUserId: String?
        get() = auth.currentUser?.uid

    override val isLoggedIn: Boolean
        get() = auth.currentUser != null

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Login gagal"))
            val user = getUserFromFirestore(uid) ?: User(uid = uid, email = email)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        email: String, password: String, fullName: String
    ): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Result.failure(Exception("Registrasi gagal"))
            val newUser = User(uid = uid, email = email, fullName = fullName)
            firestore.collection("users").document(uid).set(newUser).await()
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        val uid = currentUserId ?: return null
        return getUserFromFirestore(uid)
    }

    private suspend fun getUserFromFirestore(uid: String): User? {
        return try {
            firestore.collection("users").document(uid)
                .get().await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
