package com.apptickar.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomRequest(val receiverNumberId : String)