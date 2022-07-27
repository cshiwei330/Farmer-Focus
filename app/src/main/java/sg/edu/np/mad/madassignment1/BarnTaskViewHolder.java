package sg.edu.np.mad.madassignment1;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BarnTaskViewHolder extends RecyclerView.ViewHolder{

    TextView taskName;
    TextView taskDesc;
    TextView taskDate;
    TextView taskTime;

    public BarnTaskViewHolder(View itemView){
        super(itemView);
        taskName = itemView.findViewById(R.id.task_name);
        taskDesc = itemView.findViewById(R.id.task_desc);
        taskDate = itemView.findViewById(R.id.task_date);
        taskTime = itemView.findViewById(R.id.task_time);
    }
}
