package com.example.todo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.todo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String DATE = "date";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK + " TEXT, " + STATUS + " INTEGER, " + DATE + " TEXT)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
        openDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {
        Log.d("DatabaseHandler", "Inserting task - Task: " + task.getTask() + ", Date: " + task.getDate());
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task.getTask());
        contentValues.put(DATE, task.getDate());

        db.insert(TODO_TABLE, null, contentValues);
        Log.d("DatabaseHandler", "Task inserted successfully.");

    }

    public List<ToDoModel> getTasks(String selectedDate) {
        List<ToDoModel> taskList = new ArrayList<>();
        String[] columns = {ID, TASK, STATUS, DATE};
        String selection = DATE + "=?";
        String[] selectionArgs = {selectedDate};
        if (db == null || !db.isOpen()) {
            openDatabase();  // Ensure that the database is open before querying.
        }
        Cursor cursor = db.query(TODO_TABLE, columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(ID);
                int taskIndex = cursor.getColumnIndex(TASK);
                int statusIndex = cursor.getColumnIndex(STATUS);
                int dateIndex = cursor.getColumnIndex(DATE);

                // Check if the columns exist in the cursor
                if (idIndex != -1 && taskIndex != -1 && statusIndex != -1 && dateIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String task = cursor.getString(taskIndex);
                    int status = cursor.getInt(statusIndex);
                    String date = cursor.getString(dateIndex);

                    ToDoModel toDoModel = new ToDoModel(task, date, status);
                    toDoModel.setId(id);
                    taskList.add(toDoModel);
                }
            }

            cursor.close();
        }

        return taskList;
    }


    public List<String> getDatesWithTasks() {
        openDatabase();

        List<String> datesWithTasks = new ArrayList<>();

        String[] columns = {DATE};
        Cursor cursor = db.query(true, TODO_TABLE, columns, null, null, null, null, null, null);

        if (cursor != null) {
            int dateIndex = cursor.getColumnIndex(DATE);

            if (dateIndex != -1) {
                while (cursor.moveToNext()) {
                    datesWithTasks.add(cursor.getString(dateIndex));
                }
            } else {
                Log.e("DatabaseHandler", "Column 'date' not found in cursor");
            }

            cursor.close();
        }

        return datesWithTasks;
    }

    public boolean hasTasks(String selectedDate) {
        openDatabase();
        String[] columns = {ID};
        String selection = DATE + "=?";
        String[] selectionArgs = {selectedDate};

        Cursor cursor = db.query(TODO_TABLE, columns, selection, selectionArgs, null, null, null);

        boolean hasTasks = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return hasTasks;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
