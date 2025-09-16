package com.example.plaintext.data.di


import android.content.Context
import androidx.room.Room
import com.example.plaintext.data.PlainTextDatabase
import com.example.plaintext.data.SimulatorDatabase
import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.repository.LocalPasswordDBStore
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): PlainTextDatabase {
        return Room.databaseBuilder(
            appContext,
            PlainTextDatabase::class.java,
            "plaint_text_database"
        ).build()
    }

    @Provides
    fun providePasswordDao(plainTextDatabase: PlainTextDatabase): PasswordDao {
        return plainTextDatabase.passwordDao()
    }

    @Provides
    @Singleton
    fun providePasswordDBStore(
        passwordDao: PasswordDao
    ): PasswordDBStore = LocalPasswordDBStore(passwordDao)

    @Provides
    @Singleton
    fun provideDBSimulator(): SimulatorDatabase = SimulatorDatabase()
}