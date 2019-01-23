package com.edgedevstudio.todolistapp_2.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edgedevstudio.todolistapp_2.Async.AppExecutors
import com.edgedevstudio.todolistapp_2.Database.AppDatabase
import com.edgedevstudio.todolistapp_2.R
import com.edgedevstudio.todolistapp_2.TaskAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TaskAdapter.TaskViewCliskListener {
    private lateinit var mTaskAdapter: TaskAdapter
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        mTaskAdapter = TaskAdapter(this, this)
        recyclerViewTasks.adapter = mTaskAdapter

        // just to add line dividers to each task
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerViewTasks.addItemDecoration(decoration)

        //recognises when a user swipes (a taskEntry) to delete
        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                // Called when a user swipes left or right on a ViewHolder
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                    val position = viewHolder.adapterPosition
                    val tasks = mTaskAdapter.mTaskEntries
                    val task = tasks[position]
                    AppExecutors.getInstance().diskIO.execute {
                        appDatabase.taskDao().deleteTask(task)
                        retrieveTasksFromDB() //TODO: remove this because Live Data will automagically refresh our list whenever there is a change
                    }

                    // Here is where you'll implement swipe to delete
                }
            }
        ).attachToRecyclerView(recyclerViewTasks)



        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        appDatabase = AppDatabase.getInstance(this)
    }

    private fun retrieveTasksFromDB(){
        AppExecutors.getInstance().diskIO.execute {
            val taskEntries= appDatabase.taskDao().loadAllTask()

            runOnUiThread{
                mTaskAdapter.setTaskEntries(taskEntries)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        /* TODO:
        Having to retrieve our db each time on resume is called is inefficient
        onResume is called on Device rotation and Activity Restart (back navigation etc)

        in part 3 we will be looking at using Live Data (an Observer) to stop all these ineffiencies
        */
        retrieveTasksFromDB()
    }

    override fun onTaskViewClickListener(taskId: Int) {
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, taskId)
        startActivity(intent)
    }
}
