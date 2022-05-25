package sg.edu.np.mad.madassignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdaptor extends RecyclerView.Adapter<MyViewHolder> {
    ArrayList<Task> data;

    public MyAdaptor(ArrayList<Task> input) {
        data = input;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_layout,parent,false);
        return new MyViewHolder(item);
    }

    public void onBindViewHolder(MyViewHolder holder, int position){
        Task t = data.get(position);
        holder.taskName.setText(t.getTaskName());
        holder.taskDesc.setText(t.getTaskDesc());
    }

    public int getItemCount(){
        return data.size();
    }
}
