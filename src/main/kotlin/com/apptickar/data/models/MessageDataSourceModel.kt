package com.apptickar.data.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageDataSourceModel(val dataSourceId :  String , val messages : MutableList<Message>)
