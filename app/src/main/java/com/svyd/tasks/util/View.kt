package com.svyd.tasks.util

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

inline fun <reified T : View> Activity.find(id: Int): T = this.findViewById(id) as T