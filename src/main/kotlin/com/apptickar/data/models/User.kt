package com.apptickar.data.models

import com.apptickar.data.responses.UserResponse
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId


@Serializable
data class User(@BsonId val numberId : String, val username : String, val password : String, val salt:String, val chatsSet : HashSet<String> = hashSetOf()
                , val isOnline : Boolean = false, val image : String = ""
)

fun User.mapToUserResponse() : UserResponse
{
   return UserResponse(numberId, username)
}
