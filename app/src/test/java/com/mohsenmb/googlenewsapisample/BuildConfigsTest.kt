package com.mohsenmb.googlenewsapisample

import org.junit.Test

/**
 * A simple test to check if the build config fields are set
 */
class BuildConfigsTest {
    @Test
    fun buildConfig_apiKey_isSet() {
        val apiKey = BuildConfig.NEWS_API_KEY
        println("News api key is $apiKey")
        assert(apiKey.isNotBlank())
    }

    @Test
    fun buildConfig_apiBaseUrl_isSet() {
        val baseUrl = BuildConfig.API_BASE_URL
        println("API base url is $baseUrl")
        assert(baseUrl.isNotBlank())
    }
}
