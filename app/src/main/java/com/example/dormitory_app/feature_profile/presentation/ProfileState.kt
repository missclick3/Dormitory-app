package com.example.dormitory_app.feature_profile.presentation

data class ProfileState(
    val isEmailChangeButtonToggled: Boolean = false,
    val isTgChangeButtonToggled: Boolean = false,
    val isPhoneNumberChangeButtonToggled: Boolean = false,
    val emailField: String? = "",
    val tgField: String? = "",
    val phoneNumberField: String? = ""
)
