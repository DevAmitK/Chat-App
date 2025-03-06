package com.example.mychatapp.data.remote

import com.example.mychatapp.data.remote.FireBaseCollection.userCollection
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.UserRepo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
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

    override suspend fun getUserById(id: String): User{
        return firestore.userCollection()
            .document(id)
            .get()
            .await()
            .toObject(User::class.java)
            ?: error("No User Found In With Id $id")
    }

    override suspend fun getAllUser(): List<User> {
        return firestore.userCollection()
            .get()
            .await()
            .toObjects(User::class.java)
    }

    override suspend fun getAllUserFlow(): Flow<List<User>> {
        return callbackFlow {
            val registration = firestore.userCollection()
                .addSnapshotListener { value, error ->
                    //Handle Error
                    error?.let {
                        it.printStackTrace()
                        error(it.localizedMessage ?: "Firebase Exception" )
                    }
                    // Parse docs as List<User>
                    val users = value?.toObjects(User::class.java)
                        ?: error("User Not Found")
                    //Finally emit the list
                    CoroutineScope(coroutineContext).launch {
                        send(users)
                    }
                }
            //Remove Listener When Coroutine cansel
            awaitClose {
                registration.remove()
            }
        }

    }

    override suspend fun updateFcmToken(fcmToken: String, userId: String) {
        firestore.userCollection()
            .document(userId)
            .update(User::fcmToken.name , fcmToken)
            .await()
    }

    override suspend fun updateLastOnlineTS(userId: String) {
        firestore.userCollection()
            .document(userId)
            .update(User::lastOnlineTS.name , Timestamp.now())
            .await()
    }


}