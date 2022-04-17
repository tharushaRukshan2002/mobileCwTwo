package com.example.mobilecwtwo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMoviesAllActivity : AppCompatActivity() {

    lateinit var allMovieDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies_all)

        val searchField = findViewById<EditText>(R.id.moviesNameTxtField)
        val searchBtn = findViewById<Button>(R.id.searchMoviesBtn)
        allMovieDisplay = findViewById(R.id.allMovieDisplay)
        var details = StringBuilder()



        searchBtn.setOnClickListener {
            val keyWord = searchField.text.toString()
            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        details = getDetails(keyWord)
                        Log.i("hy", details.toString())
                    }
                    toJsonArraySetText(details)
                }
            }
        }


    }
    /**
     *  method will return the details that it got from the url. inside the method the connection will be made
     *  p.s this method has to be call in a dispatcher.
     *  @param text keyword to search
     *  @return will return a string
     */
    private fun getDetails(text: String): java.lang.StringBuilder {
        val details = StringBuilder()
        val urlString = "https://www.omdbapi.com/?s=$text&apikey=f17e219"
        val url = URL(urlString)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection

        val bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) {
            details.append(line + "\n")
            line = bf.readLine()
        }
        return details
    }

    /**
     *the method will read the details and  will display as a text
     * @param stb string builder object
     * @return Movie object
     */
    private fun toJsonArraySetText(stb: java.lang.StringBuilder) {
        val items: List<String> = listOf(
            "Title", "Year", "imdbID", "Type"
        )
        val values: MutableList<String> = mutableListOf();
        allMovieDisplay.text = null
        val allDetails = java.lang.StringBuilder()
        val jsonTxt = JSONObject(stb.toString())
        val jsonMovies: JSONArray = jsonTxt.getJSONArray("Search")
        try {
            for (movie in 0 until jsonMovies.length()) {
                val one: JSONObject = jsonMovies[movie] as JSONObject
                for (keyWord in items) {
                    val value = one[keyWord] as String
                    if (keyWord == "Title") {
                        allDetails.append("$keyWord: \"$value\"\n")
                        values.add(value)
                    } else {
                        allDetails.append("$keyWord: $value\n")
                        values.add(value)
                    }
                }
                allDetails.append("\n\n")
            }

            allMovieDisplay.append(allDetails)

        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Not Valid", Toast.LENGTH_SHORT).show()
            Log.i("problem", e.toString())
        }

    }

}