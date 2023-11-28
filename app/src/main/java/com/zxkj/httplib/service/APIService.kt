package com.zxkj.httplib.service

import com.zxkj.httplib.bean.AutoDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("/autonumber/autoComNum")
    suspend fun autoNumber(@Query("text") num:String): AutoDTO
}