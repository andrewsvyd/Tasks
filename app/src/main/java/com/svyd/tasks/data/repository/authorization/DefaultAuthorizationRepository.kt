package com.svyd.tasks.data.repository.authorization

import com.svyd.tasks.data.repository.authorization.model.AuthorizationToken
import com.svyd.tasks.data.repository.authorization.model.UserCredentials

import rx.Observable

class DefaultAuthorizationRepository(private val service: AuthorizationService) : AuthorizationRepository {

    override fun signIn(credentials: UserCredentials): Observable<AuthorizationToken> =
        service.authorize(credentials)

    override fun signUp(credentials: UserCredentials): Observable<AuthorizationToken> =
        service.sighnUp(credentials)
}
