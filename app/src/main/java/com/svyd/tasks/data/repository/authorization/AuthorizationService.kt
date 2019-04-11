package com.svyd.tasks.data.repository.authorization

import com.svyd.tasks.data.repository.authorization.model.AuthorizationToken
import com.svyd.tasks.data.repository.authorization.model.UserCredentials

import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

interface AuthorizationService {

    @POST("auth")
    fun authorize(@Body credentials: UserCredentials): Observable<AuthorizationToken>

    @POST("users")
    fun sighnUp(@Body credentials: UserCredentials): Observable<AuthorizationToken>
}
