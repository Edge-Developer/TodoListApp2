package com.edgedevstudio.todolistapp_2.Activities

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.edgedevstudio.todolistapp_2.Async.AppExecutors
import com.edgedevstudio.todolistapp_2.Database.AppDatabase
import com.edgedevstudio.todolistapp_2.Database.TaskEntry
import com.edgedevstudio.todolistapp_2.R
import kotlinx.android.synthetic.main.activity_add_task.*
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var appDatabase: AppDatabase
    private lateinit var saveTaskButton: Button
    private var mTaskId = DEFAULT_TASK_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        appDatabase = AppDatabase.getInstance(this)
        saveTaskButton = saveButton


        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID))
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID)
        saveTaskButton.setOnClickListener { saveTask() }

        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            saveTaskButton.setText("Update")
            if (mTaskId == DEFAULT_TASK_ID) { //implement saved instance state to avoid getting IntentExtra. Leads to a More Optimised App!
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID)

                AppExecutors.getInstance().diskIO.execute {
                    val taskEntry = appDatabase.taskDao().loadTaskById(mTaskId)
                    runOnUiThread{
                        populateUI(taskEntry)
                    }
                }
            }
        }

        Log.d(TAG, "mTaskId = $mTaskId")
    }

    fun populateUI(taskEntry: TaskEntry) {
        editTextTaskDescription.setText(taskEntry.description)
        setPriorityInViews(taskEntry.priority)
    }

    private fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> radioGroup.check(R.id.radButton1)
            PRIORITY_MEDIUM -> radioGroup.check(R.id.radButton2)
            PRIORITY_LOW -> radioGroup.check(R.id.radButton3)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putInt(INSTANCE_TASK_ID, mTaskId)
    }

    fun saveTask() {
        val taskDescription = editTextTaskDescription.text.toString()
        val priority = getPriorityInViews()
        val date = Date()
        val taskEntry = TaskEntry(taskDescription, priority, date)

        AppExecutors.getInstance().diskIO.execute {
            if (mTaskId == DEFAULT_TASK_ID)
                appDatabase.taskDao().insertTask(taskEntry)
            else {
                taskEntry.id = mTaskId //very important
                appDatabase.taskDao().updateTask(taskEntry)
            }
            finish()
        }
    }


    fun getPriorityInViews(): Int {
        val checkedId = radioGroup.checkedRadioButtonId
        return when (checkedId) {
            R.id.radButton1 -> PRIORITY_HIGH
            R.id.radButton2 -> PRIORITY_MEDIUM
            R.id.radButton3 -> PRIORITY_LOW
            else -> -1
        }
    }

    companion object {
        val EXTRA_TASK_ID = "extraTaskId"
        val INSTANCE_TASK_ID = "extraTaskId"

        val DEFAULT_TASK_ID = -1

        val TAG = "AddTaskActivity"

        // Constants for Priority
        val PRIORITY_HIGH = 1
        val PRIORITY_MEDIUM = 2
        val PRIORITY_LOW = 3
    }
}
