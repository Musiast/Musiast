package com.example.musiast

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private fun String.encodeParam(): String = URLEncoder.encode(this, "utf-8")

object LyricsFinder {
    private const val MIN_LENGTH = 100

    suspend fun find(title: String): String? = withContext(Dispatchers.IO) {
        val links = bing("$title lyrics")
        for (link in links) {
            val lyrics = findLyrics(link)

            if (lyrics != null && lyrics.length > MIN_LENGTH) return@withContext lyrics
        }

        null
    }

    private suspend fun findLyrics(url: String): String? = withContext(Dispatchers.IO) {
        val response = getHtmlContent(url)

        val elements = Jsoup.parse(response).body().select("*")

        var maxBr = 0
        var result: Element? = null

        elements.forEach { element ->
            val brElements = element.children().count { it.tag()?.name.equals("br") }

            if (brElements > maxBr) {
                maxBr = brElements
                result = element
            }
        }

        result?.let(::getText)
    }

    private fun getText(element: Element): String {
        val sb = StringBuilder()

        for (child in element.childNodes()) {
            if (child is TextNode)
                sb.append(child.text())

            if (child is Element) {
                if (child.tag().name.equals("br", ignoreCase = true))
                    sb.append("\n")

                sb.append(getText(child))
            }
        }

        return sb.toString()
    }

    private suspend fun bing(query: String): List<String> = withContext(Dispatchers.IO) {
        val uri = "https://www.bing.com/search?q=${query.encodeParam()}&go=Search&qs=ds&form=QBRE"

        val response = getHtmlContent(uri)

        Jsoup.parse(response)
            .getElementById("b_content")
            .getElementById("b_results")
            .getElementsByClass("b_algo")
            .flatMap { it.getElementsByTag("h2") }
            .mapNotNull {
                runCatching {
                    val href = it.getElementsByTag("a").attr("href")

                    if (href.startsWith("http") && !href.contains("https://www.bing.com")) href
                    else null
                }.getOrNull()
            }
    }

    private suspend fun getHtmlContent(url: String): String = withContext(Dispatchers.IO) {
        val httpURLConnection = URL(url).openConnection() as HttpURLConnection

        httpURLConnection.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36"
        )

        httpURLConnection.inputStream.bufferedReader().readText()
    }
}