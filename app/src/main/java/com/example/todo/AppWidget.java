package com.example.todo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import android.content.Intent;
import android.widget.RemoteViews;

import com.example.todo.Model.ToDoModel;
import com.example.todo.Utils.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        DatabaseHandler db = new DatabaseHandler(context);

        // Get tasks for the current date (you need to implement the logic to get the current date)
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        CharSequence currentDateText = currentDate;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        // Update the TextView with the current date
        views.setTextViewText(R.id.appwidget_date, currentDateText);

        // Get tasks for the current date
        List<ToDoModel> tasksForCurrentDate = db.getTasks(currentDate);

        // Clear existing views in tasksLinearLayout
        views.removeAllViews(R.id.tasksLinearLayout);

        // Iterate through tasks and add them to the LinearLayout
        for (ToDoModel task : tasksForCurrentDate) {
            // Create a new RemoteViews for each task
            RemoteViews taskRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_task_layout);

            // Update the ImageView for status
            taskRemoteViews.setImageViewResource(R.id.task_status_image, task.getStatus() == 0 ? R.drawable.uncheked : R.drawable.checked);

            // Update the TextView for task description
            taskRemoteViews.setTextViewText(R.id.task_description_text, task.getTask());

            // Add the taskRemoteViews to the tasksLinearLayout
            views.addView(R.id.tasksLinearLayout, taskRemoteViews);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // Appelé lorsque le périphérique est démarré, met à jour le widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new android.content.ComponentName(context, AppWidget.class));
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
    }
}