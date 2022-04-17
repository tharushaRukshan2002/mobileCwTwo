package com.example.mobilecwtwo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.mobilecwtwo.dataBase.AppDatabase
import com.example.mobilecwtwo.dataBase.Movie
import com.example.mobilecwtwo.dataBase.MovieDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class SearchActorActivity : AppCompatActivity() {

    private lateinit var movieDao: MovieDao
    private lateinit var setTextField: TextView
    private lateinit var mList: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actor)

        setTextField = findViewById(R.id.sMovieNameDisplayTxtActor)
        val textField = findViewById<EditText>(R.id.ActorNameTxtField)
        val dataBase = Room.databaseBuilder(this, AppDatabase::class.java, "Movie DB").build()
        movieDao = dataBase.movieDao()

        val btn = findViewById<Button>(R.id.searchActorBtn)
        btn.setOnClickListener {
            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        val keyWord = textField.text.toString()
                        if (keyWord != "") {
                            mList = movieDao.findMovieByActor(keyWord)
                        }
                    }
                    getDetailsAndSetText()
                }
            }

        }
    }

    private fun getDetailsAndSetText() {
        try {
            setTextField.text = null
            Log.i("ado", mList.toString())
            Log.i("ado", mList.size.toString())
            for (element in mList) {
                setTextField.append("Title: \"${element.title}\"")
                setTextField.append("\nYear: ${element.year}")
                setTextField.append("\nRated: ${element.rated}")
                setTextField.append("\nReleased: ${element.released}")
                setTextField.append("\nRuntime: ${element.runtime}")
                setTextField.append("\nGenre: ${element.genre}")
                setTextField.append("\nDirector: ${element.director}")
                setTextField.append("\nWriter: ${element.writers}")
                setTextField.append("\nActors: ${element.actors}")
                setTextField.append("\n\nPlot: \"${element.plot}\"\n")
                setTextField.append("-------------------------------------\n\n")
            }
        } catch (e: Exception) {
            Log.i("ado", e.toString())
        }

    }
}