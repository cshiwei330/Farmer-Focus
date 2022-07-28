package sg.edu.np.mad.madassignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SiloTaskAdapter extends RecyclerView.Adapter<SiloTaskViewHolder> {

    private ArrayList<ArrayList<Task>> data;

    public SiloTaskAdapter(ArrayList<ArrayList<Task>> input){ data = input;}

    @NonNull
    @Override
    public SiloTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfarm_silo_task_view_holder,parent,false);
        return new SiloTaskViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull SiloTaskViewHolder holder, int position) {
        //define task array list as recurringUnique
        ArrayList<Task> recurringUnique = data.get(position);

        holder.taskName.setText(recurringUnique.get(0).getTaskName());
        holder.taskDesc.setText(recurringUnique.get(0).getTaskDesc());

        //get number of completed
        int numCompleted = 0;
        for (Task t:recurringUnique) {
            if (t.getStatus()==1){
                numCompleted += 1;
            }
        }

        holder.timesCompleted.setText(String.valueOf(numCompleted));
    }

    @Override
    public int getItemCount() { return data.size(); }
}
