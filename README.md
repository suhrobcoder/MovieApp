# Movie App
Android app for displaying movies using the themoviedb api


## Download
https://drive.google.com/file/d/1-I8CI_iDUTmMWtAmhpKnYu_F5E9kwt-a/view?usp=drivesdk


## Libraries used in this project
* Coroutines
* Retrofit
* Room
* LiveData
* ViewModel
* Navigation Component
* Dagger-Hilt


## Screenshots
Home Screen|Search Screen|Details Screen
:---------:|:-----------:|:------------:
![Home Screen](https://user-images.githubusercontent.com/57591490/89486770-39660700-d7bd-11ea-9001-8f5aaa4141fd.jpg) | ![Search Screen](https://user-images.githubusercontent.com/57591490/89486781-3ff47e80-d7bd-11ea-9a25-b1d03fde82c0.jpg) | ![Details Screen](https://user-images.githubusercontent.com/57591490/89486790-44b93280-d7bd-11ea-9da1-20898dff69ec.jpg)


## Developer setup
Get an api key from https://themoviedb.org
Create Constants.kt file in util package
```
class Constants {
    companion object {
        const val API_KEY = "<< themoviedb.org - API-KEY here >>"
    }
}
```
