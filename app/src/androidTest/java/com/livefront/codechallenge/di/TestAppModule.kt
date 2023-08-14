package com.livefront.codechallenge.di

import com.livefront.codechallenge.data.repo.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object FakeAppModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(): CharacterRepository {
        return FakeCharacterRepo()
    }
}
