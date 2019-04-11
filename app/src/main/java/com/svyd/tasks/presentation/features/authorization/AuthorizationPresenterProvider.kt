package com.svyd.tasks.presentation.features.authorization

import com.svyd.tasks.data.networking.framework.ServiceProvider
import com.svyd.tasks.data.networking.token.TokenManager
import com.svyd.tasks.data.repository.authorization.AuthorizationRepository
import com.svyd.tasks.data.repository.authorization.AuthorizationService
import com.svyd.tasks.data.repository.authorization.DefaultAuthorizationRepository
import com.svyd.tasks.data.repository.authorization.model.AuthorizationToken
import com.svyd.tasks.data.repository.authorization.model.UserCredentials
import com.svyd.tasks.presentation.TasksApplication
import com.svyd.tasks.presentation.base.PostInteractor
import com.svyd.tasks.presentation.base.PresenterProvider

class AuthorizationPresenterProvider : PresenterProvider() {

    fun providePresenter(): AuthorizationPresenter {
        return AuthorizationPresenter(provideExceptionDelegate(), provideSignInInteractor(), provideSignUpInteractor(), provideTokenManager())
    }

    private fun provideSignUpInteractor(): PostInteractor<UserCredentials, AuthorizationToken> {
        return SignUpInteractor(provideRepository())
    }

    private fun provideTokenManager(): TokenManager {
        return TasksApplication.instance.tokenManager
    }

    private fun provideSignInInteractor(): PostInteractor<UserCredentials, AuthorizationToken> {
        return SignInInteractor(provideRepository())
    }

    private fun provideRepository(): AuthorizationRepository {
        return DefaultAuthorizationRepository(provideService())
    }

    private fun provideService(): AuthorizationService {
        return ServiceProvider().provideService(AuthorizationService::class.java)
    }
}
