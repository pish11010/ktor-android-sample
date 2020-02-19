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
        val github = CustomNet.GithubApi.get()
        val user = CustomNet.GithubApi.getUser("pish11010")
        val users = CustomNet.GithubApi.searchUsers("pish11010")
    }

}