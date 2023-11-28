package com.zxkj.httplib.todo

data class TodoItem(
    val id: Int,
    val title: String,
    val description: String,
    var isComplete: Boolean = false
)