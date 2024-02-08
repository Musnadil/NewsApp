package com.musnadil.newsapp.utility

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class FetchFaviconTask(private val callback: (String) -> Unit) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String?): String {
        val url = params[0]

        try {
            val document: Document = Jsoup.connect(url).get()
            val faviconElements: Elements = document.select("link[rel~=(?i)icon]")

            if (faviconElements.isNotEmpty()) {
                val faviconUrl = faviconElements.attr("href")
                return faviconUrl
            }
        } catch (e: IOException) {
            Log.e("FetchFaviconTask", "Error fetching favicon", e)
        }

        return ""
    }

    override fun onPostExecute(result: String) {
        if (result.isNotEmpty()) {
            Log.d("Favicon URL", result)
            callback(result)
        } else {
            Log.e("Favicon URL", "Failed to fetch favicon")
        }
    }
}

//        val websiteUrl = "https://id.pinterest.com"
//        val fetchFaviconTask = FetchFaviconTask { faviconUrl ->
//            // Handle the fetched favicon URL as needed
//            if (faviconUrl.isNotEmpty()) {
//                // Now you can use the fetched favicon URL in your activity
//                // For example, load it into an ImageView using an image loading library
//                Log.d("iconweb",faviconUrl)
//
//            } else {
//                // Handle the case where fetching favicon failed
//            }
//        }
//        fetchFaviconTask.execute(websiteUrl)