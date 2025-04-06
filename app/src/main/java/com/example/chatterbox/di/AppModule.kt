package com.example.chatterbox.di

import com.example.chatterbox.auth.data.FirebaseAuthRepository
import com.example.chatterbox.auth.domain.AuthRepository
import com.example.chatterbox.auth.presentation.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { FirebaseAuthRepository() }

    viewModelOf(::AuthViewModel)

}