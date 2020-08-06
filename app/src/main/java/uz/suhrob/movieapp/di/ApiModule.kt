package uz.suhrob.movieapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.suhrob.movieapp.data.api.MovieService
import uz.suhrob.movieapp.data.api.RemoteDataSource
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder()
            .create()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(movieService: MovieService): RemoteDataSource =
        RemoteDataSource(movieService)
}