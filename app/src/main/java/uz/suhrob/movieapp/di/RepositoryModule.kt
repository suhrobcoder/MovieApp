package uz.suhrob.movieapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.suhrob.movieapp.data.SharedPref
import uz.suhrob.movieapp.data.api.RemoteDataSource
import uz.suhrob.movieapp.data.db.dao.GenreDao
import uz.suhrob.movieapp.data.db.dao.MovieDao
import uz.suhrob.movieapp.data.repository.MovieRepository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context) =
        SharedPref(context)

    @Singleton
    @Provides
    fun provideMovieRepository(remoteDataSource: RemoteDataSource, movieDao: MovieDao, genreDao: GenreDao, pref: SharedPref) =
        MovieRepository(remoteDataSource, movieDao, genreDao, pref)
}