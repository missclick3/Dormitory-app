package com.example.dormitory_app.feature_profile.domain.usecases

data class ProfileUseCases(
    val getUserInfoUseCase: GetUserInfoUseCase,
    val patchFieldUseCase: PatchFieldUseCase,
    val authenticateUseCase: AuthenticateUseCase,
    val logoutUseCase: LogoutUseCase
)