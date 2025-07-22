package com.example.mychatapp.helper.fcm

import android.util.Base64
import com.google.gson.Gson

object Base64Util {

    fun encodeAsJson(any: Any,gson: Gson = Gson()) : String{
        val json = gson.toJson(any).toByteArray()
        return  String(Base64.encode(json,Base64.DEFAULT))
    }
    inline fun <reified T > decodeJson(data : String,gson: Gson = Gson()) : T{
        return  gson.fromJson(
            String(
                Base64.decode(data,Base64.DEFAULT)
            ),
                T::class.java
            )
    }
}