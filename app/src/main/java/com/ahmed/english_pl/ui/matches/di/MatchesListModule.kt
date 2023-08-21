package com.ahmed.english_pl.ui.matches.di

import com.ahmed.english_pl.data.local.ILocalDataSource
import com.ahmed.english_pl.data.remote.IRemoteDataSource
import com.ahmed.english_pl.data.repositories.matches_list.IMatchesListRepository
import com.ahmed.english_pl.data.repositories.matches_list.MatchesListRepository
import com.ahmed.english_pl.data.shared_prefrences.IPreferencesDataSource
import com.ahmed.english_pl.domain.use_cases.match_filter.IMatchesFilterUseCase
import com.ahmed.english_pl.domain.use_cases.match_filter.MatchesFilterUseCase
import com.ahmed.english_pl.domain.use_cases.matches.IMatchesListUseCase
import com.ahmed.english_pl.domain.use_cases.matches.MatchesListUseCase
import com.ahmed.english_pl.retrofit.RetrofitModule
import com.ahmed.english_pl.utils.connection_utils.IConnectionUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
@InstallIn(SingletonComponent::class)
abstract class MatchesListModule {
    companion object {
        @Singleton
        @Provides
        fun provideMatchesListingRepository(
            connectionUtils: IConnectionUtils,
            mIRemoteDataSource: IRemoteDataSource,
            mILocalDataSource: ILocalDataSource,
            mIPreferencesDataSource: IPreferencesDataSource
        ): IMatchesListRepository {
            return MatchesListRepository(
                connectionUtils,
                mIRemoteDataSource,
                mILocalDataSource,
                mIPreferencesDataSource
            )
        }

        @Singleton
        @Provides
        fun bindIMatchesFilterListUseCase(matchesUseCase: IMatchesListUseCase): IMatchesFilterUseCase {
            return MatchesFilterUseCase(matchesUseCase)
        }
    }


    @Singleton
    @Binds
    abstract fun bindIMatchesListUseCase(matchesUseCase: MatchesListUseCase): IMatchesListUseCase



}