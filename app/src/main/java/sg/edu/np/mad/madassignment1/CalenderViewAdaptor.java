package sg.edu.np.mad.madassignment1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalenderViewAdaptor extends RecyclerView.Adapter<CalenderViewViewHolder> implements Filterable {
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    ArrayList<String> tasksThisMonth;
    String selectedDay;

    public CalenderViewAdaptor(ArrayList<String> daysOfMonth, OnItemListener onItemListener, ArrayList<String> input)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.tasksThisMonth = input;
    }

    @NonNull
    @Override
    public CalenderViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Log.d("VIEWTYPE",String.valueOf(viewType));
        View view = inflater.inflate(R.layout.calender_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalenderViewViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderViewViewHolder holder, int position)
    {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        holder.eventRing.setVisibility(View.GONE);

        for (String taskday : tasksThisMonth ){
            if (daysOfMonth.get(position).equals(taskday)){
                holder.eventRing.setVisibility(View.VISIBLE);
                break;
            }
            else {
                holder.eventRing.setVisibility(View.GONE);
            }
        }

        if(selectedDay != null && selectedDay.equals(daysOfMonth.get(position))){
            Log.v("selectedDay:",selectedDay);
            Log.v("Position:",String.valueOf(position));
            holder.cellConstraintLayout.setBackgroundResource(R.color.darkBeige);
        }
    }

    //creates a list of Tasks to be filtered
    @Override
    public Filter getFilter() {
        //define filter
        Filter filter = new Filter() {
            //filtering method
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //define filter results to return
                FilterResults filterResults = new FilterResults();

                String stringDate = constraint.toString();
                String[] stringArray = stringDate.split("/");

                filterResults.values = stringArray[0];

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //set filter results to date filter list
                selectedDay = String.valueOf(results.values);
                //forces onBindViewHolder to rerun
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}