package sg.edu.np.mad.madassignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Timer;

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
        holder.taskName.setText(t.getId() + ". " +t.getTaskName());
        holder.taskDesc.setText(t.getTaskDesc());
        holder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.taskCheckBox.isChecked()) {
                    Toast.makeText(view.getContext(), "Mark as completed", Toast.LENGTH_SHORT).show();
                    t.setStatus(true);
                }
                else{
                    Toast.makeText(view.getContext(), "Mark as uncompleted", Toast.LENGTH_SHORT).show();
                    if (t.getStatus() == true){
                        t.setStatus(false);
                    }
                }
            }
        });
    }

    public int getItemCount(){
        return data.size();
    }
}
