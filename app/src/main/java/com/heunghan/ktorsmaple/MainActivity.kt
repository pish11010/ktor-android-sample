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
        val test = CustomNet.GithubApi.test()
        Log.d("ktor-sample", "test: $test")
        val github = CustomNet.GithubApi.get()
        Log.d("ktor-sample", "github: $github")
        val user = CustomNet.GithubApi.getUser("pish11010")
        Log.d("ktor-sample", "user: $user")
        val users = CustomNet.GithubApi.searchUsers("pish11010")
        Log.d("ktor-sample", "users: $users")
    }

    private fun requestResultExample() {
        val test = CustomNet.GitApi.test()
        Log.d("ktor-sample", "test: $test")
        val github = CustomNet.GitApi.get()
        Log.d("ktor-sample", "github: $github")
        val users = CustomNet.GitApi.searchUsers("pish11010")
        Log.d("ktor-sample", "users: $users")
    }

}