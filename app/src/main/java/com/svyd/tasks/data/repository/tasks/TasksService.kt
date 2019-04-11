package com.svyd.tasks.data.repository.tasks

import com.svyd.tasks.data.repository.tasks.model.TaskRequest
import com.svyd.tasks.data.repository.tasks.model.TaskResponse
import com.svyd.tasks.data.repository.tasks.model.TasksResponse
import retrofit2.http.*

import rx.Observable

interface TasksService {

    //sort by date by default
    @GET("tasks")
    fun fetchServers(@Query("sort") sort: String): Observable<TasksResponse>

    @POST("tasks")
    fun createTask(@Body task: TaskRequest): Observable<TaskResponse>

    @DELETE("tasks/{id}")
    fun deleteTask(@Path("id") id: String): Observable<Void>

    @PUT("tasks/{id}")
    fun updateTask(@Path("id") id: String,
                   @Body task: TaskRequest): Observable<Void>
}
