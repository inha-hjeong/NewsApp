package com.faraonelife.newsappcompose.repo

import com.faraonelife.newsappcompose.models.ApiKey
import com.faraonelife.newsappcompose.models.IpInfoResponse
import com.faraonelife.newsappcompose.models.NewsResponse
import com.faraonelife.newsappcompose.network.RetrofitApiInterface
import javax.inject.Inject

interface RemoteDS {
    suspend fun getIpInfo(): IpInfoResponse
    suspend fun getTopHeadlines(countryCode: String, category: String): NewsResponse
    suspend fun getNews(query: String): NewsResponse
}


class RemoteDSImpl @Inject constructor(
    private val apiKey: ApiKey, private val api: RetrofitApiInterface
) : RemoteDS {
    override suspend fun getIpInfo(): IpInfoResponse {
        val url = "http://ip-api.com/json"
        return api.getIpInfo(url)
    }

    override suspend fun getTopHeadlines(countryCode: String, category: String): NewsResponse {
        return api.getTopHeadlines(apiKey.key, countryCode, category)
    }

    override suspend fun getNews(query: String): NewsResponse {
        return api.getNews(apiKey.key, query)
    }
}
