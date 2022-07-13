
package sg.edu.np.mad.madassignment1;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TaskRecyclerViewHolder extends RecyclerView.ViewHolder{
    TextView taskName;
    TextView taskDesc;
    CheckBox taskCheckBox;

    public TaskRecyclerViewHolder(View itemView){
        super(itemView);
        taskName = itemView.findViewById(R.id.task_name);
        taskDesc = itemView.findViewById(R.id.task_desc);
        taskCheckBox = itemView.findViewById(R.id.task_checkBox);
    }

}
