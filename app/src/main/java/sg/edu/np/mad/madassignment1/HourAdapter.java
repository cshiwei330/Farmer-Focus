package sg.edu.np.mad.madassignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class HourAdapter extends ArrayAdapter<HourTasks> implements Filterable {

    ArrayList<Task> dataDateFilter;
    ArrayList<Task> data;
    ArrayList<HourTasks> dataHourTask;
    boolean AtLeastOneTask = false;

    public HourAdapter(@NonNull Context context, ArrayList<HourTasks> hourTasks, ArrayList<Task> input) {
        super(context, 0, hourTasks);
        dataHourTask = hourTasks;
        data = input;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HourTasks hourTasks = getItem(position);

        //i have no idea what this line does
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);

        setHour(convertView, hourTasks.getTime());
        setEvents(convertView, hourTasks.getTasks());

        return convertView;
    }

    private void setHour(View convertView, String time) {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//        timeTV.setText(time.format(formatter));

        timeTV.setText(time);

    }

    private void setEvents(View convertView, ArrayList<Task> tasks) {
        TextView event1 = convertView.findViewById(R.id.event1);
        TextView event2 = convertView.findViewById(R.id.event2);
        TextView event3 = convertView.findViewById(R.id.event3);

        if(tasks.size() == 0) {
            hideEvent(event1);
            hideEvent(event2);
            hideEvent(event3);
        }
        else if(tasks.size() == 1) {
            setEvent(event1, tasks.get(0));
            event1.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { openTaskInfo(convertView, tasks.get(0)); }});

            hideEvent(event2);
            hideEvent(event3);
        }
        else if(tasks.size() == 2) {
            setEvent(event1, tasks.get(0));
            event1.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { openTaskInfo(convertView, tasks.get(0)); }});

            setEvent(event2, tasks.get(1));
            event2.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { openTaskInfo(convertView, tasks.get(1)); }});

            hideEvent(event3);
        }
        else if(tasks.size() == 3) {
            setEvent(event1, tasks.get(0));
            event1.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { openTaskInfo(convertView, tasks.get(0)); }});

            setEvent(event2, tasks.get(1));
            event2.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { openTaskInfo(convertView, tasks.get(1)); }});

            setEvent(event3, tasks.get(2));
            event3.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { openTaskInfo(convertView, tasks.get(2)); }});

        }
        else {
            setEvent(event1, tasks.get(0));
            event1.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { openTaskInfo(convertView, tasks.get(0)); }});

            setEvent(event2, tasks.get(1));
            event2.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) { openTaskInfo(convertView, tasks.get(1)); }});

            event3.setVisibility(View.VISIBLE);
            String eventsNotShown = String.valueOf(tasks.size() - 2);
            eventsNotShown += " More Events";
            event3.setText(eventsNotShown);

            event3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle extras = new Bundle();
                    Intent myIntent = new Intent(convertView.getContext(), TaskViewer.class);

                    ArrayList<Integer> taskIDs = new ArrayList<Integer>();
                    for (Task t: tasks){
                        taskIDs.add(t.getId());
                    }

                    extras.putIntegerArrayList("Task id", taskIDs);

                    myIntent.putExtras(extras);

                    convertView.getContext().startActivity(myIntent);
                }
            });
        }
    }

    private void setEvent(TextView textView, Task task) {
        textView.setText(task.getTaskName());
        textView.setVisibility(View.VISIBLE);
    }

    private void hideEvent(TextView tv) {
        tv.setVisibility(View.INVISIBLE);
    }

    //creates a list of Tasks to be filtered
    @Override
    public Filter getFilter() {
        //define filter
        Filter filter = new Filter() {
            //filtering method
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //define filter results to return
                FilterResults filterResults = new FilterResults();

                //convert input 'constraint' to string
                String stringDate = constraint.toString();

                //check all tasks in data to filter date, if date is the same, add to filter
                ArrayList<Task> taskFilter = new ArrayList<>();
                for(Task task: data){
                    Log.v("Filter",task.getTaskDate());
                    if(task.getTaskDate().equals(stringDate)){
                        taskFilter.add(task);
                    }
                }

                filterResults.values = taskFilter;
                filterResults.count = taskFilter.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //set filter results to date filter list
                dataDateFilter = ((ArrayList<Task>) results.values);

                //cycle through every hour of the day and whitelisted task
                for (HourTasks ht:dataHourTask){

                    //get hour
                    String hourHour = getHour(ht.getTime());

                    //create array to store this hour's tasks
                    ArrayList<Task> hourTasks = new ArrayList<>();

                    for (Task t: dataDateFilter){

                        //get hour
                        String taskHour = getHour(t.getTaskStartTime());

                        if (hourHour.equals(taskHour)){
                            AtLeastOneTask = true;
                            hourTasks.add(t);
                        }
                    }

                    //set tasks for this hour
                    ht.setTasks(hourTasks);
                }

                //fofcvdrces getView to rerun
                notifyDataSetChanged();

                ////if god fails us:
                //notifyDataSetInvalidated();

            }
        };
        return filter;
    }
    //gets hour out of time
    public String getHour(String time){
        //split the colon
        String[] hourArray = time.split(":");
        //convert to int then string to remove all formatting
        String hour = String.valueOf(Integer.valueOf(hourArray[0]));

        return hour;
    }

    public void openTaskInfo(View convertView, Task t){
        Bundle extras = new Bundle();
        Intent myIntent = new Intent(convertView.getContext(), TaskViewActivity.class);
        extras.putInt("Task id", t.getId());

        myIntent.putExtras(extras);

        convertView.getContext().startActivity(myIntent);
    }
}