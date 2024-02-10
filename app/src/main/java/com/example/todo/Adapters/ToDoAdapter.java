package com.example.todo.Adapters;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewTask;
import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.Utils.DatabaseHandler;

import java.util.List;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> taskList;
    private DatabaseHandler db;
    private Context context;


    public ToDoAdapter(List<ToDoModel> taskList, DatabaseHandler db, Context context) {
        this.taskList = taskList;
        this.db = db;
        this.context = context;  // Initialize the Context
    }
    public Context getContext() {
        return context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull
                                     final ViewHolder holder, int position) {

        ToDoModel item = taskList.get(position);
        // Mise à jour de l'interface utilisateur en fonction de la tâche
        holder.task.setText(item.getTask());
        holder.task.setChecked(item.getStatus() == 1);
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }


    @Override
    public int getItemCount() {

        return taskList.size();
    }

    public void deleteItem(int position) {
        ToDoModel item = taskList.get(position);
        db.deleteTask(item.getId());
        taskList.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position) {
        Log.d("ToDoAdapter", "editItem: " + position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);

        }
    }
}
