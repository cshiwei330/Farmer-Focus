package sg.edu.np.mad.madassignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BarnTaskAdapter extends RecyclerView.Adapter<BarnTaskViewHolder> {

    ArrayList<Task> data;

    public BarnTaskAdapter(ArrayList<Task> input) {
        data = input;
    }

    @NonNull
    @Override
    public BarnTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfarm_barn_task_view_holder,parent,false);
        return new BarnTaskViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull BarnTaskViewHolder holder, int position) {
        //define task as t
        Task t = data.get(position);

        holder.taskName.setText(t.getTaskName());
        holder.taskDesc.setText(t.getTaskDesc());
        holder.taskDate.setText(t.getTaskDate());
        holder.taskTime.setText(t.getTaskStartTime());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void openTaskInfo(BarnTaskViewHolder holder, Task t){
        Bundle extras = new Bundle();
        Intent myIntent = new Intent(holder.taskName.getContext(), TaskViewActivity.class);
        extras.putInt("task id", t.getId());

        myIntent.putExtras(extras);

        holder.taskName.getContext().startActivity(myIntent);
    }
}
