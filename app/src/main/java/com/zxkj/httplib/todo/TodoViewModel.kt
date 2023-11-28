package com.zxkj.httplib.todo

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxkj.httplib.MyApplication
import com.zxkj.httplib.service.APIService
import com.zxkj.httplib.service.BaseUrlConfigImpl
import com.zxkj.httplib.service.HostType
import com.zxkj.libhttp.RetrofitClient
import kotlinx.coroutines.launch

class TodoViewModel() : ViewModel() {
    private val TAG = "TodoViewModel"
    private val _todoItems = mutableStateListOf<TodoItem>()
    val todoItems: List<TodoItem> get() = _todoItems

    fun addItem(item: TodoItem) {
        _todoItems.add(item)
    }

    fun removeItem(item: TodoItem) {
        _todoItems.remove(item)
    }

    fun updateItem(item: TodoItem) {
        val index = _todoItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            _todoItems[index] = item
        }
    }

    fun autoNumber(expNumber: String) {
        viewModelScope.launch {
            val apiService = RetrofitClient.getInstance(MyApplication.getApp())
                .setBaseUrlConfig(BaseUrlConfigImpl())
                .getDefault(APIService::class.java, HostType.WWW.hostType)
           val result =  apiService.autoNumber(expNumber)
            Log.d(TAG, "autoNumber: $result")
        }

    }
}