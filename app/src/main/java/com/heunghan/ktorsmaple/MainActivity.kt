package com.heunghan.ktorsmaple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        request()
    }

    private fun request() {
        CoroutineScope(Dispatchers.IO).launch {
            get()
        }
    }

    private suspend fun get() {
        val client = HttpClient()

        val result = client.get<String>("https://api.github.com")

        client.close()
    }
}