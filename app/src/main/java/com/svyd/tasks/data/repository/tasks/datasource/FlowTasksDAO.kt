package com.svyd.tasks.data.repository.tasks.datasource

import com.raizlabs.android.dbflow.kotlinextensions.where
import com.raizlabs.android.dbflow.sql.language.Delete
import com.raizlabs.android.dbflow.sql.language.SQLOperator
import com.raizlabs.android.dbflow.sql.language.Select
import com.svyd.tasks.data.persistence.tasks.FlowTaskModel
import com.svyd.tasks.data.persistence.tasks.FlowTaskModel_Table
import com.svyd.tasks.data.persistence.tasks.TasksDAO
import com.svyd.tasks.data.repository.tasks.model.TaskOperation
import com.svyd.tasks.data.repository.tasks.model.Task

import java.util.ArrayList

import rx.Observable

class FlowTasksDAO : TasksDAO {

    override fun clear() {
        Delete.table(FlowTaskModel::class.java)
    }

    override val createdTasks: Observable<List<Task>>
        get() = tasksByCondition { FlowTaskModel_Table.operation.`is`(TaskOperation.CREATE) }
                .map { flowTasks -> map(flowTasks) }

    override val updatedTasks: Observable<List<Task>>
        get() = tasksByCondition { FlowTaskModel_Table.operation.`is`(TaskOperation.UPDATE) }
                .map { flowTasks -> map(flowTasks) }

    override val deletedTasks: Observable<List<Task>>
        get() = tasksByCondition { FlowTaskModel_Table.operation.`is`(TaskOperation.DELETE) }
                .map { flowTasks -> map(flowTasks) }

    override val tasks: Observable<List<Task>>
        get() = tasksByCondition { FlowTaskModel_Table.operation.isNot(TaskOperation.DELETE) }
                .map { flowTasks -> map(flowTasks) }

    override fun createTask(task: Task): Observable<Task> {
        return createObservable(task, ::save)
    }

    private fun save(task: Task) {
        val flowTask = FlowTaskModel(task)
        task.id = flowTask.id
        flowTask.save()
    }

    override fun deleteTask(task: Task): Observable<Task> {
        return createObservable(task) { FlowTaskModel(task).delete() }
    }

    override fun updateTask(task: Task): Observable<Task> {
        return createObservable(task) { FlowTaskModel(task).update() }
    }

    override fun removeDeletedTasks(): Observable<Boolean> {
        return tasksByCondition { FlowTaskModel_Table.operation.`is`(TaskOperation.DELETE) }
                .map { tasks -> delete(tasks) }
    }

    override fun removeUpdatedTasks(): Observable<Boolean> {
        return tasksByCondition { FlowTaskModel_Table.operation.`is`(TaskOperation.UPDATE) }
                .map { tasks -> delete(tasks) }
    }

    override fun removeCreatedTasks(): Observable<Boolean> {
        return tasksByCondition { FlowTaskModel_Table.operation.`is`(TaskOperation.CREATE) }
                .map { tasks -> delete(tasks) }
    }

    private fun delete(tasks: List<FlowTaskModel>): Boolean {
        tasks.forEach { task -> task.delete() }
        return tasks.isNotEmpty()
    }

    private fun createObservable(task: Task, execute: (task: Task) -> Unit): Observable<Task> {
        return Observable.create { subscriber ->
            try {
                execute(task)
                subscriber.onNext(task)
                subscriber.onCompleted()
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    private fun tasksByCondition(condition: () -> SQLOperator): Observable<List<FlowTaskModel>> {
        return Observable.create { subscriber ->
            try {
                val flowModels = Select()
                        .from(FlowTaskModel::class.java)
                        .where(condition)
                        .orderBy(FlowTaskModel_Table.date, true)
                        .queryList()

                subscriber.onNext(flowModels)
                subscriber.onCompleted()
            } catch (exception: Exception) {
                subscriber.onError(exception)
            }
        }
    }

    override fun storeTasks(tasks: List<Task>?): Observable<List<Task>> {
        return Observable.create { subscriber ->
            try {
                tasks?.let {
                    for (task in tasks) {
                        FlowTaskModel(task).save()
                    }
                    subscriber.onNext(tasks)
                }
                subscriber.onCompleted()
            } catch (exception: java.lang.Exception) {
                subscriber.onError(exception)
            }
        }
    }

    private fun map(models: List<FlowTaskModel>): List<Task> {
        val tasks = ArrayList<Task>()
        for (model in models) {
            tasks.add(Task(
                    model.id,
                    model.title,
                    model.date,
                    0,
                    model.priority,
                    model.operation))
        }
        return tasks
    }

}
