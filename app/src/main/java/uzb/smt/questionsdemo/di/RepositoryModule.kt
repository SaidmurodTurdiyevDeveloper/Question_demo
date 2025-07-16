package uzb.smt.questionsdemo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uzb.smt.questionsdemo.data.repository.QuestionRepositoryImpl
import uzb.smt.questionsdemo.domen.repository.QuestionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface  RepositoryModule {

    @Binds
    @Singleton
     fun bindQuestionRepository(
        impl: QuestionRepositoryImpl
    ): QuestionRepository
}