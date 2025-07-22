package com.example.mychatapp.domain.remote


interface OtherRepo{
    suspend fun getServiceAccountJson() : String

}