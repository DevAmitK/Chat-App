package com.example.mychatapp.presentation.EditProfile

import androidx.core.net.toUri
import com.example.mychatapp.data.remote.USER_IMAGE
import com.example.mychatapp.domain.Ext.currentUser
import com.example.mychatapp.domain.local.repo.PreferenceRepo
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.domain.model.User
import com.example.mychatapp.domain.remote.StorageRepo
import com.streamliners.base.BaseViewModel
import com.streamliners.base.ext.execute
import com.streamliners.base.taskState.load
import com.streamliners.base.taskState.taskStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class EditProfileViewModel @Inject constructor(
    private val preferenceRepo: PreferenceRepo,
    private val userRepo: UserRepo,
    private val storageRepo: StorageRepo
) : BaseViewModel() {

    val saveProfileTask = taskStateOf<Unit>()


    fun saveUser(
        user: User,
        onSuccess: () -> Unit,
    ) {
        execute(showLoadingDialog = false) {
            saveProfileTask.load {
              val imageUrl =   user.imageUri?.toUri()?.let { storageRepo.uploadFile(currentUser(), it) }
                user.imageUri = imageUrl
                userRepo.saveUserData(user = user)
                preferenceRepo.saveLoginState(true)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            }
        }
    }
}