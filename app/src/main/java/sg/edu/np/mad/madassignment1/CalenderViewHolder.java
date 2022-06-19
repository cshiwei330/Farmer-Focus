package sg.edu.np.mad.madassignment1;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CalenderViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout.LayoutParams params;
    public LinearLayout.LayoutParams visible;
    public LinearLayout rootView; //the outermost view from your layout. Note that it doesn't necessarily have to be a LinearLayout.


    TextView taskName;
    TextView taskDesc;
    CheckBox taskCheckBox;
    TextView taskId;
    TextView taskDate;
    TextView taskTime;

    public CalenderViewHolder(View itemView){
        super(itemView);
        taskName = itemView.findViewById(R.id.taskTitle);
        taskDesc = itemView.findViewById(R.id.taskDesc);
        taskCheckBox = itemView.findViewById(R.id.taskCheckBox);
        taskDate = itemView.findViewById(R.id.taskDate);
        taskTime = itemView.findViewById(R.id.taskTime);
//        params = new LinearLayout.LayoutParams(0, 0);
//        visible = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        //rootView = itemView.findViewById(R.id.rootView);
    }
}
