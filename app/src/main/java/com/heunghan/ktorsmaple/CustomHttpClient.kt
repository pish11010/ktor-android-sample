package com.heunghan.ktorsmaple

import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.ktor.client.HttpClient
import io.ktor.client.features.*
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.*
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import kotlinx.coroutines.runBlocking

object CustomHttpClient {
    val defaultClient = createClient(
        BuildConfig.CUSTOM_SERVER_HOST
    )

    val gson = GsonBuilder()
        .serializeNulls()
        .create()

    private fun createClient(
        host: String, port: Int? = null, header: Map<String, String> = emptyMap()
    ) = HttpClient {
        defaultRequest {
            url {
                this.host = host
                port?.let { this.port = it }
                header.forEach { (key, value) ->
                    header(key, value)
                }
            }
        }
        install(UserAgent) {
            agent = "Android" +
                    "/${BuildConfig.VERSION_NAME}" +
                    "/Android ${Build.VERSION.RELEASE}" +
                    "/${Build.MANUFACTURER} - ${Build.MODEL}"
        }
        install(JsonFeature) {
            serializer = GsonSerializer {
                serializeNulls()
                disableHtmlEscaping()
            }
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = when {
                BuildConfig.DEBUG -> LogLevel.ALL
                else -> LogLevel.NONE
            }
        }
        HttpResponseValidator {
            validateResponse { }
        }
    }
    
    inline fun <reified T> request(
        method: HttpMethod,
        path: String,
        query: Map<String, Any> = emptyMap(),
        body: Map<String, Any> = emptyMap(),
        appendHeader: Map<String, Any> = emptyMap()
    ) = runBlocking {
        try {
            val requestBuilder = createRequestBuilder(method, path, query, body, appendHeader)
            defaultClient.request<T>(requestBuilder)
        } catch (cause: Throwable) {
            null
        }
    }

    inline fun <reified DATA, reified ERROR_DATA> requestResult(
        method: HttpMethod,
        path: String,
        query: Map<String, Any> = emptyMap(),
        body: Map<String, Any> = emptyMap(),
        appendHeader: Map<String, Any> = emptyMap()
    ) = runBlocking {
        try {
            val requestBuilder = createRequestBuilder(method, path, query, body, appendHeader)
            val response = defaultClient.request<HttpResponse>(requestBuilder)
            val bodyStr = response.readText()
            val result: ResponseData<DATA, ERROR_DATA> = when (response.status.value) {
                in 200..299 -> ResponseData(response, data = gson.fromJson<DATA>(bodyStr))
                else -> ResponseData(response, errorData = gson.fromJson<ERROR_DATA>(bodyStr))
            }
            result
        } catch (cause: Throwable) {
            ResponseData<DATA, ERROR_DATA>(cause = cause)
        }
    }

    fun createRequestBuilder(
        method: HttpMethod,
        path: String,
        query: Map<String, Any>,
        body: Map<String, Any>,
        appendHeader: Map<String, Any>
    ) = HttpRequestBuilder().apply {
        this.method = method
        url {
            encodedPath = path
            appendHeader.forEach { (key, value) ->
                header(key, value)
            }
        }

        when (method) {
            HttpMethod.Get -> {
                query.forEach { (key, value) ->
                    parameter(key, value)
                }
                body.forEach { (key, value) ->
                    parameter(key, value)
                }
            }
            HttpMethod.Post,
            HttpMethod.Put,
            HttpMethod.Delete -> {
                if (body.isNotEmpty()) {
                    this.body = Gson().toJson(body)
                }
            }
        }
    }
}

inline fun <reified T> Gson.fromJson(string: String?): T? =
    fromJson(string, object : TypeToken<T>() {}.type)

data class ResponseData<DATA, ERROR_DATA> constructor(
    val response: HttpResponse? = null,
    val data: DATA? = null,
    val errorData: ERROR_DATA? = null,
    val cause: Throwable? = null
) {
    val isSuccess: Boolean by lazy { response?.status?.isSuccess() ?: false }
    val hasException: Boolean by lazy { cause != null }
}