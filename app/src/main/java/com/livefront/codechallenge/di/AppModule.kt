package com.livefront.codechallenge.di

import com.livefront.codechallenge.data.CharacterAPI
import com.livefront.codechallenge.data.CharacterRepository
import com.livefront.codechallenge.data.CharacterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASEURL = "https://akabab.github.io/superhero-api/api/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCharacterApi(): CharacterAPI {
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CharacterAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(api: CharacterAPI): CharacterRepository {
        return CharacterRepositoryImpl(api)
    }
}
