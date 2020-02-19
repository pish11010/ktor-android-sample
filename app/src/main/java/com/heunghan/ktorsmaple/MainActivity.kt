package com.heunghan.ktorsmaple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import io.ktor.http.HttpMethod

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        request()
    }

    private fun request() {
        val result: GithubResponse? = CustomHttpClient.request(
                HttpMethod.Get, "/"
        )
    }

    private data class GithubResponse(
            @SerializedName("current_user_url") val isActive: Boolean
    )
}