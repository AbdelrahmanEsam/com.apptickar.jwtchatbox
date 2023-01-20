package com.apptickar.data.sources

import com.apptickar.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.push
import org.litote.kmongo.setValue


class UserDataSourceImpl(database : CoroutineDatabase) : UserDataSource {


    private val users = database.getCollection<User>()

    override suspend fun getUser(numberId: String): List<User> {

        return users.find(User::numberId eq numberId).toList()
    }



    override suspend fun insertNewUser(user: User): Boolean {
        return  users.insertOne(user).wasAcknowledged()
    }

    override suspend fun updateUserChatRooms(userId : String, roomId : String): Boolean {
        val user = getUser(userId)
        if(user.first().chatsSet.contains(roomId)) return true
        return users.updateOne(User::numberId eq userId , push(User::chatsSet ,roomId)).wasAcknowledged()
    }

    override suspend fun updateUserState(userId: String,isOnline: Boolean): Boolean {
        return users.updateOne(User::numberId eq userId, setValue(User::isOnline,isOnline)).wasAcknowledged()
    }
}