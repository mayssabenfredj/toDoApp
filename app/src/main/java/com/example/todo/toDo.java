package com.example.todo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todo.Adapters.ToDoAdapter;
import com.example.todo.Model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class toDo extends Fragment {

    private FloatingActionButton fab;
    private TextView dateSelect ;
    private String selectedDate;
    private List<ToDoModel> taskList;
    private ToDoAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);

        dateSelect = view.findViewById(R.id.dateSelect);
        fab = view.findViewById(R.id.fab);
        taskList = new ArrayList<>();
        adapter = new ToDoAdapter(taskList, ((MainActivity) requireActivity()).getDatabaseHandler(), requireContext());


        RecyclerView recyclerView = view.findViewById(R.id.tasksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ToDoFragment", "Fab clicked");
                // Ouvrir le fragment AddNewTask en bas de l'écran
                AddNewTask addNewTask = AddNewTask.newInstance();
                addNewTask.show(getChildFragmentManager(), AddNewTask.TAG);
            }
        });
        return view;
    }
    public void updateSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;

        dateSelect.setText(selectedDate);
        taskList.clear();
        taskList.addAll(((MainActivity) requireActivity()).getDatabaseHandler().getTasks(selectedDate));
        adapter.notifyDataSetChanged();
    }
    public String getSelectedDate() {
        return selectedDate;
    }
}