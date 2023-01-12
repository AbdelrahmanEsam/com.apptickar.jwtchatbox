package com.apptickar.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(val username : String, val password : String,val numberId:String)
