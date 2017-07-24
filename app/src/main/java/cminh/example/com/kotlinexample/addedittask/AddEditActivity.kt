package cminh.example.com.kotlinexample.addedittask

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import cminh.example.com.kotlinexample.Injection
import cminh.example.com.kotlinexample.R
import cminh.example.com.kotlinexample.util.ActivityUtils

class AddEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        var addEditFragment = supportFragmentManager.findFragmentById(R.id.content) as? AddEditFragment

        var taskId = intent.getStringExtra(ARGUMENT_EDIT_TASK_ID)

        if (addEditFragment == null) {
            addEditFragment = AddEditFragment.getInstance()

            if (TextUtils.isEmpty(taskId)) {
                actionBar.setTitle(R.string.add_task)
                taskId = ""
            } else {
                actionBar.setTitle(R.string.edit_task)
            }
            val bundle = Bundle()
            bundle.putString(ARGUMENT_EDIT_TASK_ID, taskId)
            addEditFragment.setArguments(bundle)

            ActivityUtils.addFragmentToActivity(supportFragmentManager, addEditFragment, R.id.content)
        }

        AddEditTaskPresenter(taskId, Injection.provideGetTask(this), Injection.provideSaveTask(this), Injection.provideUseCaseHandler(), addEditFragment)
    }

    companion object {
        val REQUEST_ADD_TASK = 1
        private val ARGUMENT_EDIT_TASK_ID = "ARGUMENT_EDIT_TASK_ID"

        fun startActivityForResult(activity: Activity, taskId: String) {
            val intent = Intent(activity, AddEditActivity::class.java)
            intent.putExtra(ARGUMENT_EDIT_TASK_ID, taskId)
            activity.startActivityForResult(intent, REQUEST_ADD_TASK)
        }
    }
}
