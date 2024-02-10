package com.example.todo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import com.example.todo.Utils.DatabaseHandler;

import java.util.List;

public class calandar extends Fragment {


    private CalendarView calendarView;
    private DatabaseHandler databaseHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calandar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        databaseHandler = new DatabaseHandler(requireContext());
        List<String> datesWithTasks = databaseHandler.getDatesWithTasks();
        updateDateCellColors(datesWithTasks);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Format the selected date
                String selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year);

                // Pass the selected date to the toDo fragment
                ((MainActivity) requireActivity()).setSelectedDate(selectedDate);

            }
        });


        return view;
    }

    private void updateDateCellColors(List<String> datesWithTasks) {
        // Get the CalendarView day picker child (assuming it's a DayPickerView)
        ViewGroup dayPicker = (ViewGroup) calendarView.getChildAt(0);

        // Iterate over all dates in the calendar
        for (int i = 0; i < dayPicker.getChildCount(); i++) {
            View dayView = dayPicker.getChildAt(i);

            if (dayView instanceof TextView) {
                TextView dayTextView = (TextView) dayView;
                // Format the day text to match your date format
                String dayText = dayTextView.getText().toString();
                Log.d("dayText", dayText);

                if (datesWithTasks.contains(dayText)) {
                    // Update the color based on task existence
                    dayTextView.setTextColor(Color.RED);
                } else {
                    dayTextView.setTextColor(Color.BLACK);
                }
            }
        }
    }
}