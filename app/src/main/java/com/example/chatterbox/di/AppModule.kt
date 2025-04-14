package com.example.chatterbox.di

import com.example.chatterbox.auth.data.FirebaseAuthRepository
import com.example.chatterbox.auth.domain.AuthRepository
import com.example.chatterbox.auth.presentation.AuthViewModel
import com.example.chatterbox.chat.users.data.FirestoreUserRepository
import com.example.chatterbox.chat.users.domain.UserRepository
import com.example.chatterbox.chat.users.presentation.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { FirebaseAuthRepository() }
    single<UserRepository> { FirestoreUserRepository() }

    viewModelOf(::AuthViewModel)
    viewModelOf(::UserViewModel)

//    scope(named("USER_SCOPE")) {
//        viewModelOf(::UserViewModel)
//    }

}