package cminh.example.com.kotlinexample.tasks

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.*
import cminh.example.com.kotlinexample.BasePresenter
import cminh.example.com.kotlinexample.R
import cminh.example.com.kotlinexample.addedittask.AddEditActivity
import cminh.example.com.kotlinexample.tasks.domain.model.Task
import cminh.example.com.kotlinexample.tasks.domain.model.TasksFilterType

class TasksFragment(var mPresenter: TasksContract.Presenter? = null, var mListAdapter: TasksAdapter? = null,
                    var mNoTasksView: View? = null, var mNoTaskIcon: ImageView? = null,
                    var mNoTaskMainView: TextView? = null, var mNoTaskAddView: TextView? = null,
                    var mTasksView: LinearLayout? = null, var mFilteringLabelView: TextView? = null) : Fragment(), TasksContract.View {

    val mItemListener = object : TaskItemListener {
        override fun onTaskClick(position: Int) {
            mPresenter?.openTaskDetails(mListAdapter!!.mTasks[position])
        }

        override fun onCompleteTaskClick(position: Int) {
            mPresenter?.completeTask(mListAdapter!!.mTasks[position])
        }

        override fun onActivateTaskClick(position: Int) {
            mPresenter?.activateTask(mListAdapter!!.mTasks[position])
        }
    }

    companion object {
        val TAG = "TasksFragment"
        fun getInstance() = TasksFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListAdapter = TasksAdapter(ArrayList<Task>(), mItemListener)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater?.inflate(R.layout.tasks_frag, container, false)

        // Set up tasks view
        val listView = root?.findViewById(R.id.tasks_list) as RecyclerView
        listView.adapter = mListAdapter
        listView.layoutManager = LinearLayoutManager(context)
        mFilteringLabelView = root.findViewById(R.id.filteringLabel) as TextView
        mTasksView = root.findViewById(R.id.tasksLL) as LinearLayout

        // Set up  no tasks view
        mNoTasksView = root.findViewById(R.id.noTasks)
        mNoTaskIcon = root.findViewById(R.id.noTasksIcon) as ImageView
        mNoTaskMainView = root.findViewById(R.id.noTasksMain) as TextView
        mNoTaskAddView = root.findViewById(R.id.noTasksAdd) as TextView
        mNoTaskAddView?.setOnClickListener { showAddTask() }

        // Set up floating action button
        val fab = activity.findViewById(R.id.fab_add_task) as FloatingActionButton

        fab.setImageResource(R.drawable.ic_add)
        fab.setOnClickListener { mPresenter?.addNewTask() }

        // Set up progress indicator
        val swipeRefreshLayout = root.findViewById(R.id.refresh_layout) as ScrollChildSwipeRefreshLayout
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorPrimaryDark)
        )
        // Set the scrolling view in the custom SwipeRefreshLayout.
        swipeRefreshLayout.setScrollUpChild(listView)

        //swipeRefreshLayout.setOnRefreshListener { mPresenter?.loadTasks(false) }

        setHasOptionsMenu(true)

        return root
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_clear -> mPresenter?.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
            //R.id.menu_refresh -> mPresenter?.loadTasks(true)
        }
        return true
    }

    override fun <P : BasePresenter> setPresenter(presenter: P) {
        mPresenter = presenter as TasksContract.Presenter
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (view == null) {
            return
        }

        val srl = view?.findViewById(R.id.refresh_layout) as SwipeRefreshLayout
        srl.post { srl.isRefreshing = active }
    }

    override fun showTasks(tasks: List<Task>) {
        Log.e(TAG, "showTasks - list")
        mListAdapter?.reloadAdapter(tasks)

        mTasksView?.visibility = View.VISIBLE
        mNoTasksView?.visibility = View.GONE
    }

    override fun showAddTask() {
        AddEditActivity.startActivityForResult(activity, "")
    }

    override fun showTaskDetailsUi(taskId: String) {
        AddEditActivity.startActivityForResult(activity, taskId)
    }

    override fun showTaskMarkedComplete() = showMessage(getString(R.string.task_marked_complete))

    override fun showTaskMarkedActive() = showMessage(getString(R.string.task_marked_active))

    override fun showCompletedTasksCleared() = showMessage(getString(R.string.completed_tasks_cleared))

    private fun showMessage(message: String) {
        setLoadingIndicator(false)
        Snackbar.make(view!!, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showLoadingTasksError() = showMessage(getString(R.string.loading_tasks_error))

    override fun showNoTasks() = showNoTasksViews(
            resources.getString(R.string.no_tasks_all),
            R.drawable.ic_assignment_turned_in_24dp,
            false
    )

    override fun showActiveFilterLabel() {
        mFilteringLabelView?.setText(resources.getString(R.string.label_active))
    }

    override fun showCompletedFilterLabel() {
        mFilteringLabelView?.setText(resources.getString(R.string.label_completed))
    }

    override fun showAllFilterLabel() {
        mFilteringLabelView?.setText(resources.getString(R.string.label_all))
    }

    override fun showNoActiveTasks() = showNoTasksViews(
            resources.getString(R.string.no_tasks_active),
            R.drawable.ic_check_circle_24dp,
            false
    )

    override fun showNoCompletedTasks() = showNoTasksViews(
            resources.getString(R.string.no_tasks_completed),
            R.drawable.ic_verified_user_24dp,
            false
    )

    override fun showSuccessfullySavedMessage() = showMessage(getString(R.string.successfully_saved_task_message))

    fun showNoTasksViews(mainText: String, iconRes: Int, showAddView: Boolean) {
        mTasksView?.setVisibility(View.GONE)
        mNoTasksView?.setVisibility(View.VISIBLE)

        mNoTaskMainView?.setText(mainText)
        mNoTaskIcon?.setImageDrawable(resources.getDrawable(iconRes))
        mNoTaskAddView?.setVisibility(if (showAddView) View.VISIBLE else View.GONE)
    }

    override fun isActive() = isAdded

    override fun showFilteringPopUpMenu() {
        val popup = PopupMenu(activity, activity.findViewById(R.id.menu_filter))
        popup.menuInflater.inflate(R.menu.filter_tasks, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item?.itemId) {
                R.id.active -> mPresenter?.setFiltering(TasksFilterType.ACTIVE_TASKS)
                R.id.completed -> mPresenter?.setFiltering(TasksFilterType.COMPLETED_TASKS)
                else -> mPresenter?.setFiltering(TasksFilterType.ALL_TASKS)
            }
            mPresenter?.loadTasks(false)
            true
        }
        popup.show()
    }


    class TasksAdapter(var mTasks: List<Task> = ArrayList<Task>(), var mItemListener: TaskItemListener) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

        fun TasksAdapter(tasks: List<Task>, itemListener: TaskItemListener) {
            mTasks = tasks
            mItemListener = itemListener
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            Log.d(TAG, "onBindViewHolder - position=$position")
            if (holder == null) return
            val task = mTasks[position]

            holder.title.text = task.mTitle
            holder.completed.isChecked = task.mCompleted
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            Log.d(TAG, "onCreateViewHolder - position=$viewType")
            return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.task_item, parent, false), mItemListener)
        }

        override fun getItemCount() = mTasks.size

        fun reloadAdapter(tasks: List<Task>) {
            mTasks = ArrayList<Task>()
            (mTasks as ArrayList<Task>).addAll(tasks)
            notifyDataSetChanged()
        }

        class ViewHolder: RecyclerView.ViewHolder {

            private var mItemListener: TaskItemListener
            var completed: CheckBox
            var title: TextView
            private var root: View

            constructor(itemView: View?, itemListener: TaskItemListener) : super(itemView) {
                root = itemView!!
                completed = itemView.findViewById(R.id.complete) as CheckBox
                title = itemView.findViewById(R.id.title) as TextView

                mItemListener = itemListener

                completed.setOnCheckedChangeListener( object : CompoundButton.OnCheckedChangeListener {
                    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            if (isChecked) {
                                completed.isChecked = true
                                mItemListener.onCompleteTaskClick(adapterPosition)
                            } else {
                                mItemListener.onActivateTaskClick(adapterPosition)
                                completed.isChecked = false
                            }
                        }
                    }
                })

                root.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        mItemListener.onTaskClick(adapterPosition)
                    }
                }
            }
        }
    }

    interface TaskItemListener {

        fun onTaskClick(position: Int)

        fun onCompleteTaskClick(position: Int)

        fun onActivateTaskClick(position: Int)
    }
}
