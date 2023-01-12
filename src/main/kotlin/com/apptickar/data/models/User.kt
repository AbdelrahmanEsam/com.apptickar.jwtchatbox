package com.apptickar.data.models


data class User(val numberId : String,val userName : String , val password : String,val salt:String,val chatsList : MutableList<String> = mutableListOf())
