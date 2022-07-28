package sg.edu.np.mad.madassignment1;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SiloTaskViewHolder extends RecyclerView.ViewHolder {

    TextView taskName,taskDesc,timesCompleted;

    public SiloTaskViewHolder(View itemview){
        super(itemview);
        taskName = itemview.findViewById(R.id.task_name);
        taskDesc = itemview.findViewById(R.id.task_desc);
        timesCompleted = itemview.findViewById(R.id.times_completed);
    }
}
