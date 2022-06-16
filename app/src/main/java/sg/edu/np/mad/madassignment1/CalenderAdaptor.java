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

import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalenderAdaptor extends RecyclerView.Adapter<CalenderViewHolder> implements Filterable {

    ArrayList<Task> data;
    ArrayList<Task> dataDeep;

    public CalenderAdaptor (ArrayList<Task> input) {
        data = input;
    }


    public CalenderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_to_do_list_layout,parent,false);
        return new CalenderViewHolder(item);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                Log.v("HOW",String.valueOf(constraint));
                String stringDate = constraint.toString();
                String[] stringDateArr = stringDate.split("/",3);
                //force initialize to null
                int[] intDateArr = new int[stringDateArr.length];
                for(int i = 0;i < stringDateArr.length;i++)
                {
                    //intDateArr[0] == dayOfMonth
                    //intDateArr[0] == month
                    //intDateArr[0] == year
                    intDateArr[i] = Integer.parseInt(stringDateArr[i]);
                }

                ArrayList<Task> taskFilter = new ArrayList<Task>();
                for(Task task: data){
                    Log.v("Filter",String.valueOf(task.getTaskYear())+ "/" +
                            String.valueOf(task.getTaskMonth()) + "/" +
                            String.valueOf(task.getTaskDayOfMonth()));
                    if(task.getTaskYear() == intDateArr[2] &&
                            task.getTaskMonth() == intDateArr[1] &&
                            task.getTaskDayOfMonth() == intDateArr[0]){
                        taskFilter.add(task);
                    }
                }

                filterResults.values = taskFilter;
                filterResults.count = taskFilter.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                dataDeep = ((ArrayList<Task>) results.values);
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    @Override
    public void onBindViewHolder(CalenderViewHolder holder, int position) {

        //define task as t
        Task t = data.get(position);

        if (dataDeep != null && dataDeep.size() > 0){
            for(Task task:dataDeep){
                holder.itemView.setVisibility(View.VISIBLE);
                if(t == task){
                    Log.v("CALVIS",String.valueOf(t.getTaskDayOfMonth()));
                    holder.itemView.setLayoutParams(holder.visible);
                }
                else{
                    Log.v("CALINVIS",String.valueOf(t.getTaskDayOfMonth()));
                    holder.itemView.setLayoutParams(holder.params);
                }
            }
        }
        else{
            for(Task task:data){
                Log.v("ELSE",String.valueOf(task.getTaskDayOfMonth()));
                holder.itemView.setLayoutParams(holder.params);
                holder.itemView.setVisibility(View.GONE);
            }
        }

        //convert int data to string
        String stringDate = String.format("%d/%d/%d",t.getTaskDayOfMonth(),t.getTaskMonth(),t.getTaskYear());
        //date format
        SimpleDateFormat fmt = new SimpleDateFormat("dd/mm/yyyy");
        //give dumby value to taskDate for forced initialization
        Date taskDate = null;


        //try catch because .parse throws errors before compiling??????
        try {
            //convert to date class
            taskDate = fmt.parse(stringDate);
        }
        catch(ParseException pe){ }

        //convert date object to string with chosen dateformat
        String strDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(taskDate);


        holder.taskName.setText(t.getId() + ". " +t.getTaskName());
        holder.taskDesc.setText(t.getTaskDesc());
        holder.taskDate.setText(strDate);

        holder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.taskCheckBox.isChecked()) {
                    Toast.makeText(view.getContext(), "Mark as completed", Toast.LENGTH_SHORT).show();
                    t.setStatus(1);
                }
                else{
                    Toast.makeText(view.getContext(), "Mark as uncompleted", Toast.LENGTH_SHORT).show();
                    if (t.getStatus() == 1){
                        t.setStatus(0);
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
