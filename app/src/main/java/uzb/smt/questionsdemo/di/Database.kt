package uzb.smt.questionsdemo.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uzb.smt.questionsdemo.data.source.storage.room.QuestionDatabase
import uzb.smt.questionsdemo.data.source.storage.room.dao.QuestionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Database {
    @Singleton
    @Provides
    fun provideDatabase(
        applicationContext: Application
    ): QuestionDatabase {
        return Room.databaseBuilder(
            applicationContext,
            QuestionDatabase::class.java, "question-app-store"
        ).build()
    }

    @Singleton
    @Provides
    fun provideQuestionDao(
        database: QuestionDatabase
    ): QuestionDao {
        return database.questionDao()
    }
}