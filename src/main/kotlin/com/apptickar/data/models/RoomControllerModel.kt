package com.apptickar.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RoomControllerModel(val roomId : String , val messageDataSource : String ,val  members : HashSet<String>)
