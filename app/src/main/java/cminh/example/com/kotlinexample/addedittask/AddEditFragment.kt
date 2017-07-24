package cminh.example.com.kotlinexample.addedittask

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cminh.example.com.kotlinexample.BasePresenter
import cminh.example.com.kotlinexample.R

/**
 * Created by cminh on 7/17/17.
 *
 */
class AddEditFragment(var mTitle: TextView? = null, var mDescription: TextView? = null,
                      var mSave: View? = null,
                      var mPresenter: AddEditTaskContract.Presenter? = null): Fragment(), AddEditTaskContract.View {
    companion object {
        private val TAG = "AddEditFragment"
        fun getInstance() = AddEditFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_add_edit_task, container, false)

        mTitle = root?.findViewById(R.id.title) as? TextView
        mDescription = root?.findViewById(R.id.description) as? TextView
        mSave = root?.findViewById(R.id.save)

        mSave?.setOnClickListener { mPresenter?.saveTask(mTitle?.text.toString(), mDescription?.text.toString()) }

        return root
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.start()
    }

    override fun onPause() {
        super.onPause()
        mPresenter?.stop()
    }

    override fun showError() {
        Log.e(TAG, "showError - save data Error!!!")
    }

    override fun showTask() {
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }

    override fun setTitle(title: String) {
        mTitle?.text = title
    }

    override fun setDescription(description: String) {
        mDescription?.text = description
    }

    override fun showInvalidData() {
        Log.e(TAG, "showInvalidData - save data Error!!!")
    }

    override fun <P : BasePresenter> setPresenter(presenter: P) {
        mPresenter = presenter as? AddEditTaskPresenter
    }
}
