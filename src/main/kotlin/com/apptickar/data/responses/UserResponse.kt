package com.apptickar.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val numberId : String,val username : String)
