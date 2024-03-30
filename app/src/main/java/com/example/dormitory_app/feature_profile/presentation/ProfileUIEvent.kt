package com.example.dormitory_app.feature_profile.presentation

sealed class ProfileUIEvent {
    data class EmailValueChanged(val emailValue: String) : ProfileUIEvent()
    data class TgValueChanged(val tgValue: String) : ProfileUIEvent()
    data class PhoneNumberValueChanged(val phoneNumberValue: String) : ProfileUIEvent()
    data object ApplyChanges : ProfileUIEvent()
    data object GetUserInfo : ProfileUIEvent()
}