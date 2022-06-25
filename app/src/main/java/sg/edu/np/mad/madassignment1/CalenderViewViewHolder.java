package sg.edu.np.mad.madassignment1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalenderViewViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

    public final View cellConstraintLayout;
    public final TextView dayOfMonth;
    public final ImageView eventRing;
    private final CalenderViewAdaptor.OnItemListener onItemListener;

    public CalenderViewViewHolder(@NonNull View itemView, CalenderViewAdaptor.OnItemListener onItemListener)
    {
        super(itemView);
        cellConstraintLayout = itemView.findViewById(R.id.cellConstraintLayout);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        eventRing = itemView.findViewById(R.id.eventRing);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}

