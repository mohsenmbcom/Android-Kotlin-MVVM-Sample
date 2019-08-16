package com.mohsenmb.googlenewsapisample.repository.webservice

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

const val NO_MORE_DATA = "NO_MORE_DATA"
const val NO_MORE_ONLINE = "NO_MORE_ONLINE"

inline fun parseError(gson: Gson, crossinline callback: (Error) -> Unit): (Throwable) -> Unit {
    return {
        callback(HttpErrorParser.getInstance(gson).parseError(it))
    }
}

class HttpErrorParser private constructor(val gson: Gson) {

    companion object {
        private lateinit var instance: HttpErrorParser

        fun getInstance(gson: Gson): HttpErrorParser {
            if (::instance.isInitialized) {
                return instance
            } else {
                instance = HttpErrorParser(gson)
                return instance
            }
        }
    }

    fun parseError(throwable: Throwable?): Error = when (throwable) {
        is HttpException -> {
            val response = try {
                gson.fromJson(throwable.response()!!.errorBody()!!.string(), HttpErrorData::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                HttpErrorData("error", "unknown", "Unexpected error!")
            }
            Error(ErrorType.Http, response)
        }
        is SocketTimeoutException -> Error(ErrorType.Timeout)
        is IOException -> Error(ErrorType.NetworkOffline)
        is UnsupportedOperationException -> when {
            throwable.message == NO_MORE_ONLINE -> Error(ErrorType.NoMoreOnlineData)
            throwable.message == NO_MORE_DATA -> Error(ErrorType.NoMoreDate)
            else -> Error(ErrorType.Unknown)
        }
        else -> Error(ErrorType.Unknown)
    }
}

data class Error(
    val type: ErrorType,
    val data: HttpErrorData? = null
)

data class HttpErrorData(
    @SerializedName("status")
    val status: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String
)

enum class ErrorType {
    NoError,
    Http,
    Timeout,
    NetworkOffline,
    NoMoreOnlineData,
    NoMoreDate,
    Unknown
}

fun noError(): Error = Error(ErrorType.NoError, null)