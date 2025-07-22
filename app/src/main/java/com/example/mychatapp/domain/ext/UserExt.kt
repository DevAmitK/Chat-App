package com.example.mychatapp.domain.ext

import com.example.mychatapp.domain.model.User
import com.example.mychatapp.ui.comp.placeHolder

fun User.id() : String{
    return id
}

fun User.imageUri () : String{
    return this.imageUri ?: placeHolder(name)
}