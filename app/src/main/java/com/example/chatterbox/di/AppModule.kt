package com.example.chatterbox.di

import com.example.chatterbox.auth.data.FirebaseAuthRepository
import com.example.chatterbox.auth.domain.AuthRepository
import com.example.chatterbox.auth.presentation.AuthViewModel
import com.example.chatterbox.chat.groups.data.FirestoreGroupRepository
import com.example.chatterbox.chat.groups.domain.GroupRepository
import com.example.chatterbox.chat.groups.presentation.GroupChatViewModel
import com.example.chatterbox.chat.groups.presentation.GroupViewModel
import com.example.chatterbox.chat.shared.data.SupabaseStorageRepository
import com.example.chatterbox.chat.shared.domain.StorageRepository
import com.example.chatterbox.chat.userChats.data.FirestoreUserChatRepository
import com.example.chatterbox.chat.userChats.domain.UserChatRepository
import com.example.chatterbox.chat.userChats.presentation.ChatViewModel
import com.example.chatterbox.chat.userChats.presentation.UserChatViewModel
import com.example.chatterbox.chat.users.data.FirestoreUserRepository
import com.example.chatterbox.chat.users.domain.UserRepository
import com.example.chatterbox.chat.users.presentation.UserViewModel
import com.example.chatterbox.retrofit.fcm.FCMService
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<AuthRepository>(createdAtStart = false) { FirebaseAuthRepository() }
    single<UserRepository>(createdAtStart = false) { FirestoreUserRepository() }
    single<UserChatRepository>(createdAtStart = false) { FirestoreUserChatRepository() }
    single<GroupRepository>(createdAtStart = false) { FirestoreGroupRepository() }
    single<StorageRepository>(createdAtStart = false) { SupabaseStorageRepository() }

//    single<AuthViewModel>(createdAtStart = false) { AuthViewModel(get(), get()) }
//    viewModelOf(::UserViewModel)
//    single<UserViewModel>(createdAtStart = false) { UserViewModel(get()) }
//    single<UserChatViewModel>(createdAtStart = false) { UserChatViewModel(get(), get()) }

    viewModelOf(::AuthViewModel)
    viewModelOf(::UserViewModel)
    viewModelOf(::UserChatViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::GroupViewModel)
    viewModelOf(::GroupChatViewModel)

    single {
        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(FCMService::class.java)
    }

}