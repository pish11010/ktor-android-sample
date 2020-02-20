package com.heunghan.ktorsmaple

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestExample()
        requestResultExample()
    }

    private fun requestExample() {
        val test = CustomApi.GithubApi.test()
        Log.d("ktor-sample", "test: $test")
        val github = CustomApi.GithubApi.get()
        Log.d("ktor-sample", "github: $github")
        val user = CustomApi.GithubApi.getUser("pish11010")
        Log.d("ktor-sample", "user: $user")
        val users = CustomApi.GithubApi.searchUsers("pish11010")
        Log.d("ktor-sample", "users: $users")
    }

    private fun requestResultExample() {
        val test = CustomApi.GitApi.test()
        Log.d("ktor-sample", "test: $test")
        val github = CustomApi.GitApi.get()
        Log.d("ktor-sample", "github: $github")
        val users = CustomApi.GitApi.searchUsers("pish11010")
        Log.d("ktor-sample", "users: $users")
    }

}