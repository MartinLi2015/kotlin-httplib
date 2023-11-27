package com.zxkj.httplib.service

import retrofit2.http.GET

interface APIService {

    @GET("/autonumber/auto")
    suspend fun autoNumber(num:String):String
}