package com.example.mychatapp.koin


import com.example.mychatapp.data.local.DataStoreUtil
import com.example.mychatapp.domain.local.repo.PreferenceRepo
import com.example.mychatapp.data.local.repoimpl.PreferenceRepoImpl
import com.example.mychatapp.data.remote.ChannelRepoImpl
import com.example.mychatapp.data.remote.StorageRepoImpl
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.data.remote.UserRepoImpl
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.StorageRepo
import com.example.mychatapp.presentation.editProfileScreen.EditProfileViewModel
import com.example.mychatapp.presentation.loginScreen.LoginViewModel
import com.example.mychatapp.presentation.chatScreen.ChatViewModel
import com.example.mychatapp.presentation.homeScreen.HomeViewModel
import com.example.mychatapp.presentation.newChatsScreen.NewChatViewModel
import com.example.mychatapp.presentation.splashScreen.SplashViewModel
import com.example.mychatapp.presentation.userProfileScreen.UserProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single { DataStoreUtil.create(get()) }
    single<PreferenceRepo> { PreferenceRepoImpl(get()) }
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
    single<UserRepo> { UserRepoImpl(get()) }
    single<ChannelRepo> { ChannelRepoImpl(get()) }
    single<StorageRepo> { StorageRepoImpl() }


}

val viewModel = module {

    viewModel { EditProfileViewModel(get(),get(),get()) }
    viewModel { LoginViewModel(get(),get()) }
    viewModel { NewChatViewModel(get (),get()) }
    viewModel { SplashViewModel(get ()) }
    viewModel { HomeViewModel(get (),get()) }
    viewModel { ChatViewModel(get ()) }
    viewModel { UserProfileViewModel(get ()) }

}


