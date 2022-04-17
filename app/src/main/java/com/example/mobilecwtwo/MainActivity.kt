package com.example.mobilecwtwo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.room.Room
import com.example.mobilecwtwo.dataBase.AppDatabase
import com.example.mobilecwtwo.dataBase.Movie
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveBtn = findViewById<Button>(R.id.addMovieDbBtn)
        val toSearchMovie = findViewById<Button>(R.id.SearchMovieBtn)
        val toSearchActor = findViewById<Button>(R.id.SearchActorBtn)
        val toSearchAllActivity = findViewById<Button>(R.id.SearchMovieAll)

        val dataBase = Room.databaseBuilder(this, AppDatabase::class.java, "Movie DB").build()
        val movieDao = dataBase.movieDao()

        saveBtn.setOnClickListener {
            runBlocking {
                launch {
                    val movie1 = Movie("The Shawshank Redemption", "1994", "R","14 Oct 1994",
                        "142 min", "Drama", "Frank Darabont", "Stephen King, Frank Darabont",
                        "Tim Robbins, Morgan Freeman, Bob Gunton",
                        "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.")

                    val movie2 = Movie("Batman: The Dark Knight Returns, Part 1", "2012","PG-13",
                        "25 Sep 2012", "76 min","Animation, Action, Crime, Drama, Thriller",
                        "Jay Oliva","Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
                        "Peter Weller, Ariel Winter, David Selby, Wade Williams",
                        "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?")

                    val movie3 = Movie("The Lord of the Rings: The Return of the King", "2003", "PG-13",
                        "17 Dec 2003","201 min","Action, Adventure, Drama","Peter Jackson",
                        "J.R.R. Tolkien, Fran Walsh, Philippa Boyens", "Elijah Wood, Viggo Mortensen, Ian McKellen",
                        "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
                        )

                    val movie4 = Movie("Inception", "2010", "PG-13", "16 Jul 2010",
                        "148 min","Action, Adventure, Sci-Fi","Christopher Nolan",
                        "Christopher Nolan", "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
                        "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster."
                        )

                    val movie5 = Movie("The Matrix","1999","R","31 Mar 1999","136 min",
                        "Action, Sci-Fi","Lana Wachowski, Lilly Wachowski","Lilly Wachowski, Lana Wachowski",
                        "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
                        "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence."
                        )

                    movieDao.insertMovies(movie1, movie2, movie3, movie4, movie5)

                }
            }
        }

        toSearchMovie.setOnClickListener {
            val intentToSearch = Intent(this, SearchMovieActivity::class.java)
            startActivity(intentToSearch)
        }

        toSearchActor.setOnClickListener {
            val intentToSearch = Intent(this, SearchActorActivity::class.java)
            startActivity(intentToSearch)
        }

        toSearchAllActivity.setOnClickListener {
            val intentToSearchAll = Intent(this, SearchMoviesAllActivity::class.java)
            startActivity(intentToSearchAll)
        }
    }
}