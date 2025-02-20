package com.example.mychatapp.helper.fcm

import android.util.Base64
import com.google.gson.Gson

object Base64Util {

    fun encodeAsJson(any: Any) : String{
        val json = Gson().toJson(any).toByteArray()
        return  String(Base64.encode(json,Base64.DEFAULT))
    }
    inline fun <reified T > decodeJson(data : String) : T{
        return  Gson().fromJson(
            String(
                Base64.decode(data,Base64.DEFAULT)
            ),
                T::class.java
            )
    }
}