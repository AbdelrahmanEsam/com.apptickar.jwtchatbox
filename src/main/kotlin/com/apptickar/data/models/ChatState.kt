package com.apptickar.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class ChatState(val numberId : String
, val isOnline : Boolean = false,val imageUrl : String = "" ,val lastMessage : String,val lastSeen : String,val unseenMessages : Int)
