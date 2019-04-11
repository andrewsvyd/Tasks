package com.svyd.tasks.data.persistence.tasks

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = DBFlowTasksDatabase.NAME, version = DBFlowTasksDatabase.VERSION)
object DBFlowTasksDatabase {
    const val NAME = "tasks-database"
    const val VERSION = 1
}