package com.heunghan.ktorsmaple

import com.google.gson.annotations.SerializedName
import io.ktor.http.HttpMethod

object CustomApi {
    internal object GithubApi {
        private const val path = "/"
        private const val userPath = "/users"
        private const val userSearchPath = "/search/users"
        fun test(): String? = CustomHttpClient.request(HttpMethod.Get, path + "123")
        fun get(): GithubResponse? = CustomHttpClient.request(HttpMethod.Get, path)

        fun getUser(id: String): GithubUser? =
            CustomHttpClient.request(HttpMethod.Get, "$userPath/$id")

        fun searchUsers(id: String): UserSearchResult? = CustomHttpClient.request(
            HttpMethod.Get, userSearchPath, query = mapOf("q" to id)
        )
    }

    internal object GitApi {
        private const val path = "/"
        private const val userSearchPath = "/search/users"
        fun test(): NetResult<Unit, GithubError> =
            CustomHttpClient.requestResult(HttpMethod.Get, path + "123")

        fun get(): NetResult<GithubResponse, Unit> = CustomHttpClient.requestResult(HttpMethod.Get, path)

        fun searchUsers(id: String): NetResult<UserSearchResult, Unit> =
            CustomHttpClient.requestResult(
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

data class GithubError(
    @SerializedName("message") val message: String,
    @SerializedName("documentation_url") val documentation_url: String
)