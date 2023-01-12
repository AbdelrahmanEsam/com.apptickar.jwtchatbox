package com.apptickar.data.sources

import com.apptickar.data.models.Message


interface MessageDataSource {

    suspend fun getAllMessages(): List<Message>

    suspend fun insertMessage(message: Message)
}