package com.example.chatterbox.di

import com.example.chatterbox.auth.data.FirebaseAuthRepository
import com.example.chatterbox.auth.domain.AuthRepository
import com.example.chatterbox.auth.presentation.AuthViewModel
import com.example.chatterbox.chat.userChats.data.FirestoreUserChatRepository
import com.example.chatterbox.chat.userChats.domain.UserChatRepository
import com.example.chatterbox.chat.userChats.presentation.UserChatViewModel
import com.example.chatterbox.chat.users.data.FirestoreUserRepository
import com.example.chatterbox.chat.users.domain.UserRepository
import com.example.chatterbox.chat.users.presentation.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository>(createdAtStart = false) { FirebaseAuthRepository() }
    single<UserRepository>(createdAtStart = false) { FirestoreUserRepository() }
    single<UserChatRepository>(createdAtStart = false) { FirestoreUserChatRepository() }

    single<AuthViewModel>(createdAtStart = false) { AuthViewModel(get(), get()) }
//    viewModelOf(::UserViewModel)
    single<UserViewModel>(createdAtStart = false) { UserViewModel(get()) }
    single<UserChatViewModel>(createdAtStart = false) { UserChatViewModel(get(), get()) }

}