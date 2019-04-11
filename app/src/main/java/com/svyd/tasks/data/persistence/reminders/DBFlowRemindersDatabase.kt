package com.svyd.tasks.data.persistence.reminders

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = DBFlowRemindersDatabase.NAME, version = DBFlowRemindersDatabase.VERSION)
object DBFlowRemindersDatabase {
    const val NAME = "reminders-database"
    const val VERSION = 1
}
