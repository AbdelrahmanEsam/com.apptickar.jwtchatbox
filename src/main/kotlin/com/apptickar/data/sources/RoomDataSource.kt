package com.apptickar.data.sources

import com.apptickar.data.models.RoomControllerModel


interface RoomDataSource {

   suspend  fun getRoom(roomId:String) : RoomControllerModel

  suspend fun addRoom(roomId: String) : Boolean

  suspend fun updateRoomMembers(roomId: String,newMember : String) : Boolean
}