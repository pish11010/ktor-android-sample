package com.heunghan.ktorsmaple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
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
        val client = HttpClient() {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
        val result: Github = client.get("https://api.github.com")
        client.close()
    }

    private data class Github(
        @SerializedName("current_user_url") val currentUserUrl: String
    )
}