package sg.edu.np.mad.madassignment1;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimerRecyclerViewHolder extends RecyclerView.ViewHolder {
    public final TextView taskName;
    public final TextView taskDesc;
    public final TextView taskTime;
    public final TextView endTIme;

    public TimerRecyclerViewHolder(@NonNull View itemView){
        super(itemView);
        taskName = itemView.findViewById(R.id.task_name);
        taskDesc = itemView.findViewById(R.id.task_desc);
        taskTime = itemView.findViewById(R.id.task_time);
        endTIme = itemView.findViewById(R.id.end_time);
    }
}
