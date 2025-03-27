package com.example.chatterbox.auth.domain

class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<Unit> {
        // The use case might seem redundant now, but it establishes a clean contract for a
        // business operation that can be expanded in the future.
        return authRepository.signInWithGoogle(idToken)
    }
}