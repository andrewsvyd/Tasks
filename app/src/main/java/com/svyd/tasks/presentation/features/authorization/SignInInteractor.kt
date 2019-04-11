package com.svyd.tasks.presentation.features.authorization

import com.svyd.tasks.data.repository.authorization.AuthorizationRepository
import com.svyd.tasks.data.repository.authorization.model.AuthorizationToken
import com.svyd.tasks.data.repository.authorization.model.UserCredentials
import com.svyd.tasks.presentation.base.PostInteractor

import rx.Observable

class SignInInteractor(private val repository: AuthorizationRepository) : PostInteractor<UserCredentials, AuthorizationToken>() {
    override fun buildPostObservable(data: UserCredentials): Observable<AuthorizationToken> {
        return repository.signIn(data)
    }
}
