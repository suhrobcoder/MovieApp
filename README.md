# Movie App
Android app for displaying movies using the themoviedb api


## Libraries used in this project
* Coroutines
* Retrofit
* Room
* LiveData
* ViewModel
* Navigation Component
* Dagger-Hilt
* Glide


## Screenshots
Home Screen|Search Screen|Details Screen
:---------:|:-----------:|:------------:
![Home Screen](https://github.com/suhrobcoder/MovieApp/blob/master/screenshots/home.jpg) | ![Search Screen](https://github.com/suhrobcoder/MovieApp/blob/master/screenshots/search.jpg) | ![Details Screen](https://github.com/suhrobcoder/MovieApp/blob/master/screenshots/details.jpg)


## Developer setup
Get an api key from https://themoviedb.org
Create Constants.kt file in util packages
```Kotlin
class Constants {
    companion object {
        const val API_KEY = "<< themoviedb.org - API-KEY here >>"
    }
}
```
