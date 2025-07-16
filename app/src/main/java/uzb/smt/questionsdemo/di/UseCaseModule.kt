package uzb.smt.questionsdemo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uzb.smt.questionsdemo.domen.repository.QuestionRepository
import uzb.smt.questionsdemo.domen.usecase.CheckCanCreateQuestion
import uzb.smt.questionsdemo.domen.usecase.CheckQuizFinish
import uzb.smt.questionsdemo.domen.usecase.CheckQuizResult
import uzb.smt.questionsdemo.domen.usecase.CreateQuestion
import uzb.smt.questionsdemo.domen.usecase.DeleteQuestion
import uzb.smt.questionsdemo.domen.usecase.GetQuestions
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetQuestionsUseCase(
        repository: QuestionRepository
    ): GetQuestions {
        return GetQuestions(repository)
    }

    @Provides
    @Singleton
    fun provideCreateQuestionUseCase(
        repository: QuestionRepository
    ): CreateQuestion {
        return CreateQuestion(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteQuestionUseCase(
        repository: QuestionRepository
    ): DeleteQuestion {
        return DeleteQuestion(repository)
    }

    @Provides
    @Singleton
    fun provideCheckCanCreateQuestionUseCase(): CheckCanCreateQuestion {
        return CheckCanCreateQuestion()
    }

    @Provides
    @Singleton
    fun provideCheckQuizResultUseCase(): CheckQuizResult {
        return CheckQuizResult()
    }

    @Provides
    @Singleton
    fun provideCheckQuizFinishUseCase(): CheckQuizFinish {
        return CheckQuizFinish()
    }
}