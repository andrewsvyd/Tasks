package com.svyd.tasks.data.repository.tasks.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Tasks(var tasks: MutableList<Task>) : Parcelable
