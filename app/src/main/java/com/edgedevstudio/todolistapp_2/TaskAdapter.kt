package com.edgedevstudio.todolistapp_2

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.edgedevstudio.todolistapp_2.Database.TaskEntry
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Olorunleke Opeyemi on 21/01/2019.
 **/
class TaskAdapter(val mContext: Context, val mTaskViewCliskListener: TaskViewCliskListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    var mTaskEntries: MutableList<TaskEntry> = ArrayList()
    val DATE_FORMAT = "dd/MM/yyy"
    private val dateformat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val taskView = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(taskView)
    }

    override fun getItemCount() = mTaskEntries.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskEntry = mTaskEntries.get(position)

        val description = taskEntry.description
        val priority = taskEntry.priority
        val updatedAt = dateformat.format(taskEntry.updatedAt)


        holder.taskDescriptionView.setText(description)
        holder.taskUpdatedAtView.setText(updatedAt)
        holder.priorityView.setText("$priority")

        val priorityCircle = holder.priorityView.background as GradientDrawable
        val priorityColor = getPriorityColor(priority)
        priorityCircle.setColor(priorityColor)

    }


    fun setTaskEntries(taskEntries : MutableList<TaskEntry>){
        mTaskEntries = taskEntries
        notifyDataSetChanged()
    }

    private fun getPriorityColor(priority: Int): Int {
        return when (priority) {
            1 -> ContextCompat.getColor(mContext, R.color.materialRed)
            2 -> ContextCompat.getColor(mContext, R.color.materialOrange)
            3 -> ContextCompat.getColor(mContext, R.color.materialYellow)
            else -> 0
        }
    }


    inner class TaskViewHolder(taskView: View) : RecyclerView.ViewHolder(taskView) {
        val taskDescriptionView: TextView
        val taskUpdatedAtView: TextView
        val priorityView: TextView

        init {
            taskDescriptionView = taskView.findViewById(R.id.taskDescription)
            taskUpdatedAtView = taskView.findViewById(R.id.taskUpdatedAt)
            priorityView = taskView.findViewById(R.id.priorityTextView)
            taskView.setOnClickListener {
                val taskEntry = mTaskEntries.get(adapterPosition)
                val taskId = taskEntry.id
                mTaskViewCliskListener.onTaskViewClickListener(taskId)
                // we will call an interface
            }

        }
    }

    interface TaskViewCliskListener {
        fun onTaskViewClickListener(taskId: Int)
    }
}