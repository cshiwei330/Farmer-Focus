package sg.edu.np.mad.madassignment1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class MyAdaptor extends RecyclerView.Adapter<MyViewHolder> {
    ArrayList<Task> data;

    public MyAdaptor(ArrayList<Task> input) {
        data = input;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_list_layout,parent,false);
        return new MyViewHolder(item);
    }

    public void onBindViewHolder(MyViewHolder holder, int position){
        //define task as t
        Task t = data.get(position);

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

        holder.taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                Intent myIntent = new Intent(holder.taskName.getContext(), ViewTaskActivity.class);
                extras.putInt("task id", t.getId());
                extras.putInt("task status", t.getStatus());
                extras.putString("task name", t.getTaskName());
                extras.putString("task desc", t.getTaskDesc());
                extras.putInt("task hour", t.getTaskHour());
                extras.putInt("task minute", t.getTaskYear());
                extras.putInt("task year", t.getTaskYear());
                extras.putInt("task month", t.getTaskMonth());
                extras.putInt("task dayOfMonth", t.getTaskDayOfMonth());
                myIntent.putExtras(extras);

                holder.taskName.getContext().startActivity(myIntent);
            }
        });

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

    public int getItemCount(){
        return data.size();
    }

    public void clear() {
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }
}


