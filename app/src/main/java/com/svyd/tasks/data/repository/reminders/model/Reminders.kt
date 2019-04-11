package com.svyd.tasks.data.repository.reminders.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Reminders(var reminders: MutableList<Reminder>): Parcelable