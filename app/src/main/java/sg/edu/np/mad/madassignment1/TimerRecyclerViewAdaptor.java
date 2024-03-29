package sg.edu.np.mad.madassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimerRecyclerViewAdaptor extends RecyclerView.Adapter<TimerRecyclerViewHolder>{
    //private final OnItemListener onItemListener;
    ArrayList<Task> taskData;
    DBHandler dbHandler;

    public TimerRecyclerViewAdaptor(ArrayList<Task> input)
    {
        taskData = input;
        //this.onItemListener = onItemListener;
    }

    @Override
    public TimerRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.timertask_layout, parent, false);
        //define dbHandler
        dbHandler = new DBHandler(item.getContext(), null,null,6);
        return new TimerRecyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerRecyclerViewHolder holder, int position) {
        //defined task as t
        Task t = taskData.get(position);

        holder.taskName.setText(t.getTaskName());
        holder.taskDesc.setText(t.getTaskDesc());
        //holder.taskTime.setText(t.getTaskDuration());
        holder.taskTime.setText(t.getTaskStartTime() + " -" + t.getTaskEndTime()); // change to getTaskDuration()



        //when card is selected, bring user to TimerActivity
        //holder.taskName.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { openTaskInfo(holder, t); }});
        holder.taskDesc.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { openTaskInfo(holder, t); }});


        //prompt message when task is checked
        holder.taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Setting time for " + t.getTaskName() + "...", Toast.LENGTH_SHORT).show();
                openTaskInfo(holder, t);

                t.getTaskDuration();
                //openTaskInfo(holder, t);
            }
        });
    }



    @Override
    public int getItemCount() {
        return taskData.size();
    }

    public void clear() {
        int size = taskData.size();
        taskData.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskData = taskList;
        notifyDataSetChanged();
    }

    public void openTaskInfo(TimerRecyclerViewHolder holder, Task t){
        Bundle extras = new Bundle();
        extras.putInt("task id", t.getId());
        Log.v("test", String.valueOf(t.getId()));
        Log.v("testTaskID", String.valueOf(extras));

        Intent myIntent = new Intent(holder.taskName.getContext(), TimerActivity.class);
        myIntent.putExtras(extras);

//        AlertDialog.Builder builder = new AlertDialog.Builder(holder.taskName.getContext());
//        builder.setTitle("Would you like to start the timer now?");
//        builder.setMessage("Select yes if you would like to start the timer for the task " +
//                "which you have chosen." +
//                "\n\nWould you like to start doing the task?");
//        builder.setCancelable(true);

        holder.taskName.getContext().startActivity(myIntent);

    }


}
