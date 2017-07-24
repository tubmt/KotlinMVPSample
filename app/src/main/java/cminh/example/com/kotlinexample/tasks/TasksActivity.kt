package cminh.example.com.kotlinexample.tasks

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import cminh.example.com.kotlinexample.Injection
import cminh.example.com.kotlinexample.R
import cminh.example.com.kotlinexample.data.source.TasksRepository
import cminh.example.com.kotlinexample.data.source.local.TasksLocalDataSource
import cminh.example.com.kotlinexample.data.source.remote.TasksRemoteDataSource
import cminh.example.com.kotlinexample.tasks.domain.filter.FilterFactory
import cminh.example.com.kotlinexample.tasks.domain.model.TasksFilterType
import cminh.example.com.kotlinexample.tasks.domain.usecase.ActivateTask
import cminh.example.com.kotlinexample.tasks.domain.usecase.ClearCompleteTasks
import cminh.example.com.kotlinexample.tasks.domain.usecase.CompleteTask
import cminh.example.com.kotlinexample.tasks.domain.usecase.GetTasks
import cminh.example.com.kotlinexample.util.ActivityUtils

class TasksActivity(var mDrawerLayout: DrawerLayout? = null, var mTasksPresenter: TasksPresenter? = null) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        ab.setDisplayHomeAsUpEnabled(true)

        // Set up the navigation drawer.
        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        mDrawerLayout?.setStatusBarBackground(R.color.colorPrimaryDark)
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }

        var tasksFragment: TasksFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as? TasksFragment
        if (tasksFragment == null) {
            tasksFragment = TasksFragment.getInstance()
            ActivityUtils.addFragmentToActivity(supportFragmentManager, tasksFragment, R.id.contentFrame)
        }

        Log.d(TAG, "tasksFragment=$tasksFragment")
        val repository = TasksRepository.getInstance(TasksLocalDataSource.getInstance(this), TasksRemoteDataSource.getInstance(this))
        val getTasks = GetTasks(repository, FilterFactory())
        Log.d(TAG, "GetTasks=$getTasks")
        val completeTask = CompleteTask(repository)
        Log.d(TAG, "CompleteTask=$completeTask")
        val activeTask = ActivateTask(repository)
        Log.d(TAG, "ActivateTask=$activeTask")
        val clearCompleteTask = ClearCompleteTasks(repository)
        Log.d(TAG, "ClearCompleteTasks=$clearCompleteTask")
        val usecase = Injection.provideUseCaseHandler()
        Log.d(TAG, "UseCaseHandler=$usecase")

        mTasksPresenter = TasksPresenter(tasksFragment,
                getTasks,
                completeTask,
                activeTask,
                clearCompleteTask,
                usecase)

        if (savedInstanceState != null) {
            mTasksPresenter?.setFiltering(savedInstanceState.getSerializable(CURRENT_FILTERING_KEY) as TasksFilterType)
        }

        val a: String? = null
        Log.d(TAG, "string value ${a?.length}")
    }

    companion object {
        val TAG = "TasksActivity"
        val CURRENT_FILTERING_KEY: String = "CURRENT_FILTERING_KEY"
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter?.mCurrentFiltering)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> mDrawerLayout?.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout?.closeDrawers()
            true
        }
    }

}
