package com.heunghan.ktorsmaple

import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.features.*
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import kotlinx.coroutines.runBlocking

object CustomHttpClient {
    val defaultClient = createClient(
            BuildConfig.CUSTOM_SERVER_HOST
    )

    private fun createClient(
            host: String,
            port: Int? = null,
            header: Map<String, String> = emptyMap()
    ): HttpClient {
        return HttpClient {
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
            HttpResponseValidator {
                validateResponse { }
            }
        }
    }

    inline fun <reified T> request(
            method: HttpMethod,
            path: String,
            query: Map<String, Any> = emptyMap(),
            body: Map<String, Any> = emptyMap(),
            appendHeader: Map<String, Any> = emptyMap()
    ): T? {
        return runBlocking {
            return@runBlocking try {
                defaultClient.request<T>(
                        createRequestBuilder(method, path, query, body, appendHeader)
                )
            } catch (cause: Throwable) {
                null
            }
        }
    }

    fun createRequestBuilder(
            method: HttpMethod,
            path: String,
            query: Map<String, Any>,
            body: Map<String, Any>,
            appendHeader: Map<String, Any>
    ): HttpRequestBuilder.() -> Unit = {
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