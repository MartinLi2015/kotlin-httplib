package com.zxkj.httplib

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zxkj.httplib.service.APIService
import com.zxkj.httplib.service.BaseUrlConfigImpl
import com.zxkj.httplib.service.HostType
import com.zxkj.httplib.ui.theme.HttpLibTheme
import com.zxkj.libhttp.RetrofitClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HttpLibTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        GlobalScope.launch {

            val retrofitClient =
                RetrofitClient.getInstance(this@MainActivity).setBaseUrlConfig(BaseUrlConfigImpl())
            val apiService =
                retrofitClient.getDefault(APIService::class.java, HostType.WWW.hostType)
            val result = apiService.autoNumber("123456789")
            Log.d(TAG, "onCreate: $result")

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HttpLibTheme {
        Greeting("Android")
    }
}