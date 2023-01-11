package com.apptickar.data.sources

import com.apptickar.data.models.User

interface UserDataSource {

    suspend fun getUser(username : String) : User?

    suspend fun insertNewUser(user : User) : Boolean

}