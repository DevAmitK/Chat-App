package com.example.mychatapp.data.local.repoimpl

import com.example.mychatapp.data.local.DataStoreUtil
import com.example.mychatapp.domain.local.repo.PreferenceRepo
import javax.inject.Inject

class PreferenceRepoImpl @Inject constructor(
    private  val dataStore: DataStoreUtil
) : PreferenceRepo {
    override suspend fun getLoginState(): Boolean {
        return dataStore.getData<Boolean>(LOGIN_KEY) ?: false
    }

    override suspend fun saveLoginState(isLogin: Boolean) {
        dataStore.setData(LOGIN_KEY,isLogin)
    }

}




