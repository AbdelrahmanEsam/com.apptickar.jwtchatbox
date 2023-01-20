package com.apptickar.data.sources

import com.apptickar.data.models.RoomControllerModel
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.push


class RoomDataSourceImpl(db: CoroutineDatabase, private val messageCollectionDataSource: MessageCollectionDataSource) : RoomDataSource{

   private val rooms = db.getCollection<RoomControllerModel>()
    override suspend fun getRoom(roomId: String): RoomControllerModel {
       var roomController  =  rooms.findOne(RoomControllerModel::roomId eq roomId)
        if (roomController == null)
        {
            addRoom(roomId)
            roomController =  rooms.findOne(RoomControllerModel::roomId eq roomId)
        }
        return roomController!!
    }

    override suspend  fun addRoom(roomId: String): Boolean {
     messageCollectionDataSource.getDataSource(roomId) // if there is no data source with this id create it
      return rooms.insertOne(RoomControllerModel(roomId = roomId, messageDataSource = roomId, members = hashSetOf())).wasAcknowledged()
    }

    override suspend fun updateRoomMembers(roomId: String,newMember : String): Boolean {
        val room = getRoom(roomId)
        if (room.members.isNotEmpty()) return true
    return rooms.updateOne(RoomControllerModel::roomId eq roomId , push(RoomControllerModel::members,newMember)).wasAcknowledged()
    }
}