package com.example.mychatapp.presentation.editProfileScreen

import androidx.core.net.toUri
import com.example.mychatapp.domain.ext.currentUserId
import com.example.mychatapp.domain.local.repo.LocalRepo
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.StorageRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.ui.comp.ImageState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.messaging
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class EditProfileViewModel @Inject constructor(
    private val preferenceRepo: LocalRepo,
    private val userRepo: UserRepo,
    private val storageRepo: StorageRepo,
) : BaseViewModel() {
    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()

    val saveProfileTask = taskStateOf<Unit>()
    val user = taskStateOf<User?>()

    fun saveUser(
        user: User,
        imageState: ImageState,
        onSuccess: () -> Unit,
    ) {
        execute(showLoadingDialog = false) {
            saveProfileTask.load {

                //update Firebase Token
                val token = Firebase.messaging.token.await()


                val imageUrl = imageState.let{
                    storageRepo.uploadFile("profileImages/${currentUserId()}",it)
                }
                val updatedUser = user.copy(
                    imageUri =imageUrl,
                    fcmToken = token
                )
                userRepo.saveUserData(user = updatedUser)

                //TODO User Save in Local

                preferenceRepo.saveLoginState(true)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }


    fun loadUser(onSuccess: (User) -> Unit) {
        //TODO User Save in Local and get User
        Firebase.auth.currentUser?.email?.let { email ->
            execute {
                userRepo.getUserWithEmail(email)?.let { user ->
                    _userState.value = user
                    onSuccess(user)
                }
            }
        }
    }


}

