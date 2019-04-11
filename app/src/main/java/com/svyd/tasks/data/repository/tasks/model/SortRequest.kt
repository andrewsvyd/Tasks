package com.svyd.tasks.data.repository.tasks.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SortRequest(var property: String, var ascending: Boolean): Parcelable
