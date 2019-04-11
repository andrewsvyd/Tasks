package com.svyd.tasks.presentation.features.tasks

import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.svyd.tasks.R
import com.svyd.tasks.data.repository.tasks.model.Task
import com.svyd.tasks.data.repository.tasks.model.TaskPriority.HIGH
import com.svyd.tasks.data.repository.tasks.model.TaskPriority.LOW
import com.svyd.tasks.data.repository.tasks.model.TaskPriority.MEDIUM

import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskHolder>() {
    private var data: MutableList<Task> = ArrayList()
    private var listener: OnTaskClickListener? = null

    val items: List<Task>
        get() = data

    fun setData(tasks: List<Task>) {
        data.clear()
        data.addAll(tasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_task, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnTaskClickListener(listener: OnTaskClickListener) {
        this.listener = listener
    }

    inner class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView
        private val tvDate: TextView
        private val tvPriority: TextView

        init {
            itemView.setOnClickListener { listener?.onTaskClick(data[layoutPosition].id) }
            tvTitle = itemView.findViewById(R.id.tvTitle_LIT)
            tvDate = itemView.findViewById(R.id.tvDate_LIT)
            tvPriority = itemView.findViewById(R.id.tvPriority_LIT)
        }

        fun bind(task: Task) {
            tvTitle.text = task.title
            setDate(task.getDate())
            setPriority(task.priority)
        }

        private fun setDate(dueBy: Long) {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.timeInMillis = dueBy
            tvDate.text = DateFormat.format(TIME_FORMAT, cal).toString()
        }

        private fun setPriority(priority: String) {
            when (priority) {
                HIGH -> tvPriority.setText(R.string.priority_high)
                MEDIUM -> tvPriority.setText(R.string.priority_medium)
                LOW -> tvPriority.setText(R.string.priority_low)
            }
        }
    }

    companion object {
        private const val TIME_FORMAT = "dd/MM/yyyy HH:mm"
    }
}
