package com.zxkj.httplib.todo

import androidx.compose.runtime.Composable

@Composable
fun TodoApp() {
    val viewModel = TodoViewModel()
    viewModel.addItem(TodoItem(1,"我是第一条","记得回去写日志",true))
    viewModel.autoNumber("5302015184919")
    val todoItems = viewModel.todoItems


    TodoListScreen(todoItems = todoItems)
}