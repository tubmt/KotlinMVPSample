package cminh.example.com.kotlinexample.data.source.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cminh.example.com.kotlinexample.tasks.domain.model.Task

/**
 * Created by cminh on 7/10/17.
 *
 */
class TasksDbHelper: SQLiteOpenHelper {
    companion object {
        val DATABASE_VERSION = 1

        val DATABASE_NAME = "Tasks.db"

        val TEXT_TYPE = " TEXT"

        val BOOLEAN_TYPE = " INTEGER"

        val COMMA_SEP = ","

        val SQL_CREATE_ENTRIES = "CREATE TABLE " + Task.TABLE_NAME + " (" +
                Task.COLUMN_ID + TEXT_TYPE + " PRIMARY KEY," +
                Task.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                Task.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                Task.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                Task.COLUMN_NAME_COMPLETED + TEXT_TYPE + BOOLEAN_TYPE + " );"
    }

    constructor(context: Context) : super(context, DATABASE_NAME, null, DATABASE_VERSION)


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Not required as at version 1
    }
}