package com.example.mychatapp.data.remote

import com.example.mychatapp.data.remote.FireBaseCollection.userCollection
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.UserRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): UserRepo {

    /**
     * User data Save in the firebase database
     */
    override suspend fun saveUserData(user: User) {
        firestore
            .userCollection()
            .document(user.id)
            .set(user)
            .await()

    }

    /**
     * Check user is login before or not
     */
    override suspend fun getUserWithEmail(email: String): User? {
        return firestore.userCollection()
            .whereEqualTo(User::email.name, email)
            .get()
            .await()
            .toObjects(User::class.java)
            .firstOrNull()
    }

    override suspend fun getAllUser(): List<User> {
        return firestore.userCollection()
            .get()
            .await()
            .toObjects(User::class.java)
    }


}