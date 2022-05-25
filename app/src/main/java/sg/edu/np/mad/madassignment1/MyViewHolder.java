package sg.edu.np.mad.madassignment1;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView taskName;
    TextView taskDesc;

    public MyViewHolder(View itemView){
        super(itemView);
        taskName = itemView.findViewById(R.id.taskTitle);
        taskDesc = itemView.findViewById(R.id.taskDesc);
    }
}
