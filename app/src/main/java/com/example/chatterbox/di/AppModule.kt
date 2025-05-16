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
    single<AuthRepository> { FirebaseAuthRepository() }
    single<UserRepository> { FirestoreUserRepository() }
    single<UserChatRepository> { FirestoreUserChatRepository() }

    viewModelOf(::AuthViewModel)
//    viewModelOf(::UserViewModel)
    single<UserViewModel> { UserViewModel(get()) }
    single<UserChatViewModel> { UserChatViewModel(get(), get()) }

}