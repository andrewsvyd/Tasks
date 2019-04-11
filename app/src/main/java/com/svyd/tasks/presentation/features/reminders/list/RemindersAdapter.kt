package com.svyd.tasks.presentation.features.reminders.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.svyd.tasks.R
import com.svyd.tasks.data.repository.reminders.model.Reminder
import com.svyd.tasks.util.ReminderUtil

import androidx.recyclerview.widget.RecyclerView

class RemindersAdapter(val context: Context) : RecyclerView.Adapter<RemindersAdapter.ReminderHolder>() {
    private var data: List<Reminder> = ArrayList()
    private var listener: ItemDeletedListener? = null

    fun setData(reminders: List<Reminder>) {
        data = reminders
        notifyDataSetChanged()
    }

    fun setDeletionListener(listener: ItemDeletedListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_reminder, parent, false)
        return ReminderHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun deleteItem(position: Int) {
        notifyItemRemoved(position)
        listener?.onItemDeleted(position)
    }

    fun undoDeletion(deletedPosition: Int) {
        notifyItemInserted(deletedPosition)
    }

    inner class ReminderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle_LIR)
        private val tvRemindBefore: TextView = itemView.findViewById(R.id.tvRemindBefore_LIR)

        fun bind(reminder: Reminder) {
            tvTitle.text = reminder.taskName
            tvRemindBefore.setText(ReminderUtil.getReminderTextForTime(reminder.reminderTime.toInt()))
        }
    }
}
