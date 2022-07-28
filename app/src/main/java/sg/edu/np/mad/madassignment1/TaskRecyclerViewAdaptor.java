package sg.edu.np.mad.madassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskRecyclerViewAdaptor extends RecyclerView.Adapter<TaskRecyclerViewHolder> {

    ArrayList<Task> taskData;
    DBHandler dbHandler;

    public TaskRecyclerViewAdaptor(ArrayList<Task> input) { taskData = input; }

    public TaskRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        //define dbHandler
        dbHandler = new DBHandler(item.getContext(), null,null,6);
        return new TaskRecyclerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecyclerViewHolder holder, int position) {
        //defined task as t
        Task t = taskData.get(position);

        holder.taskName.setText(t.getTaskName());
        holder.taskDesc.setText(t.getTaskDesc());
        holder.taskDate.setText(t.getTaskDate());

        //if checked, set box to checked
        if (t.getStatus()==1){
            holder.taskCheckBox.setChecked(true);
        }
        else {
            holder.taskCheckBox.setChecked(false);
        }

        //when card is selected, bring user to view task Activity
        holder.taskName.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { openTaskInfo(holder, t); }});
        holder.taskDesc.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { openTaskInfo(holder, t); }});


        //prompt message when task is checked
        holder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.taskCheckBox.isChecked()) {
                    Toast.makeText(view.getContext(), "Mark as completed", Toast.LENGTH_SHORT).show();
                    t.setStatus(1);
                    dbHandler.changeTaskStatus(t);

                }
                else{
                    Toast.makeText(view.getContext(), "Mark as uncompleted", Toast.LENGTH_SHORT).show();
                    if (t.getStatus() == 1){
                        t.setStatus(0);
                        dbHandler.changeTaskStatus(t);
                    }
                }
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

    public void openTaskInfo(TaskRecyclerViewHolder holder, Task t){
        Bundle extras = new Bundle();
        Intent myIntent = new Intent(holder.taskName.getContext(), TaskViewActivity.class);
        extras.putInt("Task id", t.getId());

        myIntent.putExtras(extras);

        holder.taskName.getContext().startActivity(myIntent);
    }
}
