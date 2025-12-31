package com.lagradost.cloudstream3.plugins

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class WecimaProvider : MainAPI() {
    override var mainUrl = "https://wecima.show"
    override var name = "Wecima"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/search/$query"
        val doc = app.get(url).document
        return doc.select(".poster").map {
            val title = it.select("img").attr("alt")
            val link = it.select("a").attr("href")
            val poster = it.select("img").attr("src")
            newMovieSearchResponse(title, link, TvType.Movie) {
                this.posterUrl = poster
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url).document
        val title = doc.selectFirst("h1")?.text() ?: "No title"
        val poster = doc.selectFirst("img")?.attr("src")
        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster
        }
    }
}
