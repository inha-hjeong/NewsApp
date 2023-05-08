package com.faraonelife.newsappcompose.di

import com.faraonelife.newsappcompose.repo.RemoteDS
import com.faraonelife.newsappcompose.repo.RemoteDSImpl
import com.faraonelife.newsappcompose.repo.Repo
import com.faraonelife.newsappcompose.repo.RepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindRemoteDS(remoteDsImpl: RemoteDSImpl): RemoteDS

    @Binds
    abstract fun bindRepo(repoImpl: RepoImpl): Repo
}