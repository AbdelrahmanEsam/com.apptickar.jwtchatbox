package com.apptickar.data.sources

import com.apptickar.data.models.Message
import com.apptickar.data.models.MessageDataSourceModel

interface MessageCollectionDataSource {


   suspend fun getDataSource(roomId: String) : MessageDataSourceModel?

 suspend  fun getAllDataSources() : List<MessageDataSourceModel>

 suspend fun addMessageToDataSource(message : Message, dataSourceId : String) : Boolean

}