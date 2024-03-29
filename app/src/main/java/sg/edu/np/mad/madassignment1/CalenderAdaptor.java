package sg.edu.np.mad.madassignment1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalenderAdaptor extends RecyclerView.Adapter<CalenderViewHolder> implements Filterable {

    ArrayList<Task> data;
    ArrayList<Task> dataDateFilter;
    DBHandler dbHandler;

    public CalenderAdaptor (ArrayList<Task> input) {
        data = input;
    }

    public CalenderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_to_do_list_layout,parent,false);
        //define dbHandler
        dbHandler = new DBHandler(item.getContext(), null, null,6);
        return new CalenderViewHolder(item);
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

                //convert input 'constraint' to string
                String stringDate = constraint.toString();

                //check all tasks in data to filter date, if date is the same, add to filter
                ArrayList<Task> taskFilter = new ArrayList<>();
                for(Task task: data){
                    Log.v("Filter",task.getTaskDate());
                    if(task.getTaskDate().equals(stringDate)){
                        taskFilter.add(task);
                    }
                }

                filterResults.values = taskFilter;
                filterResults.count = taskFilter.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //set filter results to date filter list
                dataDateFilter = ((ArrayList<Task>) results.values);
                //forces onBindViewHolder to rerun
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    @Override
    public void onBindViewHolder(CalenderViewHolder holder, int position) {

        //define task as t
        Task t = data.get(position);
        //if filter is NOT null (initialized), AND NOT empty
        if (dataDateFilter != null && dataDateFilter.size() > 0) {
            //check all filter tasks to task t if similar
            for (Task task : dataDateFilter) {
                //if present in filter, set to visible and break
                if (t == task) {
                    Log.v("CAL_VIS", String.valueOf(t.getTaskDate()));
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    Log.v("CALENDER VISIBLE",String.valueOf(position));
                    break;
                }
                //else, set to invisible
                else {
                    Log.v("CAL_INVIS", String.valueOf(t.getTaskDate()));
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }
            }
        }
        //filter is initialized, thus a filter has been applied, but is empty, set all to 0 size
        else {
            Log.v("ELSE", String.valueOf(t.getTaskDate()));
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }


        //convert int data to string
        String stringDate = String.format(t.getTaskDate());

        //set viewholder details
        holder.taskName.setText(t.getId() + ". " +t.getTaskName());
        holder.taskDesc.setText(t.getTaskDesc());
        holder.taskTime.setText(t.getTaskStartTime());

        //if checked, set box to checked
        if (t.getStatus()==1){
            holder.taskCheckBox.setChecked(true);
        }

        holder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.taskCheckBox.isChecked()) {
                    Toast.makeText(view.getContext(), "Mark as completed", Toast.LENGTH_SHORT).show();
                    t.setStatus(1);
                    dbHandler.changeTaskStatus(t);

                }
                else{
                    Toast.makeText(view.getContext(), "Mark as uncompleted", Toast.LENGTH_SHORT).show();
                    if (t.getStatus() == 1){
                        t.setStatus(0);
                        dbHandler.changeTaskStatus(t);
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
