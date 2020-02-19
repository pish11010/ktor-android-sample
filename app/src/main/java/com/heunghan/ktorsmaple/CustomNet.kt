package com.heunghan.ktorsmaple

import com.google.gson.annotations.SerializedName
import io.ktor.http.HttpMethod

object CustomNet {
    internal object GithubApi {
        private const val path = "/"
        private const val userPath = "/users"
        private const val userSearchPath = "/search/users"
        fun get(): GithubResponse? = CustomHttpClient.request(HttpMethod.Get, path)

        fun getUser(id: String): GithubUser? = CustomHttpClient.request(HttpMethod.Get, "$userPath/$id")
        fun searchUsers(id: String): UserSearchResult? = CustomHttpClient.request(
                HttpMethod.Get, userSearchPath, query = mapOf("q" to id)
        )
    }
}

data class GithubResponse(
        @SerializedName("current_user_url") val current_user_url: Boolean
)

data class GithubUser(
        @SerializedName("name") val name: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("login") val login: String
)

data class UserSearchResult(
        @SerializedName("total_count") val total_count: Int,
        @SerializedName("items") val items: List<GithubUser>
)