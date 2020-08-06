package uz.suhrob.movieapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.suhrob.movieapp.data.db.AppDatabase
import uz.suhrob.movieapp.data.db.dao.GenreDao
import uz.suhrob.movieapp.data.db.dao.MovieDao
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao =
        database.getMovieDao()

    @Singleton
    @Provides
    fun provideGenreDao(database: AppDatabase): GenreDao =
        database.getGenreDao()
}