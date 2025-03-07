package com.example.mychatapp.koin


import com.example.mychatapp.data.local.DataStoreUtil
import com.example.mychatapp.domain.local.repo.LocalRepo
import com.example.mychatapp.data.local.repoimpl.PreferenceRepoImpl
import com.example.mychatapp.data.remote.ChannelRepoImpl
import com.example.mychatapp.data.remote.OtherRepoImpl
import com.example.mychatapp.data.remote.StorageRepoImpl
import com.example.mychatapp.domain.remote.UserRepo
import com.example.mychatapp.data.remote.UserRepoImpl
import com.example.mychatapp.domain.remote.ChannelRepo
import com.example.mychatapp.domain.remote.OtherRepo
import com.example.mychatapp.domain.remote.StorageRepo
import com.example.mychatapp.domain.usecase.LastOnlineTSFetcher
import com.example.mychatapp.domain.usecase.LastOnlineTSUpdater
import com.example.mychatapp.domain.usecase.NewMessageNotifier
import com.example.mychatapp.helper.fcm.FcmSender
import com.example.mychatapp.presentation.editProfileScreen.EditProfileViewModel
import com.example.mychatapp.presentation.loginScreen.LoginViewModel
import com.example.mychatapp.presentation.chatScreen.ChatViewModel
import com.example.mychatapp.presentation.homeScreen.HomeViewModel
import com.example.mychatapp.presentation.newChatsScreen.NewChatViewModel
import com.example.mychatapp.presentation.newGroupChat.NewGroupChatViewModel
import com.example.mychatapp.presentation.splashScreen.SplashViewModel
import com.example.mychatapp.presentation.userProfileScreen.UserProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single { DataStoreUtil.create(get()) }
    single<LocalRepo> { PreferenceRepoImpl(get()) }
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
    single<UserRepo> { UserRepoImpl(get()) }
    single<ChannelRepo> { ChannelRepoImpl(get()) }
    single<StorageRepo> { StorageRepoImpl() }
    single<OtherRepo> { OtherRepoImpl() }


    single { HttpClient(CIO){expectSuccess = true} }
    single { FcmSender(get()) }
    single { NewMessageNotifier(get(),get(),get()) }
    single { LastOnlineTSUpdater(get(),get()) }
    single { LastOnlineTSFetcher(get()) }


}

val viewModel = module {

    viewModel { EditProfileViewModel(get(),get(),get()) }
    viewModel { LoginViewModel(get(),get()) }
    viewModel { NewChatViewModel(get (),get()) }
    viewModel { NewGroupChatViewModel(get (),get(),get()) }
    viewModel { SplashViewModel(get ()) }
    viewModel { HomeViewModel(get (),get(),get()) }
    viewModel { ChatViewModel(get (),get(),get(),get(),get()) }
    viewModel { UserProfileViewModel(get ()) }

}


