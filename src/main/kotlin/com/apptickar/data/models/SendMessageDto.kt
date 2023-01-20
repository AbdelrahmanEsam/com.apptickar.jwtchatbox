package com.apptickar.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageDto(val receiverId : String , val message : String )