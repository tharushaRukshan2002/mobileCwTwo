package com.example.mobilecwtwo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.mobilecwtwo.dataBase.AppDatabase
import com.example.mobilecwtwo.dataBase.Movie
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovieActivity : AppCompatActivity() {

    private lateinit var displayTxt: TextView

    @OptIn(ObsoleteCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)

        val saveBtn = findViewById<Button>(R.id.saveMovieToDbBtn)
        val searchBtn = findViewById<Button>(R.id.searchActionBtn)
        val getTextField = findViewById<EditText>(R.id.movieNameTxtField)
        displayTxt = findViewById(R.id.sMovieNameDisplayTxt)
        var saveMovie: Movie? = null
        var details = StringBuilder()

        val dataBase = Room.databaseBuilder(this, AppDatabase::class.java, "Movie DB").build()
        val movieDao = dataBase.movieDao()

        searchBtn.setOnClickListener {
            details.clear()
            Log.i("detailsBefore", details.toString())
            Log.i("threadBefore", Thread.currentThread().toString())
            val keyWord = getTextField.text.toString()
            runBlocking {
                launch {
                    withContext(Dispatchers.Default) {
                        details = getDetails(keyWord)
                    }
                    saveMovie = toJsonArraySetText(details)
                }
            }
        }

        saveBtn.setOnClickListener {
            runBlocking {
                launch {
                    if (saveMovie != null) {
                        movieDao.insertMovies(saveMovie!!)
                    }
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
        val urlString = "https://www.omdbapi.com/?t=$text&apikey=f17e219"
        val url = URL(urlString)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection

        val bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) {
            details.append(line + "\n")
            line = bf.readLine()
        }
//        Log.i("movie", "$details")
//        Log.i("threadCurrent", Thread.currentThread().toString())
        return details
    }

    /**
     *the method will read the details and  will display as a text
     * @param stb string builder object
     * @return Movie object
     */
    private fun toJsonArraySetText(stb: java.lang.StringBuilder): Movie? {
        val items: List<String> = listOf(
            "Title", "Year", "Rated", "Released", "Runtime", "Genre",
            "Director", "Writer", "Actors", "Plot"
        )
        val values: MutableList<String> = mutableListOf();
        displayTxt.text = null
        val allDetails = java.lang.StringBuilder()
        val json = JSONObject(stb.toString())
        try {
            for (keyWord in items) {
                val value = json[keyWord] as String
                if (keyWord == "Title" || keyWord == "Plot") {
                    allDetails.append("$keyWord: \"$value\"\n")
                    values.add(value)
                } else if (keyWord == "Actors") {
                    allDetails.append("$keyWord: $value\n\n")
                    values.add(value)
                } else {
                    allDetails.append("$keyWord: $value\n")
                    values.add(value)
                }
            }
            displayTxt.append(allDetails)
            return Movie(
                values[0], values[1], values[2], values[3], values[4], values[5], values[6],
                values[7], values[8], values[9]
            )
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Not Valid", Toast.LENGTH_SHORT).show()
            Log.i("problem", e.toString())
        }
        return null


    }

}

