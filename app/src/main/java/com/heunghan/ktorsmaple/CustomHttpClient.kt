package com.heunghan.ktorsmaple

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
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
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
            HttpResponseValidator {
                validateResponse {
                    it.status
                }
                handleResponseException {
                    it.message
                }

            }
        }
    }

    inline fun <reified T> request(
            method: HttpMethod,
            path: String,
            query: Map<String, Any> = emptyMap(),
            body: Map<String, Any> = emptyMap()
    ): T? {
        val client = defaultClient
        try {
            return runBlocking {
                return@runBlocking client.request<T?> {
                    this.method = method

                    url {
                        encodedPath = path
                        query.forEach { (key, value) ->
                            parameter(key, value)
                        }
                    }

                    if (body.isNotEmpty()) {
                        this.body = Gson().toJson(body)
                    }
                }
            }
        } catch (cause: Throwable) {
            return null
        }
    }
}