package com.zxkj.httplib.bean

data class AutoDTO(
    var num:String,
    var auto:List<AutoBean>
)

data class AutoBean(
    var comCode:String,
    var name:String,
)
