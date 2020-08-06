package uz.suhrob.movieapp.data

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val pref = context.getSharedPreferences("movie_app", Context.MODE_PRIVATE)

    var genreLoaded: Boolean
        get() = pref.getBoolean("genre_loaded", false)
        set(value) = pref.editMe { it.putBoolean("genre_loaded", value) }

    var popularMoviesLoadedPages: Int
        get() = pref.getInt("popular_pages", 0)
        set(value) = pref.editMe { it.putInt("popular_pages", value) }

    private inline fun SharedPreferences.editMe(listener: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        listener.invoke(editMe)
        editMe.apply()
    }
}