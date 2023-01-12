package com.apptickar.data.sources

import com.apptickar.data.models.User
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq


class MongoUserDataSource(private val database : CoroutineDatabase) : UserDataSource {


    private val users = database.getCollection<User>()
    override suspend fun getUser(username: String): User? {
        return users.findOne(User::numberId eq username)
    }

    override suspend fun insertNewUser(user: User): Boolean {
        return  users.insertOne(user).wasAcknowledged()
    }
}