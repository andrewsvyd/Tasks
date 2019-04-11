package com.svyd.tasks.data.repository.authorization

import com.svyd.tasks.data.repository.authorization.model.AuthorizationToken
import com.svyd.tasks.data.repository.authorization.model.UserCredentials

import rx.Observable


interface AuthorizationRepository {
    fun signIn(credentials: UserCredentials): Observable<AuthorizationToken>
    fun signUp(credentials: UserCredentials): Observable<AuthorizationToken>
}
