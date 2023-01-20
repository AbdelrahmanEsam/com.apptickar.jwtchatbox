package com.apptickar.data.sources

import com.apptickar.data.models.User

interface UserDataSource {

    suspend fun getUser(numberId : String) : List<User>?


    suspend fun insertNewUser(user : User) : Boolean

    suspend fun updateUserChatRooms(userId: String, roomId: String) : Boolean

    suspend fun updateUserState(userId: String,isOnline : Boolean)  : Boolean



}