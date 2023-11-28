package com.zxkj.httplib.todo

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodoListScreen(todoItems: List<TodoItem>) {
    LazyColumn {
        items(todoItems) { item ->
            Row(
                modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = item.isComplete, onCheckedChange = { isChecked ->
                    item.isComplete = isChecked
                })
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item.title)
            }
        }
    }
}