package sg.edu.np.mad.madassignment1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_viewer);

        Intent receivingEnd = getIntent();
        ArrayList<Integer> taskIDs = new ArrayList<Integer>();
        taskIDs = receivingEnd.getIntegerArrayListExtra("task id");

        DBHandler dbHandler = new DBHandler(this, null, null,6);

        ArrayList<Task> taskList = new ArrayList<Task>();

        for (int taskID: taskIDs){
            taskList.add(dbHandler.findTask(taskID));
        }

        //define recyclerView
        RecyclerView recyclerView = findViewById(R.id.taskViewerRecyclerView);

        // initialize recyclerview for TASKS
        //set adaptor to TaskRecyclerViewAdaptor, given taskList
        TaskRecyclerViewAdaptor mAdaptor = new TaskRecyclerViewAdaptor(taskList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdaptor);

        //For Swiping Menu
        TaskRecyclerTouchListener touchListener = new TaskRecyclerTouchListener(this, recyclerView);
        touchListener.setClickable(new TaskRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                //Toast.makeText(getApplicationContext(),taskList.get(position).getTaskName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        }).setSwipeOptionViews(R.id.edit_task, R.id.delete_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new TaskRecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {

                        Task task = taskList.get(position);

                        switch (viewID){
                            case R.id.edit_task:
                                Bundle extras = new Bundle();
                                Intent myIntent = new Intent(TaskViewer.this, TaskEditActivity.class);
                                extras.putInt("task id", task.getId());
                                myIntent.putExtras(extras);
                                startActivity(myIntent);
                                break;

                            case R.id.delete_task:
                                AlertDialog.Builder builder = new AlertDialog.Builder(TaskViewer.this);
                                builder.setMessage("Warning! This action is irreversible. Are you sure you want to delete this task?").setCancelable(true);
                                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dbHandler.deleteTask(task);

                                        //to "refresh" the change
                                        Intent myIntent = new Intent(TaskViewer.this, TaskActivity.class);
                                        startActivity(myIntent);

                                        //toast to indicate tasks successfully cleared
                                        Toast.makeText(TaskViewer.this, "Deleted Task", Toast.LENGTH_LONG).show();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                AlertDialog alert = builder.create();
                                alert.setTitle("Delete task");
                                alert.show();

                                //taskList.remove(position);
                                //mAdaptor.setTaskList(taskList);

                                mAdaptor.notifyDataSetChanged();
                                break;
                        }

                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
    }
}