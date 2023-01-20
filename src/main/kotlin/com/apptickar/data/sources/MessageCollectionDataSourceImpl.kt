package com.apptickar.data.sources

import com.apptickar.data.models.Message
import com.apptickar.data.models.MessageDataSourceModel
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.push

class MessageCollectionDataSourceImpl(db : CoroutineDatabase) : MessageCollectionDataSource {

   private val  messageCollectionDataBase = db.getCollection<MessageDataSourceModel>()
  private suspend fun addDataSource(roomId: String): Boolean {
         return messageCollectionDataBase.insertOne(MessageDataSourceModel(dataSourceId =  roomId, mutableListOf())).wasAcknowledged()
    }

    override suspend fun getDataSource(roomId: String): MessageDataSourceModel {
      var dataSource = messageCollectionDataBase.findOne(MessageDataSourceModel::dataSourceId eq roomId)
        if (dataSource == null) {addDataSource(roomId);dataSource = getDataSource(roomId)}
        return  dataSource
    }

    override suspend fun getAllDataSources(): List<MessageDataSourceModel> {
       return messageCollectionDataBase.find().descendingSort().toList()
    }

    override suspend fun addMessageToDataSource(message: Message , dataSourceId : String): Boolean {
        return messageCollectionDataBase.updateOne(MessageDataSourceModel::dataSourceId eq dataSourceId ,
            push(MessageDataSourceModel::messages ,message)).wasAcknowledged()
    }
}